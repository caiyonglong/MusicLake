package com.cyl.musiclake.musicapi.callback;

import com.cyl.musiclake.bean.Music;

import java.util.List;

/**
 * Created by master on 2018/5/15.
 */

public interface TopListApiListener extends BaseApiListener {
    void getTopList(List<Music> musicList);
}
