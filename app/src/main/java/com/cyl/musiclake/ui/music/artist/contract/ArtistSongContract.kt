package com.cyl.musiclake.ui.music.artist.contract

import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.ui.base.BaseContract

interface ArtistSongContract {

    interface View : BaseContract.BaseView {

        fun showEmptyView()

        fun showSongs(songList: MutableList<Music>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun loadSongs(artistName: String)
    }

}
