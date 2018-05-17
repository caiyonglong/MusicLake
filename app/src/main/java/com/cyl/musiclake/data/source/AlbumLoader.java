package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.data.source.db.DBDaoImpl;

import java.util.List;

import io.reactivex.Observable;

/**
 * 获取所有专辑
 */

public class AlbumLoader {

    private static Observable<List<Album>> getAlbumFormDB(Context context, Cursor cursor) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
                List<Album> results = dbDaoImpl.getAlbumsForCursor(cursor);
                subscriber.onNext(results);
                subscriber.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    /**
     * 获取所有专辑
     *
     * @param context
     * @return
     */
    public static Observable<List<Album>> getAllAlbums(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "SELECT music.album_id,music.album,music.artist_id,music.artist,count(music.name) as num FROM music WHERE music.is_online=0 GROUP BY music.album";
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        return getAlbumFormDB(context, cursor);
    }
}
