package com.cyl.musiclake.data.source.db;

import junit.framework.TestCase;

/**
 * Author   : D22434
 * version  : 2018/1/18
 * function :
 */
public class DBHelperTest extends TestCase {

    public void testName() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + DBData.MTP_TABLE + " ("
                + DBData.DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBData.MTP_MID + " varchar(20)  , "
                + DBData.MTP_PID + "  varchar(50) , "
                + DBData.MTP_DATE + "  ) ";
        System.out.println(sql);

        String sql1 = "CREATE TABLE IF NOT EXISTS "
                + DBData.PLAYLIST_TABLE + " ("
                + DBData.PLAYLIST_ID + " varchar(50) PRIMARY KEY, "
                + DBData.PLAYLIST_NAME + " ) ";

        System.out.println(sql1);

        String sql2 = "CREATE TABLE IF NOT EXISTS "
                + DBData.MUSIC_TABLE + " ("
                + DBData.MUSIC_ID + " varchar(20) PRIMARY KEY, "
                + DBData.MUSIC_NAME + " , "

                + DBData.MUSIC_FILENAME + " , "
                + DBData.MUSIC_PATH + " , "
                + DBData.MUSIC_TIME + " , "
                + DBData.MUSIC_SIZE + " , "

                + DBData.MUSIC_ARTIST_ID + " , "
                + DBData.MUSIC_ARTIST + " , "
                + DBData.MUSIC_ALBUM + " , "
                + DBData.MUSIC_ALBUM_ID + " , "

                + DBData.MUSIC_COVER + " , "
                + DBData.MUSIC_COVER_BIG + " , "
                + DBData.MUSIC_COVER_SMALL + " , "
                + DBData.IS_LOVE + " , "
                + DBData.IS_ONLINE + " , "
                + DBData.MUSIC_TYPE + " , "

                + DBData.MUSIC_YEARS + " ) ";

        System.out.println(sql2);
        String insert;
        insert = "insert into " + DBData.PLAYLIST_TABLE + " values ( " + "00000 ,'播放队列',100" + " )";
        System.out.println(insert);
        insert = "insert into " + DBData.PLAYLIST_TABLE + " values ( " + "00001 ,'最近播放',100" + " )";
        System.out.println(insert);

        String select = "SELECT " +
                DBData.PLAYLIST_TABLE + ".pid, " +
                DBData.PLAYLIST_TABLE + ".name " + " , " + "(SELECT count(" +
                DBData.MTP_TABLE + ".mid ) FROM " +
                DBData.MTP_TABLE + " WHERE " +
                DBData.MTP_TABLE + ".pid = " +
                DBData.PLAYLIST_TABLE + ".pid) AS num FROM " +
                DBData.PLAYLIST_TABLE;
        System.out.println(select);

        select = "select * from music inner join music_playlist " +
                "where music.mid = music_playlist.mid " +
                "and music_playlist.pid=0";
        System.out.println(select);

    }
}