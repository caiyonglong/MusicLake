package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.cyl.musiclake.data.model.Artist;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by hefuyi on 2016/11/4.
 */

public class ArtistLoader {

    private static Observable<Artist> getArtist(final Cursor cursor) {
        return Observable.create(subscriber -> {
            Artist artist = new Artist();
            if (cursor != null) {
                if (cursor.moveToFirst())
                    artist = new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
            }
            if (cursor != null) {
                cursor.close();
            }
            subscriber.onNext(artist);
            subscriber.onComplete();
        });

    }

    private static Observable<List<Artist>> getArtistsForCursor(final Cursor cursor) {
        return Observable.create(subscriber -> {
            List<Artist> arrayList = new ArrayList<Artist>();
            if ((cursor != null) && (cursor.moveToFirst()))
                do {
                    arrayList.add(new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
                }
                while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
            subscriber.onNext(arrayList);
            subscriber.onComplete();
        });
    }

    public static Observable<List<Artist>> getAllArtists(Context context) {
        return getArtistsForCursor(makeArtistCursor(context, null, null));
    }

    public static Observable<Artist> getArtist(Context context, long id) {
        return getArtist(makeArtistCursor(context, "_id=?", new String[]{String.valueOf(id)}));
    }

    public static Observable<List<Artist>> getArtists(Context context, String paramString) {
        return getArtistsForCursor(makeArtistCursor(context, "artist LIKE ?", new String[]{"%" + paramString + "%"}));
    }

    private static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        final String artistSortOrder = MediaStore.Audio.Media.ARTIST;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, selection, paramArrayOfString, artistSortOrder);
        return cursor;
    }
}
