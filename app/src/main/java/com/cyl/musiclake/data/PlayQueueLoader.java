package com.cyl.musiclake.data;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.data.db.DBDaoImpl;
import com.cyl.musiclake.data.db.DBData;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/11/4 22:30
 */

public class PlayQueueLoader {

    private static String TAG = "PlayQueueLoader";

    /**
     * 添加歌曲到歌单
     */
    private static void addSongToQueue(Context context, List<Music> musics) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.insertSongs(musics);
        for (int i = 0; i < musics.size(); i++) {
            dbDaoImpl.insertSongToPlaylist(DBData.PLAY_QUEUE, musics.get(i).getId());
        }
        dbDaoImpl.closeDB();
    }
//    /**
//     * 添加歌曲到歌单
//     */
//    private static void resetQueue(Context context, List<Music> musics) {
//        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
//        dbDaoImpl.insertSongs(musics);
//        for (int i = 0; i < musics.size(); i++) {
//            dbDaoImpl.insertSongToPlaylist(DBData.PLAY_QUEUE, musics.get(i).getId());
//        }
//        dbDaoImpl.closeDB();
//    }

    /**
     * 获取播放队列
     */
    public static List<Music> getPlayQueue(Context context) {
//        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
//        String sql = "select * from music inner join music_playlist " +
//                "where music.mid = music_playlist.mid " +
//                "and music_playlist.pid=0 ORDER BY music_playlist.date_added DESC";
//        Cursor cursor = dbDaoImpl.makeCursor(sql);
//        List<Music> results = dbDaoImpl.getSongsForCursor(cursor);
//        dbDaoImpl.closeDB();
        List<Playlist> playlists = DataSupport.where("pid = ?", String.valueOf(Constants.PLAYLIST_QUEUE_ID)).find(Playlist.class, true);
        if (playlists.size() >= 1) return playlists.get(0).getMusicList();
        return new ArrayList<>();
    }


    /**
     * 添加歌曲到歌单
     */
    public static void updateQueue(Context context, List<Music> musics) {
        DataSupport.saveAll(musics);
        Playlist playlist = DataSupport.where("pid = ?", String.valueOf(Constants.PLAYLIST_QUEUE_ID)).findFirst(Playlist.class);
        playlist.setMusicList(musics);
        playlist.save();

//        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
//        dbDaoImpl.insertSongs(musics);
//        dbDaoImpl.clearPlayQueue();
//        for (int i = 0; i < musics.size(); i++) {
//            dbDaoImpl.insertSongToPlaylist(DBData.PLAY_QUEUE, musics.get(i).getId());
//        }
//        dbDaoImpl.closeDB();
    }

    /**
     * 清除歌单
     */
    public static void clearQueue(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.clearPlayQueue();
        dbDaoImpl.closeDB();
    }
}
