package com.cyl.musiclake.ui.music.local.contract

import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.bean.Music

interface SongsContract {

    interface View : BaseContract.BaseView {
        fun showSongs(songList: MutableList<Music>)

        fun setEmptyView()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSongs(isReload: Boolean)
    }
}
