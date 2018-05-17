package com.cyl.musiclake.player.playback;

import android.media.MediaPlayer;

/**
 * Created by master on 2018/5/14.
 * 播放回调
 */

public interface PlaybackListener {
    /**
     * 完成下一首
     */
    void onCompletionNext();

    /**
     * 完成结束
     */
    void onCompletionEnd();

    void onBufferingUpdate(MediaPlayer mp, int percent);

    void onPrepared();

    void onError();
}
