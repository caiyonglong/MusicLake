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
public class DBDaoImpl {
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
     * 新增音乐到歌单
     */

    public void insert(Music music) {
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
        values.put(DBData.MUSIC_ALBUM_PIC, music.getCoverUri());

        values.put(DBData.MUSIC_YEARS, music.getYear());
        db.insert(DBData.MUSIC_TABLENAME, null, values);
    }

    /**
     * 查询歌单中所有音乐信息
     * flag 0 升序 1 降序
     */
    private String[] order = {"ASC", "DESC"};

    public List<Music> queryPlaylist(String playlist_id) {
        Cursor cursor = null;
        List<Music> musicInfos = new ArrayList<>();
        // 查询歌单
        cursor = db.rawQuery("select * from " + DBData.MUSIC_TABLENAME + " where " + DBData.PLAYLIST_ID + " =? ",
                new String[]{playlist_id});

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(DBData.MUSIC_NAME)) != null) {
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
    public long createPlaylist(String playlist) {
        try {
            ContentValues values = new ContentValues();
            long playlistid = System.currentTimeMillis();
            // 开始组装第一条数据
            values.put(DBData.PLAYLIST_ID, playlistid);
            values.put(DBData.PLAYLIST_TITLE, playlist);
            long result = db.insert(DBData.PLAYLIST_TABLENAME, null, values);
            return playlistid;
        } catch (Exception e) {
            return -1;
        }

    }

    /**
     * 获取歌单名
     *
     * @return
     */

    public List<Playlist> getPlaylist() {
        List<Playlist> playlists = new ArrayList<>();

        // 查询music表中所有的数据
        Cursor cursor = db.query(DBData.PLAYLIST_TABLENAME, null, null, null, null, null, null);
        //再遍历游标cursor，获取数据库中的值
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(DBData.PLAYLIST_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBData.PLAYLIST_TITLE));

                Playlist playlist = new Playlist(id, name, 0);

                Cursor cursor1 = db.rawQuery("select * from " + DBData.MUSIC_TABLENAME + " where " + DBData.PLAYLIST_ID + " =? ",
                        new String[]{id});

                int count = cursor1.getCount();
                playlist.setCount(count);

                if (cursor1 != null) {
                    cursor1.close();
                }
                playlists.add(playlist);
            }
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
    public void deletePlaylist(String playlist_id) {
        db.delete(DBData.PLAYLIST_TABLENAME, DBData.PLAYLIST_ID + " = ?", new String[]{playlist_id});
        db.delete(DBData.MUSIC_TABLENAME, DBData.PLAYLIST_ID + " = ?", new String[]{playlist_id});
    }

    /**
     * 使用完数据库必须关闭
     */
    public void close() {
        db.close();
        db = null;
    }
}
