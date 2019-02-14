package com.cyl.musiclake.bean.data.db

import android.database.Cursor
import android.database.CursorWrapper

import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist

class MusicCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    //    public Music getMusic() {
    //        Music music = new Music();
    //        music.setId(getString(getColumnIndex(DBData.MUSIC_ID)));
    //        music.setTitle(getString(getColumnIndex(DBData.MUSIC_NAME)));
    //        music.setArtistId(getString(getColumnIndex(DBData.MUSIC_ALBUM_ID)));
    //        music.setArtist(getString(getColumnIndex(DBData.MUSIC_ARTIST)));
    //        music.setAlbum(getString(getColumnIndex(DBData.MUSIC_ALBUM)));
    //        music.setAlbumId(getString(getColumnIndex(DBData.MUSIC_ALBUM_ID)));
    //
    //        music.setDuration(getLong(getColumnIndex(DBData.MUSIC_TIME)));
    //        music.setUri(getString(getColumnIndex(DBData.MUSIC_PATH)));
    //        music.setLrcPath(getString(getColumnIndex(DBData.MUSIC_LRC_PATH)));
    //        music.setCoverUri(getString(getColumnIndex(DBData.MUSIC_COVER)));
    //        music.setCoverBig(getString(getColumnIndex(DBData.MUSIC_COVER_BIG)));
    //        music.setCoverSmall(getString(getColumnIndex(DBData.MUSIC_COVER_SMALL)));
    //
    //        music.setFileName(getString(getColumnIndex(DBData.MUSIC_FILENAME)));
    //        music.setPrefix(getString(getColumnIndex(DBData.MUSIC_PREFIX)));
    //        music.setLove(getInt(getColumnIndex(DBData.IS_LOVE)) == 1);
    //        music.setOnline(getInt(getColumnIndex(DBData.IS_ONLINE)) == 1);
    //        String type = getString(getColumnIndex(DBData.MUSIC_TYPE));
    //        music.setType(type);
    //        music.setFileSize(getLong(getColumnIndex(DBData.MUSIC_SIZE)));
    //        music.setYear(getString(getColumnIndex(DBData.MUSIC_YEARS)));
    //        LogUtil.e(music.toString());
    //        return music;
    //    }
    //
    //    public Playlist getPlaylist() {
    //        String id = getString(getColumnIndex(DBData.PLAYLIST_ID));
    //        String name = getString(getColumnIndex(DBData.PLAYLIST_NAME));
    //        long create_time = getLong(getColumnIndex(DBData.PLAYLIST_DATE));
    //        String order = getString(getColumnIndex(DBData.PLAYLIST_ORDER));
    //        int num = getInt(getColumnIndex("num"));
    //        return new Playlist(id, name, num, create_time, order);
    //    }
    //
    val album: Album
        get() {
            val id = getString(getColumnIndex("albumid"))
            val name = getString(getColumnIndex("album"))
            val artistId = getLong(getColumnIndex("artistid"))
            val artist = getString(getColumnIndex("artist"))
            val num = getInt(getColumnIndex("num"))
            return Album(id, name, artist, artistId, num)
        }

    val artists: Artist
        get() {
            val id = getLong(getColumnIndex("artistid"))
            val name = getString(getColumnIndex("artist"))
            val num = getInt(getColumnIndex("num"))
            return Artist(id, name, num)
        }

}
