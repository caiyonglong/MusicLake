package com.cyl.music_hnust.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyl.music_hnust.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    /**
     * 创建和初始化数据库，使用完记得调用close方法关闭数据库
     *
     * @param context
     */
    public DBDao(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 新增音乐到歌单
     *
     */

    public void add(String playlist,Music music) {
        ContentValues values = new ContentValues();
        values.put(DBData.PLAYLIST_TITLE, playlist);
        values.put(DBData.MUSIC_ID, music.getId());
        values.put(DBData.MUSIC_NAME, music.getTitle());
        values.put(DBData.MUSIC_PATH, music.getUri());
        values.put(DBData.MUSIC_FILENAME, music.getFileName());
        values.put(DBData.MUSIC_TIME, music.getDuration());
        values.put(DBData.MUSIC_SIZE, music.getFileSize());
        values.put(DBData.MUSIC_ARTIST, music.getArtist());
        values.put(DBData.MUSIC_ALBUM, music.getAlbum());
        values.put(DBData.MUSIC_YEARS, music.getYear());
        values.put(DBData.MUSIC_ALBUM_PIC, music.getCoverUri());

        db.insert(DBData.PLAYLIST_TABLENAME, null,
                values);
    }

    /**
     * 查询歌单中所有音乐信息
     * flag 0 升序 1 降序
     */
    private String[] order={"ASC","DESC"};
    public List<Music> queryPlaylist(String title,String orderBy,int flag) {
        List<Music> musicInfos = new ArrayList<>();
        Cursor cursor = null;

        // 查询歌单
        cursor = db.rawQuery("SELECT * FROM " + DBData.PLAYLIST_TABLENAME
                +" where "+DBData.PLAYLIST_TITLE +" = " +title+" order by "+ orderBy +"  "+order[flag],
                null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(DBData.MUSIC_NAME))!=null) {
                    Music music = new Music();
                    music.setId(cursor.getInt(cursor.getColumnIndex(DBData.MUSIC_ID)));
                    music.setTitle(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_NAME)));
                    music.setArtist(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_ARTIST)));
                    music.setAlbum(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_ALBUM)));
                    music.setDuration(cursor.getLong(cursor.getColumnIndex(DBData.MUSIC_TIME)));
                    music.setUri(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_PATH)));
                    music.setCoverUri(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_ALBUM_PIC)));
                    music.setFileName(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_FILENAME)));
                    music.setFileSize(cursor.getLong(cursor.getColumnIndex(DBData.MUSIC_SIZE)));
                    music.setYear(cursor.getString(cursor.getColumnIndex(DBData.MUSIC_YEARS)));

                    musicInfos.add(music);
                }
            }
        }

        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }

        return musicInfos;
    }


    /**
     * 新建歌单
     *
     * @return
     */
    public boolean newPlaylist(String title) {
        String sql = "select * from "
                + DBData.PLAYLIST_TABLENAME + " where "
                + DBData.PLAYLIST_TITLE + " = " + title;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            try {
                ContentValues values = new ContentValues();
                values.put(DBData.PLAYLIST_TITLE, title);
                db.insert(DBData.PLAYLIST_TABLENAME, null, values);
                return true;
            } catch (Exception e) {

                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取歌单名
     *
     * @return
     */

    public List<String> getPlaylist() {
        List<String> playlists = new ArrayList<>();
        String sql = "select distinct " + DBData.PLAYLIST_TITLE + " from " + DBData.PLAYLIST_TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBData.PLAYLIST_TITLE));
            playlists.add(title);
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return playlists;
    }

    /**
     * 删除歌单 / 移除歌曲
     *
     * @return
     */
    public int deletePlaylist(String playlist, int music_id) {

        if (music_id == -1) {
            int result = db.delete(DBData.PLAYLIST_TABLENAME, DBData.PLAYLIST_TITLE + " = ? "
                    , new String[]{playlist});
            return result;
        } else {
            int result = db.delete(DBData.PLAYLIST_TABLENAME, DBData.MUSIC_ID + "='"
                    + music_id + "'", null);
            return result;
        }
    }

    /**
     * 使用完数据库必须关闭
     */
    public void close() {
        db.close();
        db = null;
    }
}
