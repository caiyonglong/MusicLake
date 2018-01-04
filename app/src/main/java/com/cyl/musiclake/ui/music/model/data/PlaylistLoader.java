package com.cyl.musiclake.ui.music.model.data;

import android.content.Context;

import com.cyl.musiclake.ui.music.model.data.db.DBDaoImpl;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.Playlist;

import java.util.List;

/**
 * 作者：yonglong on 2016/11/6 17:02
 */
public class PlaylistLoader {
    /**
     * 获取全部歌单
     *
     * @param context
     * @return
     */
    public static List<Playlist> getPlaylist(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        List<Playlist> results = dbDaoImpl.getAllPlaylist();
        dbDaoImpl.closeDB();
        return results;
    }

    /**
     * 新增歌单
     *
     * @param context
     * @param name
     * @return
     */
    public static long createPlaylist(Context context, String name) {
        long pid = -1;
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        pid = dbDaoImpl.newPlayList(name);
        dbDaoImpl.closeDB();
        return pid;
    }

    /**
     * 扫描歌单歌曲
     */
    public static List<Music> getMusicForPlaylist(Context context, String playlist_id) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        List<Music> results = dbDaoImpl.getSongs(playlist_id);
        dbDaoImpl.closeDB();
        return results;
    }

    /**
     * 添加歌曲到歌单
     */
    public static boolean addToPlaylist(Context context, String pid, long mid) {
        boolean result = false;
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        if (!dbDaoImpl.checkSongPlaylist(pid, String.valueOf(mid))) {
            dbDaoImpl.insertSong(pid, String.valueOf(mid));
            result = true;
        }
        dbDaoImpl.closeDB();
        return result;
    }

    /**
     * 移除歌曲到歌单
     */
    public static void removeSong(Context context, String pid, long mid) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.removeSong(pid, String.valueOf(mid));
        dbDaoImpl.closeDB();
    }

    /**
     * 删除歌单
     */
    public static void deletePlaylist(Context context, String playlist_id) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.deletePlaylist(playlist_id);
        dbDaoImpl.closeDB();
    }
}
