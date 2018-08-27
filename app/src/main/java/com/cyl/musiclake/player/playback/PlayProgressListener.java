package com.cyl.musiclake.player.playback;

/**
 * Created by master on 2018/5/14.
 * 播放回调
 */

public interface PlayProgressListener {

    void onProgressUpdate(long position, long duration);

}
