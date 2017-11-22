package com.cyl.musiclake.mvp.model.music;

import java.util.List;

/**
 * 作者：yonglong on 2016/9/27 00:26
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchMusic {
    private List<SongList> song_list;

    public List<SongList> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongList> song_list) {
        this.song_list = song_list;
    }

    public class SongList {
        String  song_id;
        String  title="未知";
        String  artist_id;
        String  author="未知";
        String  album_title="未知";
        String  lrclink;

        public String getAlbum_title() {
            return album_title;
        }

        public void setAlbum_title(String album_title) {
            this.album_title = album_title;
        }

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
