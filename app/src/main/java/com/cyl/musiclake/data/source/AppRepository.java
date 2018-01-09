package com.cyl.musiclake.data.source;

import android.content.Context;

import com.cyl.musiclake.data.model.Music;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by D22434 on 2018/1/8.
 */

public class AppRepository {

    public static Observable<List<Music>> getAllSongsRepository(Context mContext) {
        return Observable.create(e -> {
            List<Music> data = MusicLoader.getAllSongs(mContext);
            e.onNext(data);
            e.onComplete();
        });
    }

    public static Observable<List<Music>> getArtistSongsRepository(Context mContext, String id) {
        return Observable.create(e -> {
            List<Music> data = MusicLoader.getArtistSongs(mContext, id);
            e.onNext(data);
            e.onComplete();
        });
    }

    public static Observable<List<Music>> getAlbumSongsRepository(Context mContext, String id) {
        return Observable.create(e -> {
            List<Music> data = MusicLoader.getAlbumSongs(mContext, id);
            e.onNext(data);
            e.onComplete();
        });
    }
}
