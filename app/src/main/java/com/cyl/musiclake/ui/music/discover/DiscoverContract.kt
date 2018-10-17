package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.netease.BannerBean
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.Artist


interface DiscoverContract {

    interface View : BaseContract.BaseView {
        fun showEmptyView(msg: String)

        fun showBaiduCharts(charts: MutableList<Playlist>)

        fun showNeteaseCharts(charts: MutableList<Playlist>)

        fun showArtistCharts(charts: MutableList<Artist>)

        fun showRadioChannels(channels: MutableList<Playlist>)

        fun showBannerView(banners: MutableList<BannerBean>)

    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadBaidu()

        fun loadNetease(tag: String)

        fun loadArtists()

        fun loadRaios()
    }
}
