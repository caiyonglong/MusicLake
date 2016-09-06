package com.cyl.music_hnust.service;

import com.cyl.music_hnust.model.Music;

/**
 * 作者：yonglong on 2016/8/11 19:10
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public interface OnPlayerListener {
    /**
     * 更新进度
     */
    void onUpdate(int progress);

    /**
     * 切换歌曲
     */
    void onChange(Music music);

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 继续播放
     */
    void onPlayerResume();

}
