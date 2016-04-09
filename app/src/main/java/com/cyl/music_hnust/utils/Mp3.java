package com.cyl.music_hnust.utils;

public class Mp3 {
    private long song_id;
    private long Album_id;
    private String title;
    private String artist;

    public long getAlbum_id() {
        return Album_id;
    }

    public void setAlbum_id(long album_id) {
        Album_id = album_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Mp3{" +
                "Album_id=" + Album_id +
                ", song_id=" + song_id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
