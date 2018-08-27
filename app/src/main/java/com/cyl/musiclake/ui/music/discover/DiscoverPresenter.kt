package com.cyl.musiclake.ui.music.discover

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

    companion object {
        var artistList = mutableListOf<Artist>()
        var radioList = mutableListOf<Playlist>()
    }

    override fun loadBaidu() {
        ApiManager.request(BaiduApiServiceImpl.getOnlinePlaylist(), object : RequestCallBack<List<Playlist>> {
            override fun success(result: List<Playlist>) {
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
        val observable = NeteaseApiServiceImpl.getTopPlaylists(tag, 20)
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
                artistList = result
                mView?.showArtistCharts(result)
            }

            override fun error(msg: String) {
                println(msg)
            }
        })
    }

    override fun loadRaios() {
        val observable = BaiduApiServiceImpl.getRadioChannel()
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                radioList = result
                mView?.showRadioChannels(result)
            }

            override fun error(msg: String) {
                println(msg)
            }
        })
    }
}
