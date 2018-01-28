package com.cyl.musiclake.data.source.db;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBData {

    public static final String DEFAULT_ID = "_id";
    /**
     * 歌单信息表
     */
    public static final String PLAYLIST_TABLE = "playlist";
    public static final String PLAYLIST_ID = "pid";
    public static final String PLAYLIST_NAME = "name";
    public static final String PLAYLIST_ORDER = "play_order";
    public static final String PLAYLIST_COVER = "play_cover";
    public static final String PLAYLIST_DATE = "date_create";

    /**
     * 歌曲信息表19
     */
    public static final String MUSIC_TABLE = "music";

    public static final String MUSIC_ID = "mid";
    public static final String MUSIC_NAME = "name";
    public static final String MUSIC_ARTIST = "artist";
    public static final String MUSIC_ARTIST_ID = "artist_id";
    public static final String MUSIC_ALBUM = "album";
    public static final String MUSIC_ALBUM_ID = "album_id";

    public static final String MUSIC_PATH = "path";
    public static final String MUSIC_LRC_PATH = "lrc_path";
    public static final String MUSIC_TIME = "duration";
    public static final String MUSIC_SIZE = "size";
    public static final String MUSIC_COVER = "cover";
    public static final String MUSIC_COVER_BIG = "coverBig";

    public static final String MUSIC_COVER_SMALL = "coverSmall";
    public static final String MUSIC_YEARS = "years";
    public static final String MUSIC_FILENAME = "filename";
    public static final String IS_LOVE = "is_love";
    public static final String IS_ONLINE = "is_online";
    public static final String MUSIC_TYPE = "type";
    public static final String MUSIC_PREFIX = "prefix";

    /**
     * music playlist关联表
     */
    public static final String MTP_TABLE = "music_playlist";
    public static final String MTP_PID = "pid";
    public static final String MTP_MID = "mid";
    public static final String MTP_DATE = "date_added";

    public static final String PLAY_QUEUE = "0";
    public static final String HISTORY = "1";


}
