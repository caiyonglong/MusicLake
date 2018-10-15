package com.cyl.musiclake.ui.music.discover

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Artist


interface ArtistListContract {

    interface View : BaseContract.BaseView {
        fun showArtistList(charts: MutableList<Artist>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadArtists(offset: Int, params: Any)
    }
}
