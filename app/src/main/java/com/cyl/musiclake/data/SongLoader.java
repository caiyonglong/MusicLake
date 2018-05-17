package com.cyl.musiclake.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.db.DBDaoImpl;
import com.cyl.musiclake.data.db.DBData;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class SongLoader {

    /**
     * cursor 获取音乐集合
     *
     * @param context
     * @param cursor
     * @return
     */
    private static Observable<List<Music>> getSongsForCursor(Context context, final Cursor cursor) {
        return Observable.create(subscriber -> {
            try {
                DBDaoImpl dbDao = new DBDaoImpl(context);
                List<Music> results = dbDao.getSongsForCursor(cursor);
                cursor.close();
                subscriber.onNext(results);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * Android 扫描获取到的数据
     *
     * @param context
     * @param cursor
     * @return
     */
    public static Observable<List<Music>> getSongsForMedia(Context context, final Cursor cursor) {
        return Observable.create(subscriber -> {
            DBDaoImpl dbDao = new DBDaoImpl(context);
            List<Music> results = new ArrayList<>();
            try {
                if ((cursor != null) && (cursor.moveToFirst())) {
                    do {
                        int is_music = cursor.getInt(9);
                        long id = cursor.getLong(0);
                        String title = cursor.getString(1);
                        String artist = cursor.getString(2);
                        String album = cursor.getString(3);
                        int duration = cursor.getInt(4);
                        int trackNumber = cursor.getInt(5);
                        String artistId = cursor.getString(6);
                        String albumId = cursor.getString(7);
                        String path = cursor.getString(8);
                        String coverUri = CoverLoader.getCoverUri(context, albumId);
                        Music music = dbDao.getMusicInfo(id + "");
                        if (music != null) {
                            if (coverUri != null) {
                                music.setCoverUri(coverUri);
                                updateMusic(dbDao, music);
                            }
                        } else {
                            music = new Music(id, albumId, artistId, title, artist, album, duration, trackNumber, path);
                            dbDao.insertSong(music);
                        }
                        results.add(music);
                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
                subscriber.onNext(results);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * Android 扫描获取到的数据
     *
     * @param context
     * @param cursor
     * @return
     */
    public static Observable<List<Music>> getSongsForDB(Context context, final Cursor cursor) {
        DBDaoImpl dbDao = new DBDaoImpl(context);
        String sql = "select * from " + DBData.MUSIC_TABLE + " where " + DBData.IS_ONLINE + "=0";
        Cursor mCursor = dbDao.makeCursor(sql);
        return getSongsForCursor(context, mCursor);
    }

    /**
     * 获取所有收藏的歌曲
     *
     * @param context
     * @return
     */
    public static Observable<List<Music>> getFavoriteSong(final Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "select * from " + DBData.MUSIC_TABLE + " where " + DBData.IS_LOVE + "= 1";
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        return getSongsForCursor(context, cursor);
    }

    public static Music getMusicInfo(final Context context, String mid) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        return dbDaoImpl.getMusicInfo(mid);
    }

    public static List<Music> getSongsForDB(Context context) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        String sql = "select * from " + DBData.MUSIC_TABLE;
        Cursor cursor = dbDaoImpl.makeCursor(sql);
        return dbDaoImpl.getSongsForCursor(cursor);
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
     * 添加歌曲
     */
    private static void updateMusic(DBDaoImpl dbDaoImpl, Music music) {
        dbDaoImpl.updateSong(music);
    }

    /**
     * 删除歌曲
     */
    public static void removeSong(Context context, Music music) {
        DBDaoImpl dbDaoImpl = new DBDaoImpl(context);
        dbDaoImpl.deleteSongForId(music.getId());
        dbDaoImpl.closeDB();
    }

    public static Observable<List<Music>> getAllSongs(Context context) {
        return getSongsForDB(context, makeSongCursor(context, null, null));
    }

    public static Observable<List<Music>> getAllLocalSongs(Context context) {
        return getSongsForMedia(context, makeSongCursor(context, null, null));
    }

    public static Observable<List<Music>> searchSongs(Context context, String searchString) {
        return getSongsForMedia(context, makeSongCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                new String[]{"%" + searchString + "%", "%" + searchString + "%", "%" + searchString + "%"}));
    }

    public static Observable<List<Music>> getSongListInFolder(Context context, String path) {
        String[] whereArgs = new String[]{path + "%"};
        return getSongsForMedia(context, makeSongCursor(context, MediaStore.Audio.Media.DATA + " LIKE ?", whereArgs, null));
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
        final String songSortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        return makeSongCursor(context, selection, paramArrayOfString, songSortOrder);
    }

    private static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "duration>60000 AND is_music=1 AND title != ''";

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", MediaStore.Audio.Media.DATA, "is_music"},
                selectionStatement, paramArrayOfString, sortOrder);
    }

}
