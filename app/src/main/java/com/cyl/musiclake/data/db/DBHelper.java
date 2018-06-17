//package com.cyl.musiclake.data.db;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.cyl.musiclake.data.download.TasksManagerDBController;
//import com.cyl.musiclake.data.download.TasksManagerModel;
//
///**
// * Created by 永龙 on 2016/2/23.
// * 版本: 2016-8-12  v2.5
// */
//public class DBHelper extends SQLiteOpenHelper {
//
//    private static DBHelper mInstance = null;
//
//    private static String MUSIC_DB_NAME = "musicLake.db";
//    private static int MUSIC_DB_VERSION = 2;
//
//    /**
//     * 创建数据库
//     *
//     * @param context 上下文
//     */
//    private DBHelper(Context context) {
//        super(context, MUSIC_DB_NAME, null, MUSIC_DB_VERSION);
//    }
//
//    public static DBHelper getInstance(Context context) {
//        if (mInstance == null) {
//            synchronized (DBHelper.class) {
//                if (mInstance == null) {
//                    mInstance = new DBHelper(context);
//                }
//            }
//        }
//        return mInstance;
//    }
//
//
//    // TODO CREATE TABLE
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // 创建歌单歌曲表
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//                + DBData.MTP_TABLE + " ("
//                + DBData.DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + DBData.MTP_MID + " varchar(20)  , "
//                + DBData.MTP_PID + "  varchar(50) , "
//                + DBData.MTP_DATE + "  ) ");
//        // 创建歌单表
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//                + DBData.PLAYLIST_TABLE + " ("
//                + DBData.PLAYLIST_ID + " varchar(50) PRIMARY KEY, "
//                + DBData.PLAYLIST_NAME + " ,"
//                + DBData.PLAYLIST_ORDER + " , "
//                + DBData.PLAYLIST_COVER + " , "
//                + DBData.PLAYLIST_DATE + " ) ");
//        // 创建歌曲表
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//                + DBData.MUSIC_TABLE + " ("
//                + DBData.MUSIC_ID + " varchar(20) PRIMARY KEY, "
//                + DBData.MUSIC_NAME + " , "
//
//                + DBData.MUSIC_FILENAME + " , "
//                + DBData.MUSIC_PATH + " , "
//                + DBData.MUSIC_LRC_PATH + " , "
//                + DBData.MUSIC_TIME + " , "
//                + DBData.MUSIC_SIZE + " , "
//
//                + DBData.MUSIC_ARTIST_ID + " , "
//                + DBData.MUSIC_ARTIST + " , "
//                + DBData.MUSIC_ALBUM + " , "
//                + DBData.MUSIC_ALBUM_ID + " , "
//
//                + DBData.MUSIC_COVER + " , "
//                + DBData.MUSIC_COVER_BIG + " , "
//                + DBData.MUSIC_COVER_SMALL + " , "
//                + DBData.MUSIC_TYPE + " , "
//
//                + DBData.IS_LOVE + " , "
//                + DBData.IS_ONLINE + " , "
//                + DBData.MUSIC_PREFIX + " , "
//
//                + DBData.MUSIC_YEARS + " ) ");
//        //创建下载文件数据库
//        db.execSQL("CREATE TABLE IF NOT EXISTS "
//                + TasksManagerDBController.TABLE_NAME
//                + String.format(
//                "("
//                        + "%s INTEGER PRIMARY KEY, " // id, download id
//                        + "%s VARCHAR, " // mid
//                        + "%s VARCHAR, " // name
//                        + "%s VARCHAR, " // url
//                        + "%s VARCHAR, " // path
//                        + "%s INTEGER " // finish
//                        + ")"
//                , TasksManagerModel.ID
//                , TasksManagerModel.MID
//                , TasksManagerModel.NAME
//                , TasksManagerModel.URL
//                , TasksManagerModel.PATH
//                , TasksManagerModel.FINISH
//
//        ));
//
//        //默认新建播放队列和最近播放
//        db.execSQL("insert into " + DBData.PLAYLIST_TABLE + " values ( " + DBData.PLAY_QUEUE + " ,'播放队列', '" + DBData.PLAYLIST_NAME + "',null,null)");
//        db.execSQL("insert into " + DBData.PLAYLIST_TABLE + " values ( " + DBData.HISTORY + " ,'最近播放', '" + DBData.PLAYLIST_NAME + "' ,null,null)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // TODO Auto-generated method stub
//        db.execSQL("DROP TABLE IF EXISTS " + DBData.PLAYLIST_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + DBData.MUSIC_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + DBData.MTP_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + TasksManagerDBController.TABLE_NAME);
//    }
//
//}
