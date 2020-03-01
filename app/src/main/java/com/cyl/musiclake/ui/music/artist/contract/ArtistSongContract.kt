package com.cyl.musiclake.ui.music.artist.contract

import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.ui.base.BaseContract

interface ArtistSongContract {

    interface View : BaseContract.BaseView {

        fun showEmptyView()

        fun showSongs(songList: MutableList<BaseMusicInfo>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun loadSongs(artistName: String)
    }

}
