package com.music.lake.musiclib.player;

import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.playback.PlaybackListener;

public class BasePlayer {
    public BaseMusicInfo mNowPlayingMusic;
    public PlaybackListener listener;
    /**
     * 准备好了直接播放
     */
    public boolean playWhenReady;

    public void stop() {
    }

    public void release() {
    }

    public void start() {
    }

    public void pause() {
    }

    public boolean isPlaying() {
        return false;
    }

    public long position() {
        return 0;
    }

    public long duration() {
        return 0;
    }

    /**
     * 获取标题
     */
    public String getTitle() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic.getTitle();
        }
        return "";
    }

    /**
     * 获取歌手专辑
     *
     * @return
     */
    public String getArtistName() {
        if (mNowPlayingMusic != null) {
            return mNowPlayingMusic.getArtist();
        }
        return null;
    }

    public boolean isInitialized() {
        return true;
    }

    public void seekTo(long ms) {
    }

    public boolean isPrepared() {
        return false;
    }

    public int bufferedPercentage() {
        return 0;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void setDataSource(String uri) {
    }

    public void setMusicInfo(BaseMusicInfo mNowPlayingMusic) {
        this.mNowPlayingMusic = mNowPlayingMusic;
    }

    public void setPlayBackListener(PlaybackListener listener) {
        this.listener = listener;
    }

    public void setVolume(float mCurrentVolume) {
    }
}
