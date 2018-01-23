package com.cyl.musiclake.ui.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "Download";
    /**
     * 数据库版本号
     */
    private static final int DB_VERSION = 1;
    /**
     * 创建download_info表sql语句
     */
    private static final String CREATE_DOWNLOAD_INFO_TABLE = "create table download_info ("
            + "_id integer primary key autoincrement,"
            + "thread_id integer,"
            + "start_pos integer, "
            + "end_pos integer, "
            + "complete_size integer," + "url varchar)";
    /**
     * 创建download_file表sql语句 用来保存下载文件的状态信息
     */
    private static final String CREATE_DOWNLOAD_FILE_TABLE = "create table download_file ("
            + "_id integer primary key autoincrement,"
            + "mid varchar, "
            + "name varchar, "
            + "url varchar, "
            + "state integer, "
            + "complete_size integer,"
            + "file_size integer)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOWNLOAD_INFO_TABLE);
        db.execSQL(CREATE_DOWNLOAD_FILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
