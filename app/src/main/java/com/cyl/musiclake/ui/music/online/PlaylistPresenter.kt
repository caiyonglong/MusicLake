package com.cyl.musiclake.ui.music.online

import android.content.Context

import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class PlaylistPresenter @Inject
constructor() : BasePresenter<PlaylistContract.View>(), PlaylistContract.Presenter {
    override fun loadMorePlaylist(id: String, context: Context?) {
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

    override fun loadPlaylist(idx: String, context: Context?) {
        mView?.showLoading()
        val observable = PlaylistApiServiceImpl.getNeteaseRank(intArrayOf(idx.toInt()), 200)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.hideLoading()
                mView?.showPlayList(result.first())
            }

            override fun error(msg: String) {
                mView?.showError(msg, true)
                mView?.hideLoading()
            }
        })
    }

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

}
