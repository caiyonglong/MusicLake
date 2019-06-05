package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.netease.BannerResult
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
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
                mView?.showBaiduCharts(mutableListOf())
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
                mView?.showNeteaseCharts(mutableListOf())
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
                mView?.showArtistCharts(mutableListOf())
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
                mView?.showRadioChannels(mutableListOf())
            }
        })
    }

    fun loadBannerView() {
        val observable = NeteaseApiServiceImpl.getBanners()
        ApiManager.request(observable, object : RequestCallBack<BannerResult> {
            override fun success(result: BannerResult) {
                if (result.code == 200) {
                    mView?.showBannerView(result.banners)
                } else {
                    mView?.showBannerView(mutableListOf())
                }
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
                mView?.showBannerView(mutableListOf())
            }
        })
    }

    /**
     * 获取每日推荐（需登录）
     */
    fun loadRecommendPlaylist() {
        val observable = NeteaseApiServiceImpl.recommendPlaylist()
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showRecommendPlaylist(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
                mView?.showRecommendPlaylist(mutableListOf())
            }
        })
    }

    /**
     * 获取推荐歌单
     */
    fun loadPersonalizedPlaylist() {
        val observable = NeteaseApiServiceImpl.personalizedPlaylist()
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showRecommendPlaylist(result)
            }

            override fun error(msg: String) {
                mView?.showEmptyView(msg)
                mView?.showRecommendPlaylist(mutableListOf())
            }
        })
    }
}
