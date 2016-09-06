package com.cyl.music_hnust.lyric;

/**
 * 歌词行
 * 包括该行歌词的时间，歌词内容
 */
public class LrcRow {
    private String text;
    private long time;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}