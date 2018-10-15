package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.bean.SingerTag
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Artist


interface ArtistListContract {

    interface View : BaseContract.BaseView {
        fun showArtistList(charts: MutableList<Artist>)
        fun showArtistTags(charts: SingerTag)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadArtists(offset: Int, params: Any)
    }
}
