package com.cyl.music_hnust.dataloaders.db;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBData {

    /**
     * 歌单信息表
     */
    public static final String PLAYLIST_TABLE = "playlist";
    public static final String PLAYLIST_ID = "pid";
    public static final String PLAYLIST_NAME = "pName";

    /**
     * 歌曲信息表
     */
    public static final String MUSIC_TABLE = "music";
    public static final String MUSIC_ID = "mid";
    public static final String MUSIC_NAME = "mName";
    public static final String MUSIC_PATH = "mPath";
    public static final String MUSIC_TIME = "mTime";
    public static final String MUSIC_SIZE = "mSize";
    public static final String MUSIC_ARTIST = "mArtist";
    public static final String MUSIC_ALBUM = "mAlbum";
    public static final String MUSIC_ALBUM_ID = "mAlbumId";
    public static final String MUSIC_ALBUM_PATH = "mAlbumPath";
    public static final String MUSIC_YEARS = "years";
    public static final String MUSIC_FILENAME = "mFilename";
    /**
     * mp信息表
     */
    public static final String MTP_TABLE = "musicToPlaylist";
    public static final String MTP_PID = "pid";
    public static final String MTP_MID = "mid";
    /**
     * 播放队列信息表
     */
    public static final String QUEUE_TABLE = "playQueue";
    public static final String QUEUE_QID = "qid";
    public static final String QUEUE_MID = "mid";
}
