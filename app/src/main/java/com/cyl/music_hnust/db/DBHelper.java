package com.cyl.music_hnust.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 永龙 on 2016/2/23.
 * 版本: 2016-8-12  v2.5 只建一个歌单表，存放歌单歌曲信息
 *
 */
public class DBHelper extends SQLiteOpenHelper {
    /**
     * 创建数据库
     *
     * @param context
     *            上下文
     */
    public DBHelper(Context context) {
        super(context, DBData.MUSIC_DB_NAME, null, DBData.MUSIC_DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // 创建歌单表
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DBData.PLAYLIST_TABLENAME + " ("
                + DBData.PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBData.PLAYLIST_TITLE + " VARCHAR(100), "
                + DBData.MUSIC_ID + " VARCHAR(100), "
                + DBData.MUSIC_NAME + " VARCHAR(100), "
                + DBData.MUSIC_FILENAME + " VARCHAR(100), "
                + DBData.MUSIC_PATH + " VARCHAR(300), "
                + DBData.MUSIC_TIME + " VARCHAR(100), "
                + DBData.MUSIC_SIZE + " VARCHAR(100), "
                + DBData.MUSIC_ARTIST + " VARCHAR(100), "
                + DBData.MUSIC_ALBUM + " VARCHAR(100), "
                + DBData.MUSIC_ALBUM_PIC + " VARCHAR(100), "
                + DBData.MUSIC_YEARS + " VARCHAR(100)) "
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DBData.PLAYLIST_TABLENAME);
    }

}
