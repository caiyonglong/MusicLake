package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.cyl.musiclake.data.model.Album;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by hefuyi on 2016/11/4.
 */

public class ArtistAlbumLoader {

    public static Observable<List<Album>> getAlbumsForArtist(final Context context, final long artistID) {
        return Observable.create(subscriber -> {
            List<Album> albumList = new ArrayList<Album>();
            Cursor cursor = makeAlbumForArtistCursor(context, artistID);

            if (cursor != null) {
                if (cursor.moveToFirst())
                    do {
                        Album album = new Album(cursor.getLong(0), cursor.getString(1), cursor.getString(2), artistID, cursor.getInt(3));
                        albumList.add(album);
                    }
                    while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
            subscriber.onNext(albumList);
            subscriber.onComplete();
        });
    }


    private static Cursor makeAlbumForArtistCursor(Context context, long artistID) {

        if (artistID == -1)
            return null;

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.Albums.getContentUri("external", artistID), new String[]{"_id", "album", "artist", "numsongs", "minyear"}, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        return cursor;
    }
}
