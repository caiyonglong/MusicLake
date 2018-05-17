package com.cyl.musiclake.data;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.data.db.DBDaoImpl;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by hefuyi on 2016/11/4.
 */

public class ArtistLoader {

    private static Observable<List<Artist>> getArtistsForDB(Context context, Cursor cursor) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
                List<Artist> results = dbDaoImpl.getArtistsForCursor(cursor);
                dbDaoImpl.closeDB();
                subscriber.onNext(results);
                subscriber.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    /**
     * 获取所有艺术家
     *
     * @param context
     * @return
     */
    public static Observable<List<Artist>> getAllArtists(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "SELECT music.artist_id,music.artist,count(music.name) as num FROM music where music.is_online=0 GROUP BY music.artist";
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        return getArtistsForDB(context, cursor);
    }
}
