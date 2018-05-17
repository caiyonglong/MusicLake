package com.cyl.musiclake.data.db;

import android.database.Cursor;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

/**
 * Created by D22434 on 2017/9/28.
 */

public interface DBDao {

    /**
     * 删除歌单
     *
     * @param pId
     */
    void deletePlaylist(String pId);

    /**
     * 新建歌单
     *
     * @param title
     */
    long addPlayList(String title);

    /**
     * 加入歌单
     *
     * @param pId
     * @param mId
     */
    void insertSongToPlaylist(String pId, String mId);

    /**
     * 从歌单中移除
     *
     * @param pId
     * @param mId
     */
    boolean hasSongPlaylist(String pId, String mId);

    /**
     * 从歌单中移除
     *
     * @param pId
     * @param mId
     */
    void removeSongPlaylist(String pId, String mId);

    /**
     * 插入音乐
     *
     * @param songs
     */
    void insertSongs(List<Music> songs);

    /**
     * 获取所有歌单
     *
     * @return
     */
    List<Playlist> getAllPlaylistForCursor(Cursor cursor);

    /**
     * 获取歌单中音乐
     *
     * @return
     */
    List<Music> getSongsForCursor(Cursor cursor);

    /**
     * 更新歌曲
     */
    void updateSong(Music music);

    /**
     * 第一次进入清空本地音乐
     */
    void deleteSongForId(String mid);

    /**
     * 生成游标
     *
     * @param sql sql语句
     * @return
     */
    Cursor makeCursor(String sql);


    void closeDB();
}
