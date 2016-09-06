package com.cyl.music_hnust.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.db.DBDao;
import com.cyl.music_hnust.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/9 10:57
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 音乐扫描工具类
 */
public class MusicUtils {
    /**
     * 扫描本地歌曲
     */
    public static List<Music> scanMusic(Context context){
        List<Music> musicList=new ArrayList<>();
        musicList.clear();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
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
//            String coverUri = getCoverUri(context, albumId);
            String coverUri = String.valueOf(albumId);
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String year = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
            Music music = new Music();
            music.setId(id);
            music.setType(Music.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
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
//        CoverLoader.getInstance().loadThumbnail(uri);
        return uri;
    }

    /**
     * 扫描歌单
     * @param context
     * @return
     */
    public static List<String> scanPlaylist(Context context){
        List<String> playlists = new ArrayList<>();
        DBDao dbDao = new DBDao(context);
        playlists = dbDao.getPlaylist();
        return playlists;
    }

    /**
     * 新增歌单
     * @param context
     * @param playlist
     * @return
     */
    public static Boolean addPlaylist(Context context,String playlist){
        DBDao dbDao = new DBDao(context);
        return dbDao.newPlaylist(playlist);
    }

}
