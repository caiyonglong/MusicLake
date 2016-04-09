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
     * 音乐信息表
     */
    public static final String MUSIC_TABLENAME = "music";
    /**
     * 歌词信息表
     */
    public static final String LYRIC_TABLENAME = "lyric";
    /**
     * 歌单信息表
     */
    public static final String PLAYLIST_TABLENAME = "playlist";

    /**
     * 音乐ID字段
     */
    public static final String MUSIC_ID = "music_id";
    /**
     * 音乐文件名称字段(作为判断是否唯一存在)
     */
    public static final String MUSIC_FILE = "file";
    /**
     * 音乐名称字段
     */
    public static final String MUSIC_NAME = "name";
    /**
     * 音乐路径字段
     */
    public static final String MUSIC_PATH = "path";
    /**
     * 音乐所属文件夹字段
     */
    public static final String MUSIC_FOLDER = "folder";
    /**
     * 是否最喜爱音乐字段
     */
    public static final String MUSIC_FAVORITE = "favorite";
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
     * 音乐格式(编码类型)字段
     */
    public static final String MUSIC_FORMAT = "format";
    /**
     * 音乐专辑字段
     */
    public static final String MUSIC_ALBUM = "album";
    /**
     * 音乐专辑图片路径字段
     */
    public static final String MUSIC_ALBUM_PIC = "albumPic";
    /**
     * 音乐艺术家字段
     */
    public static final String MUSIC_YEARS = "years";
    /**
     * 歌词ID字段
     */
    public static final String LYRIC_ID = "id";
    /**
     * 歌词文件名字段(作为判断是否唯一存在)
     */
    public static final String LYRIC_FILE = "file";
    /**
     * 歌词路径字段
     */
    public static final String LYRIC_PATH = "path";

    /**
     * 歌单ID字段
     */
    public static final String PLAYLIST_ID = "id";
    /**
     * 歌单文件名字段(作为判断是否唯一存在)
     */
    public static final String PLAYLIST_TITLE = "name";

}
