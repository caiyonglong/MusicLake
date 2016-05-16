package com.cyl.music_hnust.lyric;

/**
 * Created by yonglong on 2016/4/23.
 */
public interface ILrcViewListener {
    /**
     * 当歌词被用户上下拖动的时候回调该方法
     */
    void onLrcSeeked(int newPosition, LrcRow row);
}