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
     * @param b
     * @return
     */
    public static List<Playlist> getPlaylists(Context context, boolean b) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        return dbDaoImpl.getPlaylist();
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
        long result = dbDaoImpl.createPlaylist(name);
        dbDaoImpl.close();
        return result;
    }

    /**
     * 扫描歌单歌曲
     */
    public static List<Music> getMusicForPlaylist(Context context, String playlist_id) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        List<Music> results = dbDaoImpl.queryPlaylist(playlist_id);
        dbDaoImpl.close();
        return results;
    }

    /**
     * 添加歌曲到歌单
     */
    public static void addToPlaylist(Context context, String playlist_id, int music_id) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);

//        if (result == -1) {
//            Toast.makeText(context, "添加失败，歌曲已存在!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
//        }
        dbDaoImpl.close();
    }


    public static void deletePlaylist(Context context, String playlist_id) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.deletePlaylist(playlist_id);
        dbDaoImpl.close();

//        if (result == -1) {
//        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
//        }else {
        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
//        }
    }
}
