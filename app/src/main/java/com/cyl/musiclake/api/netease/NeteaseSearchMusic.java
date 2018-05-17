package com.cyl.musiclake.api.netease;

import java.util.List;

/**
 * Created by D22434 on 2018/1/5.
 */

public class NeteaseSearchMusic {
    private List<NeteaseMusic> songs;
    private int songcount;

    public List<NeteaseMusic> getSongs() {
        return songs;
    }

    public void setSongs(List<NeteaseMusic> songs) {
        this.songs = songs;
    }

    public int getSongcount() {
        return songcount;
    }

    public void setSongcount(int songcount) {
        this.songcount = songcount;
    }
}
