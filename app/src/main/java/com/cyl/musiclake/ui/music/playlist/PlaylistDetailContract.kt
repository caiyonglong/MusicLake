package com.cyl.musiclake.ui.music.playlist

import android.content.Context

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist

interface PlaylistDetailContract {

    interface View : BaseContract.BaseView {

        override fun getContext(): Context

        fun showPlaylistSongs(songList: MutableList<Music>?)

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
