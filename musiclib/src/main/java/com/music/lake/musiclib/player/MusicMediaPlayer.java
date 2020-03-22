package com.music.lake.musiclib.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import com.danikula.videocache.HttpProxyCacheServer;
import com.music.lake.musiclib.MusicPlayerManager;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.utils.LogUtil;


/**
 * Created by D22434 on 2018/1/16.
 */

public class MusicMediaPlayer extends BasePlayer implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    private String TAG = "MusicPlayerEngine";
    private MediaPlayer mCurrentMediaPlayer;
    //是否已经初始化
    private boolean mIsInitialized = false;
    //是否已经初始化
    private boolean mIsPrepared = false;
    private Context context;

    public MusicMediaPlayer(final Context context) {
        this.context = context;
        initMediaPlayer();
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    }

    private void initMediaPlayer() {
        mCurrentMediaPlayer = new MediaPlayer();
        mCurrentMediaPlayer.setOnPreparedListener(this);
        mCurrentMediaPlayer.setOnBufferingUpdateListener(this);
        mCurrentMediaPlayer.setOnErrorListener(this);
        mCurrentMediaPlayer.setOnCompletionListener(this);
    }

    public void setMusicInfo(final BaseMusicInfo baseMusicInfo) {
        this.mNowPlayingMusic = baseMusicInfo;
    }

    public void setDataSource(final String path) {
        mIsInitialized = setDataSourceImpl(path);
    }

    private boolean setDataSourceImpl(final String path) {
        if (path == null) return false;
        try {
            mIsPrepared = false;
            mCurrentMediaPlayer.reset();
            if (listener != null) {
                listener.onPlayerStateChanged(false);
            }
            boolean cacheSetting = MusicPlayerManager.getInstance().isHasCache();
            LogUtil.d(TAG, "缓存设置：" + cacheSetting);
            //本地歌曲无需缓存
            if (path.startsWith("content://") || path.startsWith("/storage")) {
                mCurrentMediaPlayer.setDataSource(context, Uri.parse(path));
            } else if (cacheSetting) {
                //缓存开启，读取缓存
                HttpProxyCacheServer proxy = MusicPlayerManager.getProxy();
                String proxyUrl = proxy.getProxyUrl(path);
                LogUtil.d(TAG, "设置缓存,缓存地址：proxyUrl=" + proxyUrl);
                mCurrentMediaPlayer.setDataSource(proxyUrl);
            } else {
                //不缓存
                mCurrentMediaPlayer.setDataSource(path);
            }
            LogUtil.d(TAG, "prepareAsync");
            mCurrentMediaPlayer.prepareAsync();
            if (listener != null) {
                listener.onLoading(true);
            }
        } catch (Exception todo) {
            LogUtil.e(TAG, "Exception:" + todo.getMessage());
            todo.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    public boolean isPrepared() {
        return mIsPrepared;
    }

    @Override
    public void start() {
        super.start();
        LogUtil.d(TAG, "start");
        mCurrentMediaPlayer.start();
        if (listener != null) {
            listener.onPlayerStateChanged(isPlaying());
        }
    }

    @Override
    public void stop() {
        super.stop();
        LogUtil.d(TAG, "stop");
        try {
            mCurrentMediaPlayer.reset();
            mIsInitialized = false;
            mIsPrepared = false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        super.release();
        LogUtil.d(TAG, "release");
        mCurrentMediaPlayer.release();
    }

    @Override
    public void pause() {
        super.pause();
        LogUtil.d(TAG, "pause");
        mCurrentMediaPlayer.pause();
        if (listener != null) {
            listener.onPlayerStateChanged(isPlaying());
        }
    }

    @Override
    public boolean isPlaying() {
        return mCurrentMediaPlayer.isPlaying();
    }

    /**
     * 只有在mediaPlayer prepared之后才能调用，不然会报-38错误
     */
    public long duration() {
        if (mIsPrepared) {
            return mCurrentMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 只有在mediaPlayer prepared之后才能调用，不然会报-38错误
     */
    @Override
    public long position() {
        if (mIsPrepared) {
            try {
                return mCurrentMediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public void seekTo(long ms) {
        super.seekTo(ms);
        mCurrentMediaPlayer.seekTo((int) ms);
    }

    @Override
    public void setVolume(final float vol) {
        super.setVolume(vol);
        LogUtil.e("Volume", "vol = " + vol);
        try {
            mCurrentMediaPlayer.setVolume(vol, vol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getAudioSessionId() {
        return mCurrentMediaPlayer.getAudioSessionId();
    }

    @Override
    public boolean onError(final MediaPlayer mp, final int what, final int extra) {
        LogUtil.e(TAG, "Music Server Error what: " + what + " extra: " + extra);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                mIsInitialized = false;
                //播放错误，需要重新释放mediaPlayer
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = new MediaPlayer();
                //唤醒锁，
                mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
                return true;
            default:
                break;
        }
        if (listener != null) {
            listener.onLoading(false);
            listener.onError();
        }
        return true;
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion(final MediaPlayer mp) {
        LogUtil.e(TAG, "onCompletion");
        if (mp == mCurrentMediaPlayer && listener != null) {
            listener.onCompletionNext();
        } else if (listener != null) {
            listener.onCompletionEnd();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.e(TAG, "onBufferingUpdate" + percent);
        if (listener != null) {
            listener.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.d(TAG, "onPrepared");
        mIsPrepared = true;
        if (playWhenReady) {
            mp.start();
        }
        if (listener != null) {
            listener.onPrepared();
            listener.onLoading(false);
            listener.onPlayerStateChanged(true);
        }
    }
}
