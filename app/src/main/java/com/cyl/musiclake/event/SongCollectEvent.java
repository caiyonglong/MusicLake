package com.cyl.musiclake.event;

/**
 * Created by D22434 on 2018/1/10.
 */

public class SongCollectEvent {
    private String mid;
    private boolean collect;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }
}
