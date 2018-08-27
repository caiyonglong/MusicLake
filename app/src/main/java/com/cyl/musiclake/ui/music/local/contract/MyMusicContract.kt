package com.cyl.musiclake.ui.music.local.contract

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist

interface MyMusicContract {

    interface View : BaseContract.BaseView {

        fun showSongs(songList: MutableList<Music>)

        fun showPlaylist(playlists: MutableList<Playlist>)

        fun showHistory(musicList: MutableList<Music>)

        fun showLoveList(musicList: MutableList<Music>)

        fun showDownloadList(musicList: MutableList<Music>)

    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSongs()

        fun loadPlaylist()
    }
}
