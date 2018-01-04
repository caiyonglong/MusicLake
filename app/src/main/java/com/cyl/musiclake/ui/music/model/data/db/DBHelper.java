package com.cyl.musiclake.ui.music.model.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 永龙 on 2016/2/23.
 * 版本: 2016-8-12  v2.5
 */
public class DBHelper extends SQLiteOpenHelper {

    public static DBHelper mInstance = null;

    private static String MUSIC_DB_NAME = "hkmusic.db";
    private static int MUSIC_DB_VERSION = 2;


    /**
     * 创建数据库
     *
     * @param context 上下文
     */
    private DBHelper(Context context) {
        super(context, MUSIC_DB_NAME, null, MUSIC_DB_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBHelper.class) {
                if (mInstance == null) {
                    mInstance = new DBHelper(context);
                }
            }
        }
        return mInstance;
    }


    // TODO CREATE TABLE
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建歌单歌曲表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.MTP_TABLE + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBData.MTP_MID + " varchar(20) , "
                + DBData.MTP_PID + " varchar(50) ) ");
        // 创建歌单表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.PLAYLIST_TABLE + " ("
                + DBData.PLAYLIST_ID + " varchar(50) PRIMARY KEY, "
                + DBData.PLAYLIST_NAME + " ) ");
        // 创建歌曲表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.MUSIC_TABLE + " ("
                + DBData.MUSIC_ID + " varchar(20) PRIMARY KEY, "
                + DBData.MUSIC_NAME + " , "
                + DBData.MUSIC_FILENAME + " , "

                + DBData.MUSIC_PATH + " , "
                + DBData.MUSIC_TIME + " , "
                + DBData.MUSIC_SIZE + " , "
                + DBData.MUSIC_ARTIST + " , "

                + DBData.MUSIC_ALBUM + " , "
                + DBData.MUSIC_ALBUM_ID + " , "
                + DBData.MUSIC_ALBUM_PATH + " , "

                + DBData.MUSIC_YEARS + " ) ");

        // 创建播放队列表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.QUEUE_TABLE + " ("
                + DBData.QUEUE_QID + " varchar(20) PRIMARY KEY, "
                + DBData.MUSIC_ID + " , "
                + DBData.MUSIC_NAME + " , "
                + DBData.MUSIC_FILENAME + " , "

                + DBData.MUSIC_PATH + " , "
                + DBData.MUSIC_TIME + " , "
                + DBData.MUSIC_SIZE + " , "
                + DBData.MUSIC_ARTIST + " , "

                + DBData.MUSIC_ALBUM + " , "
                + DBData.MUSIC_ALBUM_ID + " , "
                + DBData.MUSIC_ALBUM_PATH + " , "

                + DBData.MUSIC_YEARS + " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DBData.PLAYLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBData.MUSIC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBData.MTP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBData.QUEUE_TABLE);
    }

}
