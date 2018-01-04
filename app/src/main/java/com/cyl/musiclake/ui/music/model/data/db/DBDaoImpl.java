package com.cyl.musiclake.ui.music.model.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.Playlist;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;
import static com.cyl.musiclake.ui.music.model.data.db.DBData.MTP_MID;
import static com.cyl.musiclake.ui.music.model.data.db.DBData.MTP_PID;
import static com.cyl.musiclake.ui.music.model.data.db.DBData.MTP_TABLE;
import static com.cyl.musiclake.ui.music.model.data.db.DBData.PLAYLIST_TABLE;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBDaoImpl implements DBDao {
    private static final String TAG = "DBDaoImpl";
    private DBHelper helper;
    private SQLiteDatabase db;

    /**
     * 创建和初始化数据库，使用完记得调用close方法关闭数据库
     *
     * @param context
     */
    public DBDaoImpl(Context context) {
        // TODO Auto-generated constructor stub
        helper = DBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 查询歌单中所有音乐信息
     * flag 0 升序 1 降序
     */
    private String[] order = {"ASC", "DESC"};


    /**
     * 删除歌单
     *
     * @param pId
     */
    @Override
    public void deletePlaylist(String pId) {
        db.delete(PLAYLIST_TABLE, DBData.PLAYLIST_ID + " = ?", new String[]{pId});
        db.delete(MTP_TABLE, DBData.PLAYLIST_ID + " = ?", new String[]{pId});
    }

    @Override
    public long newPlayList(String title) {
        long pid = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        // 开始组装第一条数据
        values.put(DBData.PLAYLIST_ID, pid);
        values.put(DBData.PLAYLIST_NAME, title);
        db.insertWithOnConflict(DBData.PLAYLIST_TABLE, null, values, CONFLICT_IGNORE);
        Log.d(TAG, "--newPlayList--" + pid);
        return pid;
    }

    @Override
    public void insertSong(String pId, String mId) {
        Log.d(TAG, "--insertSong--" + pId + "_--" + mId);
        ContentValues values = new ContentValues();
        values.put(MTP_PID, pId);
        values.put(MTP_MID, mId);
        db.insertWithOnConflict(DBData.MTP_TABLE, null, values, CONFLICT_IGNORE);
        getSongs(pId);
    }

    @Override
    public boolean checkSongPlaylist(String pId, String mId) {
        boolean result = false;
        Cursor cursor = db.query(MTP_TABLE
                , null
                , MTP_MID + " = ? and " + MTP_PID + " = ?"
                , new String[]{mId, pId}
                , null
                , null
                , null);

        if (cursor.getCount() > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public void removeSong(String pId, String mId) {
        db.delete(MTP_TABLE, MTP_PID + " = ? and " +
                MTP_MID + " = ? ", new String[]{pId, mId});
    }

    @Override
    public void insertSongs(List<Music> songs) {
        db.rawQuery("delete from " + DBData.MUSIC_TABLE, null);
        for (int i = 0; i < songs.size(); i++) {
            Music music = songs.get(i);
            ContentValues values = new ContentValues();
            values.put(DBData.MUSIC_ID, music.getId());

            values.put(DBData.MUSIC_NAME, music.getTitle());
            values.put(DBData.MUSIC_ARTIST, music.getArtist());

            values.put(DBData.MUSIC_PATH, music.getUri());
            values.put(DBData.MUSIC_FILENAME, music.getFileName());

            values.put(DBData.MUSIC_TIME, music.getDuration());
            values.put(DBData.MUSIC_SIZE, music.getFileSize());

            values.put(DBData.MUSIC_ALBUM, music.getAlbum());
            values.put(DBData.MUSIC_ALBUM_ID, music.getAlbumId());
            values.put(DBData.MUSIC_ALBUM_PATH, music.getCoverUri());

            values.put(DBData.MUSIC_YEARS, music.getYear());
            db.insertWithOnConflict(DBData.MUSIC_TABLE, null, values, CONFLICT_IGNORE);
        }
    }

    @Override
    public void updateQueue(List<Music> songs) {
        db.rawQuery("delete from " + DBData.QUEUE_TABLE, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < songs.size(); i++) {
            Music music = songs.get(i);
            values.clear();
            values.put(DBData.MUSIC_ID, music.getId());

            values.put(DBData.MUSIC_NAME, music.getTitle());
            values.put(DBData.MUSIC_ARTIST, music.getArtist());

            values.put(DBData.MUSIC_PATH, music.getUri());
            values.put(DBData.MUSIC_FILENAME, music.getFileName());

            values.put(DBData.MUSIC_TIME, music.getDuration());
            values.put(DBData.MUSIC_SIZE, music.getFileSize());

            values.put(DBData.MUSIC_ALBUM, music.getAlbum());
            values.put(DBData.MUSIC_ALBUM_ID, music.getAlbumId());
            values.put(DBData.MUSIC_ALBUM_PATH, music.getCoverUri());

            values.put(DBData.MUSIC_YEARS, music.getYear());

            db.insert(DBData.QUEUE_TABLE, null, values);
        }
    }


    @Override
    public List<Playlist> getAllPlaylist() {
        List<Playlist> results = new ArrayList<>();
        String sql = "SELECT a.pid, a.pName, " +
                "(SELECT Count(b.mid) " +
                "FROM musicToPlaylist b " +
                " WHERE b.pid = a.pid) AS num FROM playlist a";

//        String sql = "select playlist.pid,playlist.pName,count(musicToPlaylist.mid) as num " +
//                "from playlist , musicToPlaylist where playlist.pid = musicToPlaylist.pid group by playlist.pid";
//        Cursor cursor = db.query(PLAYLIST_TABLE + "as a"
//                , new String[]{"a." + PLAYLIST_ID,
//                        "a." + PLAYLIST_NAME,
//                        "select count( b." + MTP_MID + ") as num"}
//                , PLAYLIST_TABLE + "." + PLAYLIST_ID + " = " + MTP_TABLE + "." + MTP_PID
//                , null
//                , PLAYLIST_TABLE + "." + PLAYLIST_ID
//                , null
//                , null);
        Log.d(TAG, sql + "----");
        Cursor cursor = db.rawQuery(sql, null);
        //再遍历游标cursor，获取数据库中的值
        Log.d(TAG, cursor.getCount() + "----");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Playlist playlist = new MusicCursorWrapper(cursor).getPlaylist();
                Log.d(TAG, playlist.toString() + "----");
                results.add(playlist);
            }
        }
        // 记得关闭游标
        cursor.close();
        return results;
    }

    @Override
    public List<Music> getSongs(String pId) {
        List<Music> results = new ArrayList<>();
        // 查询歌单
        String sql = "select * from music,musicToPlaylist where music.mid = musicToPlaylist.mid and musicToPlaylist.pid=" + pId;
        Cursor cursor = db.rawQuery(sql, null);

        Log.d(TAG, cursor.getCount() + "----" + sql);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Music music = new MusicCursorWrapper(cursor).getMusic();
                Log.d(TAG, music.toString() + "----");
                results.add(music);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return results;
    }

    @Override
    public List<Music> getQueue() {
        List<Music> results = new ArrayList<>();
        // 查询歌单
        Cursor cursor = db.rawQuery("select * from playQueue", null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(DBData.MUSIC_NAME)) != null) {
                    Music music = new MusicCursorWrapper(cursor).getMusic();
                    results.add(music);
                }
            }
        }

        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return results;
    }

    @Override
    public void clearQueue() {
        db.rawQuery("delete from " + DBData.QUEUE_TABLE, null);
    }


    @Override
    public void closeDB() {
//        if (db != null) {
//            db.close();
//        }
    }
}
