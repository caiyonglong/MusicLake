package com.cyl.musiclake.ui.music.charts.presenter

import com.cyl.musicapi.BaseApiImpl
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.music.charts.ChartsAdapter
import com.cyl.musiclake.ui.music.charts.GroupItemData
import com.cyl.musiclake.ui.music.charts.contract.OnlinePlaylistContract
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OnlinePlaylistPresenter @Inject
constructor() : BasePresenter<OnlinePlaylistContract.View>(), OnlinePlaylistContract.Presenter {
    override fun loadQQList() {
        BaseApiImpl.getAllQQTopList(success = { result ->
            val data = mutableListOf<GroupItemData>()
            data.add(GroupItemData("QQ音乐官方榜单"))
            var flag = true
            result.forEach {
                val playlist = Playlist()
                playlist.pid = it.id
                playlist.des = it.description
                playlist.name = it.name
                playlist.coverUrl = it.cover
                playlist.playCount = it.playCount
                playlist.musicList = MusicUtils.getMusicList(it.list, Constants.QQ)
                playlist.type = Constants.PLAYLIST_QQ_ID
                val item = GroupItemData(playlist)
                if (it.list?.size == 0) {
                    item.itemType = ChartsAdapter.ITEM_CHART
                } else {
                    item.itemType = ChartsAdapter.ITEM_CHART_LARGE
                }
                data.add(item)
            }
            mView?.showQQCharts(data)
        }, fail = {
        })
    }

    override fun loadBaiDuPlaylist() {
        BaiduApiServiceImpl.getOnlinePlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(object : Observer<List<Playlist>> {
                    override fun onSubscribe(d: Disposable) {
                        mView.showLoading()
                    }

                    override fun onNext(result: List<Playlist>) {
                        val data = mutableListOf<GroupItemData>()
                        data.add(GroupItemData("百度音乐榜单"))
                        result.forEach {
                            val item = GroupItemData(it)
                            item.itemType = ChartsAdapter.ITEM_CHART_LARGE
                            data.add(item)
                        }
                        mView.showBaiduCharts(data)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView.hideLoading()
                        mView.showErrorInfo(e.message)
                    }

                    override fun onComplete() {
                        mView.hideLoading()
                    }
                })
    }

    /**
     * 加载网易云歌单
     */
    override fun loadTopList() {
    }

    /**
     * 加载Netease网易云歌单
     */
    fun loadNeteaseTopList() {
        BaseApiImpl.getAllNeteaseTopList(success = { result ->
            val data = mutableListOf<GroupItemData>()
            data.add(GroupItemData("网易云音乐官方榜单"))
            var flag = true
            result.forEach {
                val playlist = Playlist()
                playlist.pid = it.id
                playlist.des = it.description
                playlist.name = it.name
                playlist.coverUrl = it.cover
                playlist.playCount = it.playCount
                playlist.musicList = MusicUtils.getMusicList(it.list, Constants.NETEASE)
                playlist.type = Constants.PLAYLIST_WY_ID
                val item = GroupItemData(playlist)
                if (it.list?.size == 0) {
                    item.itemType = ChartsAdapter.ITEM_CHART
                } else {
                    item.itemType = ChartsAdapter.ITEM_CHART_LARGE
                }
                data.add(item)
            }
            mView?.showNeteaseCharts(data)
        }, fail = {
        })
    }
}
