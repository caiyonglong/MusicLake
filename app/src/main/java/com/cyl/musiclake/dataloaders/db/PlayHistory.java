package com.cyl.musiclake.dataloaders.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 永龙 on 2016/2/23.
 * 版本: 2016-8-12  v2.5
 */
public class PlayHistory extends SQLiteOpenHelper {

    public static PlayHistory mInstance = null;

    private static String MUSIC_DB_NAME = "hkmusic.db";
    private static int MUSIC_DB_VERSION = 2;


    /**
     * 创建数据库
     *
     * @param context 上下文
     */
    private PlayHistory(Context context) {
        super(context, MUSIC_DB_NAME, null, MUSIC_DB_VERSION);
    }

    public static PlayHistory getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PlayHistory.class) {
                if (mInstance == null) {
                    mInstance = new PlayHistory(context);
                }
            }
        }
        return mInstance;
    }


    // TODO CREATE TABLE
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建播放队列表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.QUEUE_TABLE + " ("
                + DBData.QUEUE_QID + " PRIMARY KEY, "
                + DBData.QUEUE_TITLE + " , "
                + DBData.QUEUE_ALBUM + " , "
                + DBData.QUEUE_ARTIST + " , "
                + DBData.QUEUE_TYPE + " , "
                + DBData.QUEUE_URL + " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DBData.QUEUE_TABLE);
    }

}
