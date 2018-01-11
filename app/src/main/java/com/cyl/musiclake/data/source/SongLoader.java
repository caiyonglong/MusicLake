package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.db.DBDaoImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class SongLoader {
    private static Context context;

    public static Observable<List<Music>> getSongsForCursor(final Cursor cursor) {
        return Observable.create(subscriber -> {
            List<Music> arrayList = new ArrayList<>();
            if ((cursor != null) && (cursor.moveToFirst()))
                do {
                    long id = cursor.getLong(0);
                    String title = cursor.getString(1);
                    String artist = cursor.getString(2);
                    String album = cursor.getString(3);
                    int duration = cursor.getInt(4);
                    int trackNumber = cursor.getInt(5);
                    long artistId = cursor.getInt(6);
                    long albumId = cursor.getLong(7);
                    String path = cursor.getString(8);

                    arrayList.add(new Music(id, albumId, artistId, title, artist, album, duration, trackNumber, path));
                }
                while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
            insertSongs(context, arrayList);
            subscriber.onNext(arrayList);
            subscriber.onComplete();
        });
    }

//    public static Observable<List<Music>> getFavoriteSong(final Context context) {
//        Cursor cursor = FavoriteSong.getInstance(context).getFavoriteSong();
//        SortedCursor retCursor = TopTracksLoader.makeSortedCursor(context, cursor, 0);
//        return SongLoader.getSongsForCursor(retCursor);
//    }


    /**
     * 添加歌曲到歌单
     */
    private static void insertSongs(Context context, List<Music> musics) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.clearQueue();
        dbDaoImpl.insertSongs(musics);
        dbDaoImpl.closeDB();
    }

    /**
     * 添加歌曲到歌单
     */
    public static List<Music> getPlayQueue(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        List<Music> results = dbDaoImpl.getQueue();
        dbDaoImpl.closeDB();
        return results;
    }


    /**
     * 添加歌曲到歌单
     */
    public static void updateQueue(Context context, List<Music> musics) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.updateQueue(musics);
        dbDaoImpl.closeDB();
    }

    /**
     * 移除歌曲到歌单
     */
    public static void clearQueue(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.clearQueue();
        dbDaoImpl.closeDB();
    }


    public static Observable<List<Music>> getAllSongs(Context context) {
        return getSongsForCursor(makeSongCursor(context, null, null));
    }

    public static Observable<List<Music>> searchSongs(Context context, String searchString) {
        return getSongsForCursor(makeSongCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                new String[]{"%" + searchString + "%", "%" + searchString + "%", "%" + searchString + "%"}));
    }

    public static Observable<List<Music>> getSongListInFolder(Context context, String path) {
        String[] whereArgs = new String[]{path + "%"};
        return getSongsForCursor(makeSongCursor(context, MediaStore.Audio.Media.DATA + " LIKE ?", whereArgs, null));
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
        final String songSortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        return makeSongCursor(context, selection, paramArrayOfString, songSortOrder);
    }

    private static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "is_music=1 AND title != ''";

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", MediaStore.Audio.Media.DATA},
                selectionStatement, paramArrayOfString, sortOrder);
    }

}
