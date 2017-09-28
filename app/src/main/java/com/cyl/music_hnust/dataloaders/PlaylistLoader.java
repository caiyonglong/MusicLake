package com.cyl.music_hnust.dataloaders;

import android.content.Context;
import android.widget.Toast;

import com.cyl.music_hnust.dataloaders.db.DBDaoImpl;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.Playlist;

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
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.newPlayList(name);
        dbDaoImpl.closeDB();
        return 1;
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
    public static void addToPlaylist(Context context, String pid, long mid) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.insertSong(pid, String.valueOf(mid));
        dbDaoImpl.closeDB();
    }


    public static void deletePlaylist(Context context, String playlist_id) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.deletePlaylist(playlist_id);
        dbDaoImpl.closeDB();
    }
}
