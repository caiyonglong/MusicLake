package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.netease.BannerResult
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class DiscoverPresenter @Inject
constructor() : BasePresenter<DiscoverContract.View>(), DiscoverContract.Presenter {

    override fun loadBaidu() {
        ApiManager.request(BaiduApiServiceImpl.getOnlinePlaylist(), object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showBaiduCharts(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }

    /**
     * 加载网易排行榜（0歌曲）
     */
    override fun loadNetease(tag: String) {
        val observable = NeteaseApiServiceImpl.getTopPlaylists(tag, 30)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showNeteaseCharts(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }

    override fun loadArtists() {
        ApiManager.request(NeteaseApiServiceImpl.getTopArtists(30, 0), object : RequestCallBack<MutableList<Artist>> {
            override fun success(result: MutableList<Artist>) {
                mView?.showArtistCharts(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }

    override fun loadRaios() {
        loadBannerView()
        val observable = BaiduApiServiceImpl.getRadioChannel()
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showRadioChannels(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }

    fun loadBannerView() {
        val observable = NeteaseApiServiceImpl.getBanners()
        ApiManager.request(observable, object : RequestCallBack<BannerResult> {
            override fun success(result: BannerResult) {
                if (result.code == 200) {
                    mView?.showBannerView(result.banners)
                }
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }

    fun loadRecommendSongs() {
        val observable = NeteaseApiServiceImpl.recommendSongs()
        ApiManager.request(observable, object : RequestCallBack<MutableList<Music>> {
            override fun success(result: MutableList<Music>) {
                mView?.showRecommendSongs(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }

    fun loadRecommendPlaylist() {
        val observable = NeteaseApiServiceImpl.recommendPlaylist()
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showRecommendPlaylist(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
            }
        })
    }
}
