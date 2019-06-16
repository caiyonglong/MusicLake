package com.cyl.musiclake.ui.music.discover.artist

import com.cyl.musicapi.bean.SingerTag
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.bean.Artist


interface ArtistListContract {

    interface View : BaseContract.BaseView {
        fun showArtistList(artistList: MutableList<Artist>)
        fun showArtistTags(tags: SingerTag)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadArtists(offset: Int, params: Map<String,Int>)
    }
}
