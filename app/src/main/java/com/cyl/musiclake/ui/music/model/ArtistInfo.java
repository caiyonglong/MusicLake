package com.cyl.musiclake.ui.music.model;

/**
 * Created by yonglong on 2016/11/23.
 */

public class ArtistInfo {
    private String name;
    private String url;
    private String desc;
    private long id;
    private int count;

    public ArtistInfo(long id, String name, int count) {
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
