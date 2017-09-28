package com.cyl.music_hnust.dataloaders.db;

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
    private static int MUSIC_DB_VERSION = 1;

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
        // 创建歌单表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.PLAYLIST_TABLENAME + " ("
                + DBData.PLAYLIST_ID + " PRIMARY KEY, "
                + DBData.PLAYLIST_TITLE + " ) "
        );
        // 创建歌单表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.PLAYLIST_TABLENAME + " ("
                + DBData.PLAYLIST_ID + " PRIMARY KEY, "
                + DBData.MUSIC_ID + " ) "
        );
        // 创建歌曲表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.MUSIC_TABLENAME + " ("
                + DBData.MUSIC_ID + " VARCHAR(100) PRIMARY KEY, "
                + DBData.MUSIC_NAME + " VARCHAR(100), "
                + DBData.MUSIC_FILENAME + " VARCHAR(100), "

                + DBData.MUSIC_PATH + " VARCHAR(300), "
                + DBData.MUSIC_TIME + " VARCHAR(100), "
                + DBData.MUSIC_SIZE + " VARCHAR(100), "
                + DBData.MUSIC_ARTIST + " VARCHAR(100), "

                + DBData.MUSIC_ALBUM + " VARCHAR(100), "
                + DBData.MUSIC_ALBUM_ID + " VARCHAR(100), "
                + DBData.MUSIC_ALBUM_PIC + " VARCHAR(100), "

                + DBData.MUSIC_YEARS + " VARCHAR(100)) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DBData.PLAYLIST_TABLENAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBData.MUSIC_TABLENAME);
    }

}
