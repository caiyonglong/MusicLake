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

public class ArtistSongLoader {

    public static Observable<List<Music>> getSongsForArtist(final Context context, final long artistID) {
        return Observable.create(subscriber -> {
            Cursor cursor = makeArtistSongCursor(context, artistID);
            List<Music> songsList = new ArrayList<Music>();
            if ((cursor != null) && (cursor.moveToFirst()))
                do {
                    long id = cursor.getLong(0);
                    String title = cursor.getString(1);
                    String artist = cursor.getString(2);
                    String album = cursor.getString(3);
                    int duration = cursor.getInt(4);
                    int trackNumber = cursor.getInt(5);
                    long albumId = cursor.getInt(6);
                    long artistId = artistID;
                    String uri = cursor.getString(7);

                    songsList.add(new Music(id, albumId, artistId, title, artist, album, duration, trackNumber, uri));
                }
                while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            subscriber.onNext(songsList);
            subscriber.onComplete();
        });
    }


    private static Cursor makeArtistSongCursor(Context context, long artistID) {
        ContentResolver contentResolver = context.getContentResolver();
        final String artistSongSortOrder = MediaStore.Audio.Media.TITLE;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String string = "is_music=1 AND title != '' AND artist_id=" + artistID;
        return contentResolver.query(uri, new String[]{"_id", "title", "artist", "album", "duration", "track", "album_id", "_data"}, string, null, artistSongSortOrder);
    }
}
