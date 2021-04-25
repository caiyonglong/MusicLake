package com.cyl.musiclake.ui.music.charts

import com.cyl.musicapi.BaseApiImpl
import com.cyl.musicapi.bean.TopListBean
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class PlaylistPresenter @Inject
constructor() : BasePresenter<PlaylistContract.View>(), PlaylistContract.Presenter {

    /**
     *根据歌单id获取歌单详情
     */
    override fun loadNeteasePlaylist(id: String) {
        mView?.showLoading()
        val observable = NeteaseApiServiceImpl.getPlaylistDetail(id)
        ApiManager.request(observable, object : RequestCallBack<Playlist> {
            override fun success(result: Playlist) {
                mView?.hideLoading()
                mView?.showPlayList(result)
            }

            override fun error(msg: String) {
                mView?.showError(msg, true)
                mView?.hideLoading()
            }
        })
    }

    /**
     * 自身的服务器
     * 获取排行榜歌单
     */
    override fun loadPlaylist(idx: String, type: String?) {
        val musicType = if (type == Constants.PLAYLIST_QQ_ID) Constants.QQ else Constants.NETEASE
        mView?.showLoading()
        val callBack: (result: TopListBean) -> Unit = { bean ->
            bean.let {
                val playlist = Playlist()
                playlist.coverUrl = it.cover
                playlist.des = it.description
                playlist.pid = it.id
                playlist.name = it.name
                playlist.type = type.toString()
                playlist.playCount = it.playCount
                playlist.musicList = MusicUtils.getMusicList(it.list, musicType)
                mView?.hideLoading()
                mView?.showPlayList(playlist)
            }
        }
        if (type == Constants.PLAYLIST_QQ_ID) {
            BaseApiImpl.getQQTopList(idx, success = callBack, fail = {
                mView?.showError(it, true)
            })
        } else {
            BaseApiImpl.getPlaylistDetail(musicType, idx, {
                val playlist = Playlist()
                playlist.type = Constants.PLAYLIST_WY_ID
                playlist.name = it.detail.name
                playlist.des = it.detail.desc
                playlist.coverUrl = it.detail.cover
                playlist.pid = it.detail.id
                playlist.musicList = MusicUtils.getMusicList(it.songs, musicType)
                mView?.hideLoading()
                mView?.showPlayList(playlist)
            }, {
                mView.showError(it, true)
            })
        }
    }

    /**
     * 获取百度音乐排行榜音乐
     */
    override fun loadOnlineMusicList(type: String, limit: Int, mOffset: Int) {
        mView?.showLoading()
        ApiManager.request(BaiduApiServiceImpl.getOnlineSongs(type, limit, mOffset), object : RequestCallBack<MutableList<Music>> {
            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

            override fun success(result: MutableList<Music>?) {
                result?.forEach {
                    if (it.isCp)
                        result.remove(it)
                }
                mView?.showOnlineMusicList(result)
                mView?.hideLoading()
            }

        })
    }

    /**
     * 加载推荐歌单列表（0歌曲）
     */
    fun loadNetease(tag: String) {
        val observable = NeteaseApiServiceImpl.getTopPlaylists(tag, 100)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showNeteaseCharts(result)
            }

            override fun error(msg: String) {
                mView?.showNeteaseCharts(mutableListOf())
            }
        })
    }

    /**
     * 加载精品歌单列表（0歌曲）
     */
    fun loadHighQualityPlaylist(tag: String) {
        val observable = NeteaseApiServiceImpl.getTopPlaylistsHigh(tag, 100, null)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showNeteaseCharts(result)
            }

            override fun error(msg: String) {
                mView?.showNeteaseCharts(mutableListOf())
            }
        })
    }

}
