package com.cyl.music_hnust.db;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBData {
    /**
     * 音乐数据库名称
     */
    public static final String MUSIC_DB_NAME = "hkmusic.db";
    /**
     * 音乐数据库版本号
     */
    public static final int MUSIC_DB_VERSION = 1;

    /**
     * 歌单信息表
     */
    public static final String PLAYLIST_TABLENAME = "playlist";
    /**
     * 歌单信息表
     */
    public static final String MUSIC_TABLENAME = "music";

    /**
     * 歌单ID字段
     */
    public static final String PLAYLIST_ID = "id";
    /**
     * 歌单文件名字段(作为判断是否唯一存在)
     */
    public static final String PLAYLIST_TITLE = "name";


    /**
     * 音乐ID字段
     */
    public static final String MUSIC_ID = "music_id";
    /**
     * 音乐名称字段
     */
    public static final String MUSIC_NAME = "title";
    /**
     * 音乐路径字段
     */
    public static final String MUSIC_PATH = "uri";
    /**
     * 音乐时长字段
     */
    public static final String MUSIC_TIME = "time";
    /**
     * 音乐文件大小字段
     */
    public static final String MUSIC_SIZE = "size";
    /**
     * 音乐艺术家字段
     */
    public static final String MUSIC_ARTIST = "artist";
    /**
     * 音乐专辑字段
     */
    public static final String MUSIC_ALBUM = "album";
    /**
     * 音乐专辑图片路径字段
     */
    public static final String MUSIC_ALBUM_PIC = "albumPic";
    /**
     * 发行日期
     */
    public static final String MUSIC_YEARS = "years";
    /**
     * 发行日期
     */
    public static final String MUSIC_FILENAME= "filename";


}
