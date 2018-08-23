package com.cyl.musiclake.ui.music.online.base

import android.content.Context

import com.cyl.musicapi.BaseApiImpl
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack

import java.util.ArrayList

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class PlaylistPresenter @Inject
constructor() : BasePresenter<PlaylistContract.View>(), PlaylistContract.Presenter {
    override fun loadPlaylist(idx: String, context: Context?) {
        val observable = PlaylistApiServiceImpl.getNeteaseRank(intArrayOf(idx.toInt()), 200)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showPlayList(result.first())
            }

            override fun error(msg: String) {
                mView?.showError(msg, true)
                mView.hideLoading()
            }
        })
    }

    override fun loadOnlineMusicList(type: String, limit: Int, mOffset: Int) {
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
