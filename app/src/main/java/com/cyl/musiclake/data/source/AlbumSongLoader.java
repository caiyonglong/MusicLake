package com.cyl.musiclake.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cyl.musiclake.data.model.Music;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by hefuyi on 2016/11/4.
 */

public class AlbumSongLoader {

    public static Observable<List<Music>> getSongsForAlbum(final Context context, final long albumID) {
        return Observable.create(subscriber -> {
            Cursor cursor = makeAlbumSongCursor(context, albumID);
            List<Music> arrayList = new ArrayList<Music>();
            if ((cursor != null) && (cursor.moveToFirst()))
                do {
                    long id = cursor.getLong(0);
                    String title = cursor.getString(1);
                    String artist = cursor.getString(2);
                    String album = cursor.getString(3);
                    int duration = cursor.getInt(4);
                    int trackNumber = cursor.getInt(5);
                        /*This fixes bug where some track numbers displayed as 100 or 200*/
                    while (trackNumber >= 1000) {
                        trackNumber -= 1000; //When error occurs the track numbers have an extra 1000 or 2000 added, so decrease till normal.
                    }
                    long artistId = cursor.getInt(6);
                    String uri = cursor.getString(7);
                    long albumId = albumID;

                    arrayList.add(new Music(id, albumId, artistId, title, artist, album, duration, trackNumber, uri));
                }
                while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
            subscriber.onNext(arrayList);
            subscriber.onComplete();
        });
    }

    private static Cursor makeAlbumSongCursor(Context context, long albumID) {
        ContentResolver contentResolver = context.getContentResolver();
        final String albumSongSortOrder = MediaStore.Audio.Media.TITLE;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String string = "is_music=1 AND title != '' AND album_id=" + albumID;
        Cursor cursor = contentResolver.query(uri, new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "_data"}, string, null, albumSongSortOrder);
        return cursor;
    }
}
