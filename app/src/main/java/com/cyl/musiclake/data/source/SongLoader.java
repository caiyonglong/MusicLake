package com.cyl.musiclake.data.source;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.db.DBDaoImpl;
import com.cyl.musiclake.data.source.db.DBData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class SongLoader {

    public static Observable<List<Music>> getSongsForCursor(Context context, final Cursor cursor) {
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

    public static Observable<List<Music>> getFavoriteSong(final Context context) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
                String sql = "select * from " + DBData.MUSIC_TABLE + " where " + DBData.IS_LOVE + "= 1";
                Cursor cursor = dbDaoImpl.makeCursor(sql);
                List<Music> results = dbDaoImpl.getSongsForCursor(cursor);
                dbDaoImpl.closeDB();
                subscriber.onNext(results);
                subscriber.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    public static Music getMusicInfo(final Context context, String mid) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        return dbDaoImpl.getMusicInfo(mid);
    }

    public static Observable<Music> getMusicInfo(final Context context, Music music) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
                String sql = "select * from " + DBData.MUSIC_TABLE + " where " + DBData.MUSIC_ID + " = '" + music.getId() + "'";
                Cursor cursor = dbDaoImpl.makeCursor(sql);
                List<Music> results = dbDaoImpl.getSongsForCursor(cursor);
                if (results.size() != 0) {
                    subscriber.onNext(results.get(0));
                } else {
                    subscriber.onNext(music);
                }
                dbDaoImpl.closeDB();
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    public static Observable<Music> updateFavoriteSong(final Context context, Music music) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
                music.setLove(!music.isLove());
                dbDaoImpl.insertSong(music);
                dbDaoImpl.updateSong(music);
                dbDaoImpl.closeDB();
                subscriber.onNext(music);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * 本地歌曲
     * 添加歌曲
     */
    private static void insertSongs(Context context, List<Music> musics) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.insertSongs(musics);
        dbDaoImpl.closeDB();
    }

    /**
     * 本地歌曲
     * 移除歌曲
     */
    public static void removeSong(Context context, Music music) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.deleteSongForId(music.getId());
        dbDaoImpl.closeDB();
    }

    public static Observable<List<Music>> getAllSongs(Context context) {
        return getSongsForCursor(context, makeSongCursor(context, null, null));
    }

    public static Observable<List<Music>> searchSongs(Context context, String searchString) {
        return getSongsForCursor(context, makeSongCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                new String[]{"%" + searchString + "%", "%" + searchString + "%", "%" + searchString + "%"}));
    }

    public static Observable<List<Music>> getSongListInFolder(Context context, String path) {
        String[] whereArgs = new String[]{path + "%"};
        return getSongsForCursor(context, makeSongCursor(context, MediaStore.Audio.Media.DATA + " LIKE ?", whereArgs, null));
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
