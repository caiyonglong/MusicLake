package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.source.db.DBDaoImpl;

import java.util.List;

import io.reactivex.Observable;

/**
 * 获取专辑所有歌曲
 */

public class AlbumSongLoader {

    public static Observable<List<Music>> getSongsForAlbumDB(final Context context, final Cursor cursor) {
        return Observable.create(subscriber -> {
            DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
            List<Music> results = dbDaoImpl.getSongsForCursor(cursor);
            dbDaoImpl.closeDB();
            subscriber.onNext(results);
            subscriber.onComplete();
        });
    }

    /**
     * 获取专辑所有歌曲
     *
     * @param context
     * @return
     */
    public static Observable<List<Music>> getSongsForAlbum(Context context, String albumName) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "SELECT * FROM music where music.is_online=0 and album = '" + albumName + "'";
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        return getSongsForAlbumDB(context, cursor);
    }
}
