package com.cyl.musiclake.ui.music.search

import com.cyl.musicapi.netease.SearchInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.api.music.MusicApiServiceImpl
import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.bean.HotSearchBean
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.data.db.DaoLitepal
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.utils.LogUtil
import io.reactivex.Observable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by yonglong on 2018/1/6.
 */

class SearchPresenter @Inject
constructor() : BasePresenter<SearchContract.View>(), SearchContract.Presenter {
    override fun searchByType(key: String, offset: Int, type: Int) {
        ApiManager.request(NeteaseApiServiceImpl.searchMoreInfo(key, 10, offset, type),
                object : RequestCallBack<SearchInfo> {
                    override fun success(result: SearchInfo) {
                        when (type) {
                            1004 -> {

                            }
                            100 -> {

                            }
                            1000 -> {

                            }
                        }
                    }

                    override fun error(msg: String) {
                    }
                })
    }

    override fun searchLocal(key: String) {
        mView?.showLoading()
        doAsync {
            val result = DaoLitepal.searchLocalMusic(key)
            uiThread {
                mView?.hideLoading()
                mView?.showSearchResult(result)
            }
        }
    }

    override fun search(key: String, type: SearchEngine.Filter, limit: Int, page: Int) {
        mView?.showLoading()
        val observable = Observable.mergeDelayError(
                BaiduApiServiceImpl.getSearchMusicInfo(key, limit, page),
                MusicApiServiceImpl.searchMusic(key, SearchEngine.Filter.QQ, limit, page),
                MusicApiServiceImpl.searchMusic(key, SearchEngine.Filter.XIAMI, limit, page),
                MusicApiServiceImpl.searchMusic(key, SearchEngine.Filter.NETEASE, limit, page))
//                Function4<MutableList<Music>, MutableList<Music>, MutableList<Music>, MutableList<Music>, MutableList<Music>> { t1, t2, t3, t4 ->
//                    val musicList = mutableListOf<Music>()
//                    val max = Math.max(Math.max(t1.size,t2.size),Math.max(t3.size,t4.size))
////                    musicList.addAll(t1)
////                    musicList.addAll(t2)
////                    musicList.addAll(t3)
////                    musicList.addAll(t4)
//                    for (i in 0 until max) {
//                        if (t2.size > i) {
//                            t2[i].let { music ->
//                                musicList.add(music)
//                            }
//                        }
//                        if (t3.size > i) {
//                            t3[i].let { music ->
//                                musicList.add(music)
//                            }
//                        }
//
//                        if (t4.size > i) {
//                            t4[i].let { music ->
//                                musicList.add(music)
//                            }
//                        }
//                        if (t1.size > i) {
//                            t1[i].let { music ->
//                                musicList.add(music)
//                            }
//                        }
//                    }
//                    return@Function4 musicList
//        })

        ApiManager.request(observable,
                object : RequestCallBack<MutableList<Music>> {
                    override fun success(result: MutableList<Music>) {
                        LogUtil.e("searchSuccess", result.toString())
                        mView?.hideLoading()
                        mView?.showSearchResult(result)
                    }

                    override fun error(msg: String) {
                        LogUtil.e("searchFail", msg)
                        mView?.hideLoading()
                        mView?.showSearchResult(mutableListOf())
                    }
                })
    }

    override fun getHotSearchInfo() {
        if (MusicApp.hotSearchList != null) {
            mView?.showHotSearchInfo(MusicApp.hotSearchList)
            return
        } else {
            ApiManager.request(NeteaseApiServiceImpl.getHotSearchInfo(), object : RequestCallBack<MutableList<HotSearchBean>> {
                override fun success(result: MutableList<HotSearchBean>?) {
                    result?.let {
                        MusicApp.hotSearchList = it
                        mView?.showHotSearchInfo(it)
                    }
                }

                override fun error(msg: String?) {
                }
            })
        }
    }

    override fun getSearchHistory() {
        doAsync {
            val data = DaoLitepal.getAllSearchInfo()
            uiThread {
                mView?.showSearchHistory(data)
            }
        }
    }

    override fun saveQueryInfo(query: String) {
        DaoLitepal.addSearchInfo(query)
    }
}
