package com.cyl.musiclake.ui.music.discover

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.Artist


interface DiscoverContract {

    interface View : BaseContract.BaseView {
        fun showEmptyView()

        fun showBaiduCharts(charts: List<Playlist>)

        fun showNeteaseCharts(charts: List<Playlist>)

        fun showArtistCharts(charts: List<Artist>)

        fun showRadioChannels(channels: List<Playlist>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadBaidu()

        fun loadNetease(tag: String)

        fun loadArtists()

        fun loadRaios()
    }
}
