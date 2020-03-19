package com.music.lake.musiclib.player;

import com.music.lake.musiclib.bean.BaseMusicInfo;

public class BasePlayer {
    public BaseMusicInfo mNowPlayingMusic;

    public void stop() {

    }

    public void release() {
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
     *
     * @return
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

}
