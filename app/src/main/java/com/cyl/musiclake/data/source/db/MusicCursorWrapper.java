package com.cyl.musiclake.data.source.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.utils.LogUtil;

public class MusicCursorWrapper extends CursorWrapper {

    public MusicCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Music getMusic() {
        Music music = new Music();
        music.setId(getString(getColumnIndex(DBData.MUSIC_ID)));
        music.setTitle(getString(getColumnIndex(DBData.MUSIC_NAME)));
        music.setArtistId(getLong(getColumnIndex(DBData.MUSIC_ALBUM_ID)));
        music.setArtist(getString(getColumnIndex(DBData.MUSIC_ARTIST)));
        music.setAlbum(getString(getColumnIndex(DBData.MUSIC_ALBUM)));
        music.setAlbumId(getLong(getColumnIndex(DBData.MUSIC_ALBUM_ID)));

        music.setDuration(getLong(getColumnIndex(DBData.MUSIC_TIME)));
        music.setUri(getString(getColumnIndex(DBData.MUSIC_PATH)));
        music.setLrcPath(getString(getColumnIndex(DBData.MUSIC_LRC_PATH)));
        music.setCoverUri(getString(getColumnIndex(DBData.MUSIC_COVER)));
        music.setCoverBig(getString(getColumnIndex(DBData.MUSIC_COVER_BIG)));
        music.setCoverSmall(getString(getColumnIndex(DBData.MUSIC_COVER_SMALL)));

        music.setFileName(getString(getColumnIndex(DBData.MUSIC_FILENAME)));
        music.setPrefix(getString(getColumnIndex(DBData.MUSIC_PREFIX)));
        music.setLove(getInt(getColumnIndex(DBData.IS_LOVE)) == 1);
        music.setOnline(getInt(getColumnIndex(DBData.IS_ONLINE)) == 1);
        String type = getString(getColumnIndex(DBData.MUSIC_TYPE));
        music.setType(type);
        music.setFileSize(getLong(getColumnIndex(DBData.MUSIC_SIZE)));
        music.setYear(getString(getColumnIndex(DBData.MUSIC_YEARS)));
        LogUtil.e(music.toString());
        return music;
    }

    public Playlist getPlaylist() {
        String id = getString(getColumnIndex(DBData.PLAYLIST_ID));
        String name = getString(getColumnIndex(DBData.PLAYLIST_NAME));
        long create_time = getLong(getColumnIndex(DBData.PLAYLIST_DATE));
        String order = getString(getColumnIndex(DBData.PLAYLIST_ORDER));
        int num = getInt(getColumnIndex("num"));
        return new Playlist(id, name, num, create_time, order);
    }

    public Album getAlbum() {
        long id = getLong(getColumnIndex(DBData.MUSIC_ALBUM_ID));
        String name = getString(getColumnIndex(DBData.MUSIC_ALBUM));
        long artistId = getLong(getColumnIndex(DBData.MUSIC_ARTIST_ID));
        String artist = getString(getColumnIndex(DBData.MUSIC_ARTIST));
        int num = getInt(getColumnIndex("num"));
        return new Album(id, name, artist, artistId, num);
    }

    public Artist getArtist() {
        long id = getLong(getColumnIndex(DBData.MUSIC_ARTIST_ID));
        String name = getString(getColumnIndex(DBData.MUSIC_ARTIST));
        int num = getInt(getColumnIndex("num"));
        return new Artist(id, name, num, num);
    }

}
