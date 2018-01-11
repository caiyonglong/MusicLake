package com.cyl.musiclake.data.source;

import android.content.Context;

import com.cyl.musiclake.data.model.Album;
import com.cyl.musiclake.data.model.Artist;
import com.cyl.musiclake.data.model.FolderInfo;
import com.cyl.musiclake.data.model.Music;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by D22434 on 2018/1/8.
 */

public class AppRepository {

    public static Observable<List<Music>> getAllSongsRepository(Context mContext) {
        return SongLoader.getAllSongs(mContext);
    }

    public static Observable<List<Music>> getArtistSongsRepository(Context mContext, long id) {
        return ArtistSongLoader.getSongsForArtist(mContext, id);
    }

    public static Observable<List<Music>> getAlbumSongsRepository(Context mContext, long id) {
        return AlbumSongLoader.getSongsForAlbum(mContext, id);
    }

    public static Observable<List<Artist>> getAllArtistsRepository(Context mContext) {
        return ArtistLoader.getAllArtists(mContext);
    }

    public static Observable<List<Album>> getAllAlbumsRepository(Context mContext) {
        return AlbumLoader.getAllAlbums(mContext);
    }

    public static Observable<List<Music>> getPlaylistSongsRepository(Context mContext, String playlistId) {
        return PlaylistLoader.getMusicForPlaylist(mContext, playlistId);
    }

    public static Observable<List<FolderInfo>> getFolderInfosRepository(Context mContext) {
        return FolderLoader.getFoldersWithSong(mContext);
    }

}
