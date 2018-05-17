package com.cyl.musicapi.callback;

import com.cyl.musiclake.bean.Music;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public interface TopListApiListener {
    void getTopList(List<Music> musicList);
}
