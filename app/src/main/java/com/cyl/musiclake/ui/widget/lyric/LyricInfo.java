package com.cyl.musiclake.ui.widget.lyric;

import java.util.List;

/**
 * Author   : D22434
 * version  : 2018/3/7
 * function :
 */

public class LyricInfo {

    public List<LineInfo> songLines;

    public String song_artist;  // 歌手
    public String song_title;  // 标题
    public String song_album;  // 专辑
    long song_offset;  // 偏移量

    public List<LineInfo> getSongLines() {
        return songLines;
    }

    public void setsongLines(List<LineInfo> songLines) {
        this.songLines = songLines;
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


    public static class LineInfo {
        public String content;  // 歌词内容
        public int line;  // 歌词内容
        public long start;  // 开始时间

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
            this.line = 1;
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
                "songLines=" + songLines +
                ", song_artist='" + song_artist + '\'' +
                ", song_title='" + song_title + '\'' +
                ", song_album='" + song_album + '\'' +
                ", song_offset=" + song_offset +
                '}';
    }
}
