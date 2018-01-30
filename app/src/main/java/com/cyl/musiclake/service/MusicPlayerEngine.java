package com.cyl.musiclake.service;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static com.cyl.musiclake.service.MusicPlayerService.PREPARE_ASYNC_ENDED;
import static com.cyl.musiclake.service.MusicPlayerService.PREPARE_ASYNC_UPDATE;
import static com.cyl.musiclake.service.MusicPlayerService.RELEASE_WAKELOCK;
import static com.cyl.musiclake.service.MusicPlayerService.TRACK_PLAY_ENDED;
import static com.cyl.musiclake.service.MusicPlayerService.TRACK_PLAY_ERROR;
import static com.cyl.musiclake.service.MusicPlayerService.TRACK_WENT_TO_NEXT;

/**
 * Created by D22434 on 2018/1/16.
 */

public class MusicPlayerEngine implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {

    private String TAG = "MusicPlayerEngine";

    private final WeakReference<MusicPlayerService> mService;

    private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();

    private MediaPlayer mNextMediaPlayer;

    private Handler mHandler;

    //是否已经初始化
    private boolean mIsInitialized = false;

    public MusicPlayerEngine(final MusicPlayerService service) {
        mService = new WeakReference<MusicPlayerService>(service);
        mCurrentMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    public void setDataSource(final String path) {
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
        if (mIsInitialized) {
            setNextDataSource(null);
        }
    }

    private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
        try {
            player.reset();
            if (path.startsWith("content://")) {
                player.setDataSource(mService.get(), Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);
        } catch (final IOException todo) {
            return false;
        } catch (final IllegalArgumentException todo) {
            return false;
        }
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        return true;
    }

    public void setNextDataSource(final String path) {
        try {
            Log.e(TAG, "--" + path);
            mCurrentMediaPlayer.setNextMediaPlayer(null);

            if (mNextMediaPlayer != null) {
                mNextMediaPlayer.release();
                mNextMediaPlayer = null;
            }
            if (path == null) {
                return;
            }
            mNextMediaPlayer = new MediaPlayer();
            mNextMediaPlayer.setWakeMode(mService.get(), PowerManager.PARTIAL_WAKE_LOCK);
            mNextMediaPlayer.setAudioSessionId(getAudioSessionId());
            if (setDataSourceImpl(mNextMediaPlayer, path)) {
                mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer);
            } else {
                if (mNextMediaPlayer != null) {
                    mNextMediaPlayer.release();
                    mNextMediaPlayer = null;
                }
            }
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "Next media player is current one, continuing");
        } catch (IllegalStateException e) {
            Log.e(TAG, "Media player not initialized!");
            return;
        }
    }


    public void setHandler(final Handler handler) {
        mHandler = handler;
    }


    public boolean isInitialized() {
        return mIsInitialized;
    }


    public void start() {
        mCurrentMediaPlayer.start();
    }


    public void stop() {
        try {
            mCurrentMediaPlayer.reset();
            mIsInitialized = false;
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


    public long duration() {
        return mCurrentMediaPlayer.getDuration();
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
        mCurrentMediaPlayer.setVolume(vol, vol);
    }

    public int getAudioSessionId() {
        return mCurrentMediaPlayer.getAudioSessionId();
    }

    @Override
    public boolean onError(final MediaPlayer mp, final int what, final int extra) {
        Log.w(TAG, "Music Server Error what: " + what + " extra: " + extra);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                final MusicPlayerService service = mService.get();
                final TrackErrorInfo errorInfo = new TrackErrorInfo(service.getAudioId(),
                        service.getTitle());
                mIsInitialized = false;
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = new MediaPlayer();
                mCurrentMediaPlayer.setWakeMode(service, PowerManager.PARTIAL_WAKE_LOCK);
                Message msg = mHandler.obtainMessage(TRACK_PLAY_ERROR, errorInfo);
                mHandler.sendMessageDelayed(msg, 2000);
                return true;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onCompletion(final MediaPlayer mp) {
        if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
            mCurrentMediaPlayer.release();
            mCurrentMediaPlayer = mNextMediaPlayer;
            mNextMediaPlayer = null;
            mHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT);
        } else {
            mService.get().mWakeLock.acquire(30000);
            mHandler.sendEmptyMessage(TRACK_PLAY_ENDED);
            mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mHandler.sendEmptyMessage(PREPARE_ASYNC_ENDED);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Message message = mHandler.obtainMessage(PREPARE_ASYNC_UPDATE, percent);
        mHandler.sendMessage(message);
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
