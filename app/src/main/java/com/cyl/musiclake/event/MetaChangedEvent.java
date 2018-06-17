package com.cyl.musiclake.event;

import com.cyl.musiclake.data.db.Music;

/**
 * Created by D22434 on 2018/1/10.
 */

public class MetaChangedEvent {
    private Music music;

    public MetaChangedEvent(Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

}
