package com.cyl.musiclake.player;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.utils.LogUtil;
import com.danikula.videocache.HttpProxyCacheServer;

import java.lang.ref.WeakReference;

import static com.cyl.musiclake.player.MusicPlayerService.PLAYER_PREPARED;
import static com.cyl.musiclake.player.MusicPlayerService.PREPARE_ASYNC_UPDATE;
import static com.cyl.musiclake.player.MusicPlayerService.RELEASE_WAKELOCK;
import static com.cyl.musiclake.player.MusicPlayerService.TRACK_PLAY_ENDED;
import static com.cyl.musiclake.player.MusicPlayerService.TRACK_PLAY_ERROR;
import static com.cyl.musiclake.player.MusicPlayerService.TRACK_WENT_TO_NEXT;

/**
 * Created by D22434 on 2018/1/16.
 */

public class MusicPlayerEngine implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    private String TAG = "MusicPlayerEngine";

    private final WeakReference<MusicPlayerService> mService;

    private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();

    private Handler mHandler;

    //是否已经初始化
    private boolean mIsInitialized = false;
    //是否已经初始化
    private boolean mIsPrepared = false;

    MusicPlayerEngine(final MusicPlayerService service) {
        mService = new WeakReference<>(service);
        mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
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
            boolean cacheSetting = PreferenceManager.getDefaultSharedPreferences(MusicApp.getAppContext()).getBoolean("key_cache_mode", true);
            LogUtil.d(TAG, "缓存设置：" + cacheSetting);
            //本地歌曲无需缓存
            if (path.startsWith("content://") || path.startsWith("/storage")) {
                player.setDataSource(mService.get(), Uri.parse(path));
            } else if (cacheSetting) {
                //缓存开启，读取缓存
                HttpProxyCacheServer proxy = MusicApp.getProxy();
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

    public void setHandler(final Handler handler) {
        mHandler = handler;
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


    public void stop() {
        try {
            mCurrentMediaPlayer.reset();
            mIsInitialized = false;
            mIsPrepared = false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public void release() {
        mCurrentMediaPlayer.release();
    }


    public void pause() {
        mCurrentMediaPlayer.pause();
    }

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
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                final MusicPlayerService service = mService.get();
                final TrackErrorInfo errorInfo = new TrackErrorInfo(service.getAudioId(),
                        service.getTitle());
                mIsInitialized = false;
                //播放错误，需要重新释放mediaPlayer
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = new MediaPlayer();
                mCurrentMediaPlayer.setWakeMode(service, PowerManager.PARTIAL_WAKE_LOCK);
                Message msg = mHandler.obtainMessage(TRACK_PLAY_ERROR, errorInfo);
                mHandler.sendMessageDelayed(msg, 500);
                return true;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onCompletion(final MediaPlayer mp) {
        LogUtil.e(TAG, "onCompletion");
        if (mp == mCurrentMediaPlayer) {
            mHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT);
        } else {
            mService.get().mWakeLock.acquire(30000);
            mHandler.sendEmptyMessage(TRACK_PLAY_ENDED);
            mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.e(TAG, "onBufferingUpdate" + percent);
        Message message = mHandler.obtainMessage(PREPARE_ASYNC_UPDATE, percent);
        mHandler.sendMessage(message);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (!mIsPrepared) {
            mIsPrepared = true;
            Message message = mHandler.obtainMessage(PLAYER_PREPARED);
            mHandler.sendMessage(message);
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
