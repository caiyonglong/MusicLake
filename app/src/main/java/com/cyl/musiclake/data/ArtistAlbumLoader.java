package com.cyl.musiclake.data;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.data.db.DBDaoImpl;

import java.util.List;

import io.reactivex.Observable;

/**
 * 获取歌手所有的专辑
 */

public class ArtistAlbumLoader {

    public static Observable<List<Album>> getAlbumsForArtist(final Context context, final String artistName) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
                String sql = "SELECT * FROM music WHERE music.artist='" + artistName + "' GROUP BY album";
                Cursor cursor = dbDaoImpl.makeCursor(sql);
                List<Album> results = dbDaoImpl.getAlbumsForCursor(cursor);
                dbDaoImpl.closeDB();
                subscriber.onNext(results);
                subscriber.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

}
