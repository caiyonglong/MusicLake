package com.cyl.musiclake.event;

/**
 * Created by D22434 on 2018/1/10.
 */

public class StatusChangedEvent {
    private String mid;
    private boolean isPrepared;
    private boolean isPlaying;

    public StatusChangedEvent(String mid, boolean isPrepared, boolean isPlaying) {
        this.mid = mid;
        this.isPrepared = isPrepared;
        this.isPlaying = isPlaying;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
