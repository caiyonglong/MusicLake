package com.music.lake.musiclib.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import com.danikula.videocache.HttpProxyCacheServer;
import com.music.lake.musiclib.MusicPlayerManager;
import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.playback.PlaybackListener;
import com.music.lake.musiclib.utils.LogUtil;


/**
 * Created by D22434 on 2018/1/16.
 */

public class MusicPlayerEngine extends BasePlayer implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    private String TAG = "MusicPlayerEngine";
    private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();
    //是否已经初始化
    private boolean mIsInitialized = false;
    //是否已经初始化
    private boolean mIsPrepared = false;
    private Context context;
    private PlaybackListener listener;

    MusicPlayerEngine(final Context context) {
        this.context = context;
        listener = (PlaybackListener) context;
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    }

    public void setMusicInfo(final BaseMusicInfo baseMusicInfo) {
        this.mNowPlayingMusic = baseMusicInfo;
    }

    public void setDataSource(final String path) {
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
    }

    private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
        if (path == null) return false;
        try {
            if (player.isPlaying()) player.stop();
            mIsPrepared = false;
            player.reset();
            boolean cacheSetting = MusicPlayerManager.getInstance().isHasCache();
            LogUtil.d(TAG, "缓存设置：" + cacheSetting);
            //本地歌曲无需缓存
            if (path.startsWith("content://") || path.startsWith("/storage")) {
                player.setDataSource(context, Uri.parse(path));
            } else if (cacheSetting) {
                //缓存开启，读取缓存
                HttpProxyCacheServer proxy = MusicPlayerManager.getProxy();
                String proxyUrl = proxy.getProxyUrl(path);
                LogUtil.d(TAG, "设置缓存,缓存地址：proxyUrl=" + proxyUrl);
                player.setDataSource(proxyUrl);
            } else {
                //不缓存
                player.setDataSource(path);
            }
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);
            player.setOnErrorListener(this);
            player.setOnCompletionListener(this);
            player.prepareAsync();
        } catch (Exception todo) {
            LogUtil.e(TAG, "Exception:" + todo.getMessage());
            todo.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public boolean isPrepared() {
        return mIsPrepared;
    }

    public void start() {
        mCurrentMediaPlayer.start();
    }

    @Override
    public void stop() {
        super.stop();
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
        mCurrentMediaPlayer.release();
    }

    @Override
    public void pause() {
        super.pause();
        mCurrentMediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mCurrentMediaPlayer.isPlaying();
    }

    /**
     * getDuration 只能在prepared之后才能调用，不然会报-38错误
     *
     * @return
     */
    public long duration() {
        if (mIsPrepared) {
            return mCurrentMediaPlayer.getDuration();
        } else return 0;
    }

    public long position() {
        try {
            return mCurrentMediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            return -1;
        }
    }

    public void seek(final long whereto) {
        mCurrentMediaPlayer.seekTo((int) whereto);
    }


    public void setVolume(final float vol) {
        LogUtil.e("Volume", "vol = " + vol);
        try {
            mCurrentMediaPlayer.setVolume(vol, vol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                if (listener != null) {
                    listener.onError();
                }
                return true;
            default:
                break;
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
//        if (mp == mCurrentMediaPlayer) {
//            mHandler.sendEmptyMessage(MusicPlayerService.TRACK_WENT_TO_NEXT);
//        } else {
//            //        mService.get().mWakeLock.acquire(30000);
//            mHandler.sendEmptyMessage(MusicPlayerService.TRACK_PLAY_ENDED);
//            mHandler.sendEmptyMessage(MusicPlayerService.RELEASE_WAKELOCK);
//        }
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
        mp.start();
        if (listener != null) {
            listener.onPrepared();
        }
        if (!mIsPrepared) {
            mIsPrepared = true;
        }
    }

    private class TrackErrorInfo {

        private String audioId;
        private String trackName;

        public TrackErrorInfo(String audioId, String trackName) {
            this.audioId = audioId;
            this.trackName = trackName;
        }

        public String getAudioId() {
            return audioId;
        }

        public void setAudioId(String audioId) {
            this.audioId = audioId;
        }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }
    }
}
