package com.cyl.musiclake.ui.music.search

import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.api.MusicApiServiceImpl
import com.cyl.musiclake.api.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.HotSearchBean
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by yonglong on 2018/1/6.
 */

class SearchPresenter @Inject
constructor() : BasePresenter<SearchContract.View>(), SearchContract.Presenter {
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
        ApiManager.request(MusicApiServiceImpl
                .searchMusic(key, type, limit, page)
                .compose(mView?.bindToLife()),
                object : RequestCallBack<MutableList<Music>> {
                    override fun success(result: MutableList<Music>) {
                        mView?.showSearchResult(result)
                        mView?.hideLoading()
                    }

                    override fun error(msg: String) {
                        mView?.showSearchResult(mutableListOf())
                        mView?.hideLoading()
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
