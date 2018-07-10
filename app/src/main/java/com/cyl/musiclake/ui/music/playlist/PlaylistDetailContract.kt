package com.cyl.musiclake.ui.music.playlist

import android.content.Context

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.db.Album
import com.cyl.musiclake.db.Artist

interface PlaylistDetailContract {

    interface View : BaseContract.BaseView {

        override fun getContext(): Context

        fun showPlaylistSongs(songList: MutableList<Music>?)

        fun changePlayStatus(isPlaying: Boolean?)

        fun removeMusic(position: Int)

        fun success(type: Int)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun loadPlaylistSongs(playlist: Playlist)

        fun loadArtistSongs(artist: Artist)
        fun loadAlbumSongs(album: Album)

        fun deletePlaylist(playlist: Playlist)

        fun renamePlaylist(playlist: Playlist, title: String)

        fun disCollectMusic(pid: String, position: Int, music: Music)
    }
}
