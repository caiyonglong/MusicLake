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
                + DBData.MUSIC_LRC_PATH + " , "
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
        insert = "insert into " + DBData.PLAYLIST_TABLE + " values (00000 ,'播放队列',10,233555)";
        System.out.println(insert);


        String select = "SELECT *" +
                " , " + "(SELECT count(" +
                DBData.MTP_TABLE + ".mid ) FROM " +
                DBData.MTP_TABLE + " WHERE " +
                DBData.MTP_TABLE + ".pid = " +
                DBData.PLAYLIST_TABLE + ".pid) AS num FROM " +
                DBData.PLAYLIST_TABLE + " where " +
                DBData.PLAYLIST_TABLE + "." + DBData.PLAYLIST_DATE + "is not null ";

        System.out.println(select);


        select = "select * from "
                + DBData.MUSIC_TABLE + " , "
                + DBData.MTP_TABLE + " where "
                + DBData.MUSIC_TABLE + ".mid = "
                + DBData.MTP_TABLE + ".mid " + "and "
                + DBData.MTP_TABLE + ".pid=0"  ;

        System.out.println(select);

        sql = "SELECT DISTINCT " +
                " music.mid, " +
                " music.name, " +
                " music.filename, " +
                " music.path, " +
                " music.duration, " +
                " music.size, " +
                " music.artist_id, " +
                " music.artist, " +
                " music.album, " +
                " music.album_id, " +
                " music.cover, " +
                " music.coverBig, " +
                " music.coverSmall, " +
                " music.type, " +
                " music.is_love, " +
                " music.is_online, " +
                " music.prefix, " +
                " music.years, " +
                " ( " +
                "  SELECT " +
                "   music_playlist.date_added " +
                "  FROM " +
                "   music_playlist " +
                "  WHERE " +
                "   music_playlist.pid = 0 " +
                "  AND music_playlist.mid = music.mid " +
                "  ORDER BY " +
                "   music_playlist.date_added DESC " +
                " ) AS time, " +
                " ( " +
                "  SELECT " +
                "   Count(music_playlist.mid) " +
                "  FROM " +
                "   music_playlist " +
                "  WHERE " +
                "   music_playlist.pid = 0 " +
                "  AND music_playlist.mid = music.mid " +
                " ) AS num " +
                "FROM " +
                " music_playlist, " +
                " music " +
                "WHERE " +
                " music_playlist.mid = music.mid " +
                "AND music_playlist.pid = 0 " +
                "ORDER BY " +
                " time DESC";

        System.out.println(sql);

    }
}