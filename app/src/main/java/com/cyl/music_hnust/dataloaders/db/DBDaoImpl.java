package com.cyl.music_hnust.dataloaders.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 永龙 on 2016/2/23.
 */
public class DBDaoImpl implements DBDao {
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
        db.delete(DBData.PLAYLIST_TABLE, DBData.PLAYLIST_ID + " = ?", new String[]{pId});
        db.delete(DBData.MTP_TABLE, DBData.PLAYLIST_ID + " = ?", new String[]{pId});
    }

    @Override
    public void newPlayList(String title) {
        try {
            ContentValues values = new ContentValues();
            long playlistid = System.currentTimeMillis();
            // 开始组装第一条数据
            values.put(DBData.PLAYLIST_ID, playlistid);
            values.put(DBData.PLAYLIST_NAME, title);
            db.insert(DBData.PLAYLIST_TABLE, null, values);
        } catch (Exception e) {
        }
    }

    @Override
    public void insertSong(String pId, String mId) {

    }

    @Override
    public void removeSong(String pId, String mId) {

    }

    @Override
    public void insertSongs(List<Music> songs) {
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
            db.insert(DBData.MUSIC_TABLE, null, values);
        }
    }

    @Override
    public void insertQueue(List<Music> songs) {

    }

    @Override
    public List<Playlist> getAllPlaylist() {
        List<Playlist> results = new ArrayList<>();

        // 查询music表中所有的数据
        Cursor cursor = db.query(DBData.PLAYLIST_TABLE
                , null, null, null, null, null, null);
        //再遍历游标cursor，获取数据库中的值
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                Playlist playlist = new MusicCursorWrapper(cursor).getPlaylist();

                Cursor cursor1 = db.rawQuery(
                        "select * from " + DBData.MTP_TABLE +
                                " where " + DBData.PLAYLIST_ID + " =? ",
                        new String[]{playlist.getId()});

                int count = cursor1.getCount();
                playlist.setCount(count);
                cursor1.close();

                results.add(playlist);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return results;
    }

    @Override
    public List<Music> getSongs(String pId) {
        Cursor cursor = null;
        List<Music> musicInfos = new ArrayList<>();
        // 查询歌单
        cursor = db.rawQuery("select * from music,playlist where music.mid=playlist.mid and playlist.pid= " + pId, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(DBData.MUSIC_NAME)) != null) {
                    Music music = new MusicCursorWrapper(cursor).getMusic();
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

    @Override
    public List<Music> getQueue() {
        return null;
    }

    @Override
    public void closeDB() {
        if (db!=null){
            db.close();
        }
    }
}
