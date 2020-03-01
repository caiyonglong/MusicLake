package com.cyl.musiclake.ui.music.local.contract

import com.cyl.musiclake.ui.base.BaseContract
import com.music.lake.musiclib.bean.BaseMusicInfo

interface SongsContract {

    interface View : BaseContract.BaseView {
        fun showSongs(songList: MutableList<BaseMusicInfo>)

        fun setEmptyView()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSongs(isReload: Boolean)
    }
}
