package com.cyl.musiclake.event;

/**
 * Created by D22434 on 2018/1/10.
 */

public class LyricChangedEvent {
    private String lyric;
    private boolean status;

    public LyricChangedEvent(String lyric, boolean status) {
        this.lyric = lyric;
        this.status = status;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
