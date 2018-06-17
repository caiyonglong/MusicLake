package com.cyl.musiclake.db;

import com.cyl.musiclake.data.db.Music;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonglong on 2016/11/23.
 */

public class Artist extends LitePalSupport {
    private String name;
    private long id;
    private int count;
    private int trackNum;

    private List<Music> songs = new ArrayList<>();

    public Artist() {
    }

    public Artist(long id, String name, int count, int trackNum) {
        this.name = name;
        this.id = id;
        this.count = count;
        this.count = trackNum;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(int trackNum) {
        this.trackNum = trackNum;
    }

    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(List<Music> songs) {
        this.songs = songs;
    }
}
