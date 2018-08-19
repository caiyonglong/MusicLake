package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.BaseApiImpl
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.Artist
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

    private val neteaseLists = ArrayList<Playlist>()
    private val charts = arrayOf("云音乐新歌榜", "云音乐热歌榜", "网易原创歌曲榜", "云音乐飙升榜", "云音乐电音榜", "UK排行榜周榜", "美国Billboard周榜 ", "KTV嗨榜 ", "iTunes榜 ", "Hit FM Top榜 ", "日本Oricon周榜 ", "韩国Melon排行榜周榜 ", "韩国Mnet排行榜周榜 ", "韩国Melon原声周榜 ", "中国TOP排行榜(港台榜) ", "中国TOP排行榜(内地榜)", "香港电台中文歌曲龙虎榜 ", "华语金曲榜", "中国嘻哈榜", "法国 NRJ EuroHot 30 周榜", "台湾Hito排行榜 ", "Beatport全球电子舞曲榜")

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

    override fun loadNetease() {
        for (i in charts.indices) {
            val playlist = Playlist()
            playlist.pid = i.toString()
            playlist.name = charts[i]
            neteaseLists.add(playlist)
            BaseApiImpl.getInstance(mView.context).getTopList(i.toString()) {
                it.data.let {
                    val playlist = Playlist()
                    playlist.pid = i.toString()
                    playlist.name = it.name
                    playlist.count = it.playCount
                    playlist.coverUrl = it.cover
                    playlist.des = it.description
                    val musicList = ArrayList<Music>()
                    if (it.list!!.isNotEmpty()) {
                        for (item in it.list!!) {
                            val music = MusicUtils.getMusic(item)
                            musicList.add(music)
                        }
                    }
                    playlist.musicList = musicList
                    neteaseLists[i] = playlist
                    mView?.showNeteaseCharts(neteaseLists)
                }
            }
        }
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
