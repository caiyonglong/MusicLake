package com.cyl.music_hnust.model;

import java.util.List;

/**
 * 作者：yonglong on 2016/9/27 00:26
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchMusic {
    private List<Song> song;

    public List<Song> getSong() {
        return song;
    }

    public void setSong(List<Song> song) {
        this.song = song;
    }

    public static class Song {
        String songname;
        String artistname;
        String songid;

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }
}
