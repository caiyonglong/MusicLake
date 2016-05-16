package com.cyl.music_hnust.lyric;

import java.util.List;

/**
 * Created by yonglong on 2016/4/23.
 */
public interface ILrcView {
    /**
     * 设置要展示的歌词行集合
     */
    void setLrc(List<LrcRow> lrcRows);

    /**
     * 音乐播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     */
    void seekLrcToTime(long time,long duration);
    /**
     * 设置歌词拖动时候的监听类
     */
    void setListener(ILrcViewListener l);
}
