package com.cyl.music_hnust.dataloaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.music.Album;
import com.cyl.music_hnust.model.music.Artist;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.utils.CoverLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/11/4 22:30
 */

public class MusicLoader {


    /**
     *
     * 获取一个专辑的详情信息
     * @param context
     * @param id
     * @return
     */
    public static Album getAlbum(Context context,long id) {

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "album", "artist", "artist_id", "numsongs"},
                "_id=?", new String[]{String.valueOf(id)}, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        Album album = new Album();

        if (cursor != null) {
            if (cursor.moveToFirst())
                album =new Album(
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


        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "album", "artist", "artist_id", "numsongs"},
                null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        List<Album> arrayList = new ArrayList<>();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
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
     * 获取所有歌手
     * @param context
     * @return
     */
    public static List<Artist> getAllArtists(Context context) {
        Cursor cursor = context.getContentResolver().query
                (MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                        new String[]{"_id", "artist", "number_of_tracks"},
                        null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        List<Artist> arrayList = new ArrayList<>();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new Artist(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getInt(2)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }


    /**
     * 获取音乐专辑歌曲
     * @param context
     * @return
     */
    public static List<Music> getAlbumSongs(Context context,String albumID) {
        List<Music> musicList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
//        final String albumSongSortOrder = PreferencesUtility.getInstance(context).getAlbumSongSortOrder();
        String string = "is_music=1 AND title != '' AND album_id=" + albumID;
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null, string, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return musicList;
        }
        while (cursor.moveToNext()) {
            // 是否为音乐
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0) {
                continue;
            }
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String unknown = context.getString(R.string.unknown);
            artist = artist.equals("<unknown>") ? unknown : artist;
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
     * @param context
     * @return
     */
    public static List<Music> getArtistSongs(Context context,String artistID) {
        List<Music> musicList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String string = "is_music=1 AND title != '' AND artist_id=" + artistID;

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null, string, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return musicList;
        }
        while (cursor.moveToNext()) {
            // 是否为音乐
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0) {
                continue;
            }
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String unknown = context.getString(R.string.unknown);
            artist = artist.equals("<unknown>") ? unknown : artist;
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
}
