package com.cyl.musiclake.data.source.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;
import static com.cyl.musiclake.data.source.db.DBData.MTP_DATE;
import static com.cyl.musiclake.data.source.db.DBData.MTP_MID;
import static com.cyl.musiclake.data.source.db.DBData.MTP_PID;
import static com.cyl.musiclake.data.source.db.DBData.MTP_TABLE;
import static com.cyl.musiclake.data.source.db.DBData.PLAYLIST_TABLE;

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
        db.delete(MTP_TABLE, DBData.MTP_PID + " = ?", new String[]{pId});
    }

    /**
     * 清空播放队列
     * 播放队列 pid=0
     */
    public void clearPlayQueue() {
        db.delete(MTP_TABLE, DBData.MTP_PID + " = ?", new String[]{DBData.PLAY_QUEUE});
    }

    /**
     * 清空播放历史
     * 播放历史 pid=1
     */
    public void clearHistory() {
        db.delete(MTP_TABLE, DBData.MTP_PID + " = ?", new String[]{DBData.HISTORY});
    }


    /**
     * 新建歌单
     *
     * @param title
     * @return
     */
    @Override
    public long addPlayList(String title) {
//        db.beginTransaction();
        long pid = System.currentTimeMillis();
//        try {
        ContentValues values = new ContentValues();
        // 开始组装第一条数据
        values.put(DBData.PLAYLIST_ID, pid);
        values.put(DBData.PLAYLIST_NAME, title);
        values.put(DBData.PLAYLIST_DATE, System.currentTimeMillis());
        values.put(DBData.PLAYLIST_ORDER, DBData.PLAYLIST_NAME);
        db.insertWithOnConflict(DBData.PLAYLIST_TABLE, null, values, CONFLICT_IGNORE);
        LogUtil.e(TAG, "--newPlayList--" + pid);
//        db.setTransactionSuccessful();
//        } catch (Exception e) {
//            pid = -1;
//        } finally {
//            db.endTransaction();
//        }
        return pid;
    }

    /**
     * 插入歌曲到歌单(加入队列，加入历史)
     *
     * @param pId
     * @param mId
     */
    @Override
    public void insertSongToPlaylist(String pId, String mId) {
        LogUtil.d(TAG, "--insertSong--" + pId + "_--" + mId);
        ContentValues values = new ContentValues();
        values.put(MTP_PID, pId);
        values.put(MTP_MID, mId);
        values.put(MTP_DATE, System.currentTimeMillis());
        db.insertWithOnConflict(DBData.MTP_TABLE, null, values, CONFLICT_IGNORE);
    }

    /**
     * 判断歌单中是否存在mid音乐
     *
     * @param pId
     * @param mId
     * @return
     */
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

    /**
     * 从歌单(队列，历史)中移除
     *
     * @param pId
     * @param mId
     */
    @Override
    public void removeSong(String pId, String mId) {
        db.delete(MTP_TABLE, MTP_PID + " = ? and " +
                MTP_MID + " = ? ", new String[]{pId, mId});
    }

    /**
     * 音乐表单中新增音乐
     *
     * @param songs
     */
    @Override
    public void insertSongs(List<Music> songs) {
        for (int i = 0; i < songs.size(); i++) {
            insertSong(songs.get(i));
        }
    }

    /**
     * 音乐表单中新增音乐
     */
    public void insertSong(Music music) {
        ContentValues values = getMusicContentValues(music);
        LogUtil.e(TAG, music.toString() + "---");
        db.insertWithOnConflict(DBData.MUSIC_TABLE, null, values, CONFLICT_IGNORE);
    }

    /**
     * 删除音乐表单中数据
     */
    @Override
    public void deleteSongForId(String mid) {
        db.delete(DBData.MUSIC_TABLE, DBData.MUSIC_ID + " = ?", new String[]{mid});
    }

    /**
     * 生成游标
     *
     * @param sql
     * @return
     */
    @Override
    public Cursor makeCursor(String sql) {
        return db.rawQuery(sql, null);
    }

    /**
     * 查询歌曲游标
     *
     * @return
     */
    public Cursor makeSongCursor(String selection, String[] paramArrayOfString, String sortOrder) {
        return db.query(DBData.MUSIC_TABLE, null, selection, paramArrayOfString, null, null, sortOrder);
    }


    @Override
    public List<Playlist> getAllPlaylistForCursor(Cursor cursor) {
        List<Playlist> results = new ArrayList<>();
        //再遍历游标cursor，获取数据库中的值
        LogUtil.d(TAG, cursor.getCount() + "----");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Playlist playlist = new MusicCursorWrapper(cursor).getPlaylist();
                LogUtil.d(TAG, playlist.toString() + "----");
                results.add(playlist);
            }
        }
        // 记得关闭游标
        cursor.close();
        return results;
    }

    @Override
    public List<Music> getSongsForCursor(Cursor cursor) {
        List<Music> results = new ArrayList<>();
        // 查询歌单
//        String sql = "select * from music,musicToPlaylist where music.mid = musicToPlaylist.mid and musicToPlaylist.pid=" + pId;
//        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Music music = new MusicCursorWrapper(cursor).getMusic();
                LogUtil.d(TAG, "getSongsForCursor:" + music.toString());
                results.add(music);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return results;
    }

    /**
     * 更新music
     *
     * @param music
     */
    @Override
    public void updateSong(Music music) {
//        db.beginTransaction();
//        try {
        String mid = music.getId();
        ContentValues values = getMusicContentValues(music);
        db.update(DBData.MUSIC_TABLE, values, DBData.MUSIC_ID + " = ?",
                new String[]{mid});
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
    }

    @Override
    public void closeDB() {
//        if (db != null) {
//            db.close();
//        }
    }


    private ContentValues getMusicContentValues(Music music) {
        ContentValues values = new ContentValues();
        values.put(DBData.MUSIC_ID, music.getId());
        values.put(DBData.MUSIC_NAME, music.getTitle());
        values.put(DBData.MUSIC_ARTIST, music.getArtist());
        values.put(DBData.MUSIC_ARTIST_ID, music.getArtist());
        values.put(DBData.MUSIC_ALBUM, music.getAlbum());
        values.put(DBData.MUSIC_ALBUM_ID, music.getAlbumId());

        values.put(DBData.MUSIC_PATH, music.getUri());
        values.put(DBData.MUSIC_FILENAME, music.getFileName());
        values.put(DBData.MUSIC_TIME, music.getDuration());
        values.put(DBData.MUSIC_SIZE, music.getFileSize());
        values.put(DBData.MUSIC_YEARS, music.getYear());

        values.put(DBData.IS_LOVE, music.isLove());
        values.put(DBData.IS_ONLINE, music.isOnline());
        values.put(DBData.MUSIC_PREFIX, music.getPrefix());
        values.put(DBData.MUSIC_TYPE, String.valueOf(music.getType()));
        values.put(DBData.MUSIC_COVER, music.getCoverUri());
        values.put(DBData.MUSIC_COVER_BIG, music.getCoverBig());
        values.put(DBData.MUSIC_COVER_SMALL, music.getCoverSmall());
        return values;
    }
}
