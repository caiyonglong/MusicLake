package com.cyl.musiclake.bean;

/**
 * Created by yonglong on 2016/11/23.
 */

public class Album {

    private long id;
    private String name;
    private String artistName;
    private String cover;
    private long artistId;
    private int count;

    public Album() {
    }

    public Album(long id, String name, String artistName, long artistId, int count) {
        this.name = name;
        this.id = id;
        this.artistName = artistName;
        this.artistId = artistId;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public long getArtistId() {
        return artistId;
    }

    public int getCount() {
        return count;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", artistName='" + artistName + '\'' +
                ", artistId=" + artistId +
                ", count=" + count +
                '}';
    }
}
