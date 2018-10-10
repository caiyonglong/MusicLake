package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.netease.BannerBean
import com.cyl.musicapi.netease.BannerResult
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import java.util.*
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
                println(msg)
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
                println(msg)
            }
        })
    }

    override fun loadArtists() {
        ApiManager.request(NeteaseApiServiceImpl.getTopArtists(30, 0), object : RequestCallBack<MutableList<Artist>> {
            override fun success(result: MutableList<Artist>) {
                mView?.showArtistCharts(result)
            }

            override fun error(msg: String) {
                println(msg)
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
                println(msg)
            }
        })
    }

    fun loadBannerView() {
        val observable = NeteaseApiServiceImpl.getBanners()
        ApiManager.request(observable, object : RequestCallBack<BannerResult> {
            override fun success(result: BannerResult) {
                if (result.code==200){
                    mView?.showBannerView(result.banners)
                }
//                mView?.(result)
            }

            override fun error(msg: String) {
                println(msg)
            }
        })
    }
}
