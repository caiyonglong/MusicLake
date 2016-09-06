package com.cyl.music_hnust.model;

import android.graphics.Bitmap;

/**
 * 作者：yonglong on 2016/8/9 10:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class MusicInfo {
    // 歌曲类型 本地/网络
    private Type type;
    // [本地歌曲]歌曲id
    private long id;
    // 音乐标题
    private String title;
    // 艺术家
    private String artist;
    // 专辑
    private String album;
    // 持续时间
    private long duration;
    // 音乐路径
    private String uri;
    // [本地歌曲]专辑封面路径
    private String coverUri;
    // 文件名
    private String fileName;
    // [网络歌曲]专辑封面bitmap
    private Bitmap cover;
    // 文件大小
    private long fileSize;
    // 发行日期
    private String year;

    private enum  Type {
        ONLINE,
        LOCAL
    }
}
