package com.cyl.music_hnust.model.music;

/**
 * Created by yonglong on 2016/11/23.
 */

public class Album {
    private String name;
    private long id;
    private String artistName;
    private long artistId;
    private int count;

    public Album(long id, String name,  String artistName, long artistId, int count) {
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
}
