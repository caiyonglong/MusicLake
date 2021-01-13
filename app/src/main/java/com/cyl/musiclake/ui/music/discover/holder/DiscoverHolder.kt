package com.cyl.musiclake.ui.music.discover.holder

import com.cyl.musicapi.netease.BannerBean
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.ui.music.discover.DiscoverEntry

class DiscoverBannerBean(var data: MutableList<BannerBean>) : DiscoverEntry {

    override fun getItemType(): Int {
        return DiscoverEntry.DISCOVER_BANNER
    }

}

class DiscoverOtherBean() : DiscoverEntry {

    override fun getItemType(): Int {
        return DiscoverEntry.DISCOVER_OTHER
    }
}

class DiscoverHotSingerBean(var data: MutableList<Artist>) : DiscoverEntry {

    override fun getItemType(): Int {
        return DiscoverEntry.DISCOVER_BANNER
    }
}

class DiscoverRecommendPlaylistBean(var data: MutableList<Artist>) : DiscoverEntry {

    override fun getItemType(): Int {
        return DiscoverEntry.DISCOVER_RECOMMEND_PLAYLIST
    }
}