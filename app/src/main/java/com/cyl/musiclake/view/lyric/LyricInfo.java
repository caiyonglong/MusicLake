package com.cyl.musiclake.view.lyric;

import java.util.List;

/**
 * Author   : D22434
 * version  : 2018/3/7
 * function :
 */

public class LyricInfo {

    List<LineInfo> song_lines;

    String song_artist;  // 歌手
    String song_title;  // 标题
    String song_album;  // 专辑
    long song_offset;  // 偏移量

    public List<LineInfo> getSong_lines() {
        return song_lines;
    }

    public void setSong_lines(List<LineInfo> song_lines) {
        this.song_lines = song_lines;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getSong_album() {
        return song_album;
    }

    public void setSong_album(String song_album) {
        this.song_album = song_album;
    }

    public long getSong_offset() {
        return song_offset;
    }

    public void setSong_offset(long song_offset) {
        this.song_offset = song_offset;
    }


    static class LineInfo {
        String content;  // 歌词内容
        long start;  // 开始时间

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "LineInfo{" +
                    "content='" + content + '\'' +
                    ", start=" + start +
                    '}' + "\n";
        }
    }

    @Override
    public String toString() {
        return "LyricInfo{" +
                "song_lines=" + song_lines +
                ", song_artist='" + song_artist + '\'' +
                ", song_title='" + song_title + '\'' +
                ", song_album='" + song_album + '\'' +
                ", song_offset=" + song_offset +
                '}';
    }
}
