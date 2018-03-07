package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.source.db.DBDaoImpl;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by hefuyi on 2016/11/4.
 */

public class ArtistSongLoader {

    public static Observable<List<Music>> getSongsForArtistDB(final Context context, final Cursor cursor) {
        return Observable.create(subscriber -> {
            DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
            List<Music> results = dbDaoImpl.getSongsForCursor(cursor);
            dbDaoImpl.closeDB();
            subscriber.onNext(results);
            subscriber.onComplete();
        });
    }

    /**
     * 获取艺术家所有歌曲
     *
     * @param context
     * @return
     */
    public static Observable<List<Music>> getSongsForArtist(Context context, String artistName) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "SELECT * FROM music where music.is_online=0 and artist = '" + artistName + "'";
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        return getSongsForArtistDB(context, cursor);
    }

}
