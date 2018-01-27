package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.data.source.db.DBDaoImpl;
import com.cyl.musiclake.data.source.db.DBData;
import com.cyl.musiclake.utils.LogUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * 作者：yonglong on 2016/11/6 17:02
 */
public class PlaylistLoader {
    private static final String TAG = "PlaylistLoader";

    /**
     * 获取全部歌单
     *
     * @param context
     * @return
     */
    public static List<Playlist> getPlaylist(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "SELECT *" +
                " , " + "(SELECT count(" +
                DBData.MTP_TABLE + ".mid ) FROM " +
                DBData.MTP_TABLE + " WHERE " +
                DBData.MTP_TABLE + ".pid = " +
                DBData.PLAYLIST_TABLE + ".pid) AS num FROM " +
                DBData.PLAYLIST_TABLE + " where " +
                DBData.PLAYLIST_TABLE + "." + DBData.PLAYLIST_DATE + " is not null ";
        LogUtil.d(TAG, sql + "----");
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        List<Playlist> results = dbDaoImpl.getAllPlaylistForCursor(cursor);
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
        pid = dbDaoImpl.addPlayList(name);
        dbDaoImpl.closeDB();
        return pid;
    }

    /**
     //     * 扫描歌单歌曲
     //     */
//    public static List<Music> getMusicForPlaylist(Context context, String playlist_id) {
//        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
//        List<Music> results = dbDaoImpl.getSongs(playlist_id);
//        dbDaoImpl.closeDB();
//        return results;
//    }

    /**
     * 扫描歌单歌曲
     */
    public static Observable<List<Music>> getMusicForPlaylist(Context context, String playlist_id) {
        return Observable.create(subscriber -> {
            String sql = "select * from "
                    + DBData.MUSIC_TABLE + " , "
                    + DBData.MTP_TABLE + " where "
                    + DBData.MUSIC_TABLE + ".mid = "
                    + DBData.MTP_TABLE + ".mid " + "and "
                    + DBData.MTP_TABLE + ".pid=" + playlist_id + " ORDER BY music_playlist.date_added DESC";

            DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
            if (playlist_id.equals("1")) {
                sql = "SELECT DISTINCT  music.mid,  music.name,  music.filename,  music.path,  music.lrc_path,  music.duration,  music.size,  music.artist_id,  music.artist,  music.album,  music.album_id,  music.cover,  music.coverBig,  music.coverSmall,  music.type,  music.is_love,  music.is_online,  music.prefix,  music.years,  (   SELECT    music_playlist.date_added   FROM    music_playlist   WHERE    music_playlist.pid = 0   AND music_playlist.mid = music.mid   ORDER BY    music_playlist.date_added DESC  ) AS time,  (   SELECT    Count(music_playlist.mid)   FROM    music_playlist   WHERE    music_playlist.pid = 0   AND music_playlist.mid = music.mid  ) AS num FROM  music_playlist,  music WHERE  music_playlist.mid = music.mid AND music_playlist.pid = 1 ORDER BY  time DESC";
            } else if (playlist_id.equals("0")) {
                sql = "SELECT DISTINCT  music.mid,  music.name,  music.filename,  music.path,  music.lrc_path,  music.duration,  music.size,  music.artist_id,  music.artist,  music.album,  music.album_id,  music.cover,  music.coverBig,  music.coverSmall,  music.type,  music.is_love,  music.is_online,  music.prefix,  music.years,  (   SELECT    music_playlist.date_added   FROM    music_playlist   WHERE    music_playlist.pid = 0   AND music_playlist.mid = music.mid   ORDER BY    music_playlist.date_added DESC  ) AS time,  (   SELECT    Count(music_playlist.mid)   FROM    music_playlist   WHERE    music_playlist.pid = 0   AND music_playlist.mid = music.mid  ) AS num FROM  music_playlist,  music WHERE  music_playlist.mid = music.mid AND music_playlist.pid = 0 ORDER BY  time DESC";
            }
            Cursor cursor = dbDaoImpl.makeCursor(sql);
            List<Music> results = dbDaoImpl.getSongsForCursor(cursor);
            dbDaoImpl.closeDB();
            subscriber.onNext(results);
            subscriber.onComplete();
        });
    }

    /**
     * 添加歌曲到歌单
     */
    public static boolean addToPlaylist(Context context, String pid, String mid) {
        boolean result = false;
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        if (!dbDaoImpl.checkSongPlaylist(pid, mid)) {
            dbDaoImpl.insertSongToPlaylist(pid, mid);
            result = true;
        }
        dbDaoImpl.closeDB();
        return result;
    }

    /**
     * 移除歌曲到歌单
     */
    public static void removeSong(Context context, String pid, String mid) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.removeSong(pid, mid);
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
