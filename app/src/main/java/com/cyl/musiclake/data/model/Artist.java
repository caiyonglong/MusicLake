package com.cyl.musiclake.data.model;

/**
 * Created by yonglong on 2016/11/23.
 */

public class Artist {
    private String name;
    private long id;
    private int count;
    private int trackNum;

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
}
