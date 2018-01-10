package com.cyl.musiclake.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.cyl.musiclake.data.model.Album;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.db.DBDaoImpl;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/11/4 22:30
 */

public class SongQueueLoader {


    /**
     * 获取一个专辑的详情信息
     *
     * @param context
     * @param id
     * @return
     */
    public static Album getAlbum(Context context, long id) {


        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "album", "artist", "artist_id", "numsongs"},
                "_id=?", new String[]{String.valueOf(id)}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        Album album = new Album();

        if (cursor != null) {
            if (cursor.moveToFirst())
                album = new Album(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3),
                        cursor.getInt(4));
        }
        if (cursor != null)
            cursor.close();
        return album;
    }

    /**
     * 获取所有专辑
     *
     * @param context
     * @return
     */
    public static List<Album> getAllAlbums(Context context) {
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                new String[]{"_id", "album", "artist", "artist_id", "numsongs"},
//                null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
//
//        //            @Override
////            public void call(Subscriber<? super List<Album>> subscriber) {
////
////            }
//        return Observable.create(e -> {
//            List<Album> arrayList = new ArrayList<Album>();
//            if ((cursor != null) && (cursor.moveToFirst()))
//                do {
//                    arrayList.add(new Album(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4)));
//                }
//                while (cursor.moveToNext());
//            if (cursor != null) {
//                cursor.close();
//            }
//            e.onNext(arrayList);
//            e.onComplete();
////                e.onCompleted();
//        });


        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "album", "artist", "artist_id", "numsongs"},
                null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        List<Album> arrayList = new ArrayList<>();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                if (cursor.getString(2).equals("<unknown>"))
                    continue;
                arrayList.add(new Album(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3),
                        cursor.getInt(4)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();

        return arrayList;
    }


    /**
     * 获取音乐专辑歌曲
     *
     * @param context
     * @return
     */
    public static List<Music> getAlbumSongs(Context context, String albumID) {
        List<Music> musicList = new ArrayList<>();


        ContentResolver contentResolver = context.getContentResolver();
//        final String albumSongSortOrder = PreferencesUtility.getInstance(context).getAlbumSongSortOrder();
        String string = MediaStore.Audio.Media.IS_MUSIC + "=1 AND title != '' AND album_id=" + albumID;
        Cursor cursor = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, string,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return musicList;
        }
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (artist.equals("<unknown>")) {
                continue;
            }
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String coverUri = getCoverUri(context, albumId);
//            String coverUri = String.valueOf(albumId);
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String year = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
            Music music = new Music();
            music.setId(id);
            music.setType(Music.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setAlbumId(albumId);
            music.setDuration(duration);
            music.setUri(uri);
            music.setCoverUri(coverUri);
            music.setFileName(fileName);
            music.setFileSize(fileSize);
            music.setYear(year);
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    /**
     * 获取艺术家歌曲
     *
     * @param context
     * @return
     */
    public static List<Music> getArtistSongs(Context context, String artistID) {
        List<Music> musicList = new ArrayList<>();
        String string = "is_music=1 AND title != '' AND artist_id=" + artistID;
        Cursor cursor = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, string,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return musicList;
        }
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (artist.equals("<unknown>")) {
                continue;
            }
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String coverUri = getCoverUri(context, albumId);
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String year = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
            Music music = new Music();
            music.setId(id);
            music.setType(Music.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setAlbumId(albumId);
            music.setDuration(duration);
            music.setUri(uri);
            music.setCoverUri(coverUri);
            music.setFileName(fileName);
            music.setFileSize(fileSize);
            music.setYear(year);
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }


    private static String getCoverUri(Context context, long albumId) {
        String uri = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            uri = cursor.getString(0);
            cursor.close();
        }
        CoverLoader.getInstance().loadThumbnail(uri);
        return uri;
    }


    /**
     * 获取所有歌单
     *
     * @param context
     * @return
     */
    public static List<Music> getAllSongs(Context context) {
        List<Music> musicList = new ArrayList<>();
        Cursor cursor = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Audio.Media.IS_MUSIC + "=1",
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null && cursor.getCount() == 0) {
            return musicList;
        }
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (artist.equals("<unknown>")) {
                continue;
            }
            long artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String coverUri = getCoverUri(context, albumId);
//            String coverUri = String.valueOf(albumId);
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String year = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
            Music music = new Music();
            music.setId(id);
            music.setType(Music.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setArtistId(artistId);
            music.setAlbum(album);
            music.setAlbumId(albumId);
            music.setDuration(duration);
            music.setUri(uri);
            music.setCoverUri(coverUri);
            music.setFileName(fileName);
            music.setFileSize(fileSize);
            music.setYear(year);
            musicList.add(music);
        }
        cursor.close();
        insertSongs(context, musicList);
        return musicList;
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
        String selectionStatement = "is_music=1 AND title != ''";
        final String songSortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selectionStatement, paramArrayOfString, songSortOrder);

        return cursor;
    }


    /**
     * 添加歌曲到歌单
     */
    private static void insertSongs(Context context, List<Music> musics) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
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

    public static Cursor query(Context context, Uri uri, String[] projection, String selection,
                               String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
            return null;
        }
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection,
                               String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }

}
