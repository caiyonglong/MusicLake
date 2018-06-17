package com.cyl.musiclake.db;

import com.cyl.musiclake.data.db.Music;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonglong on 2016/11/23.
 */

public class Album extends LitePalSupport {

    private String id;
    private String name;
    private String artistName;
    private String cover;
    private long artistId;
    private int count;

    private List<Music> songs = new ArrayList<>();

    public Album() {
    }

    public Album(String id, String name, String artistName, long artistId, int count) {
        this.name = name;
        this.id = id;
        this.artistName = artistName;
        this.artistId = artistId;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getId() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(List<Music> songs) {
        this.songs = songs;
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
