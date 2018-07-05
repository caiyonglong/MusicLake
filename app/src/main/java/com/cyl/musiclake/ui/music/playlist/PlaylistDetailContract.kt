package com.cyl.musiclake.ui.music.playlist

import android.content.Context

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist

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

        fun deletePlaylist(playlist: Playlist)

        fun renamePlaylist(playlist: Playlist, title: String)

        fun disCollectMusic(pid: String, position: Int, music: Music)
    }
}
