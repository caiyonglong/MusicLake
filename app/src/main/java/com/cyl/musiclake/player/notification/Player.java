package com.cyl.musiclake.player.notification;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.cyl.musiclake.player.playback.PlaybackListener;
import com.cyl.musiclake.utils.LogUtil;

import java.io.IOException;

/**
 * Created by master on 2018/5/14.
 */

public class Player implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    private String TAG = "MusicPlayerEngine";
    private MediaPlayer mCurrentMediaPlayer = new MediaPlayer();
    private PlaybackListener listener;
    private Context context;


    //是否已经初始化
    private boolean mIsInitialized = false;
    //是否已经初始化
    private boolean mIsPrepared = false;

    public Player(Context context) {
        this.context = context;
        listener = (PlaybackListener) context;
    }

    public void setDataSource(final String path) {
        mIsInitialized = setDataSourceImpl(mCurrentMediaPlayer, path);
    }

    private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
        try {
            if (player.isPlaying()) player.stop();
            mIsPrepared = false;
            player.reset();
            if (path.startsWith("content://")) {
                player.setDataSource(context, Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);
            player.setOnErrorListener(this);
            player.setOnCompletionListener(this);
        } catch (final IOException todo) {
            return false;
        } catch (final IllegalArgumentException todo) {
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
        mCurrentMediaPlayer.setVolume(vol, vol);
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
                mCurrentMediaPlayer.release();
                mCurrentMediaPlayer = new MediaPlayer();
                return true;
            default:
                break;
        }
        return true;
    }


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
        mp.start();
        if (listener != null) {
            listener.onPrepared();
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
