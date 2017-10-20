package com.cyl.music_hnust.bean.music;

/**
 * Created by yonglong on 2016/11/23.
 */

public class Artist {
    private String name;
    private long id;
    private int count;

    public Artist(long id,String name,  int count) {
        this.name = name;
        this.id = id;
        this.count = count;
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
