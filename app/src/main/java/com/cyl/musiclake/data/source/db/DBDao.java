package com.cyl.musiclake.data.source.db;

import android.database.Cursor;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;

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
    long newPlayList(String title);

    /**
     * 加入歌单
     *
     * @param pId
     * @param mId
     */
    void insertSong(String pId, String mId);

    /**
     * 从歌单中移除
     *
     * @param pId
     * @param mId
     */
    boolean checkSongPlaylist(String pId, String mId);

    /**
     * 从歌单中移除
     *
     * @param pId
     * @param mId
     */
    void removeSong(String pId, String mId);

    /**
     * 插入音乐
     *
     * @param songs
     */
    void insertSongs(List<Music> songs);

    /**
     * 第一次进入清空本地音乐
     */
    void clearSongs();


    /**
     * 获取所有歌单
     *
     * @return
     */
    List<Playlist> getAllPlaylist();

    /**
     * 获取歌单中所有音乐
     *
     * @param pId
     * @return
     */
    List<Music> getSongs(String pId);

    /**
     * 获取播放队列
     *
     * @return
     */
    List<Music> getQueue();

    /**
     * 更新播放队列
     *
     * @param songs
     */
    void updateQueue(List<Music> songs);

    void clearQueue();


    /**
     * 获取收藏歌曲
     *
     */
    List<Music> getAllLoves(Cursor cursor);

    /**
     * 收藏歌曲
     *
     * @param mid
     */
    void addToLove(String[] mid);

    /**
     * 取消收藏歌曲
     *
     * @param mid
     */
    void disableLove(String[] mid);

    /**
     * 保存播放历史
     *
     * @param mid
     */
    void saveHistory(String mid);

    /**
     * 清空播放历史
     */
    void clearHistory();

    void closeDB();
}
