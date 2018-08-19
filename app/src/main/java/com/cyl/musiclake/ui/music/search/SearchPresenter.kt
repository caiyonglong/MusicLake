package com.cyl.musiclake.ui.music.search

import com.cyl.musiclake.api.MusicApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.bean.Music
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
                        mView?.hideLoading()
                    }
                })
    }

    override fun getSuggestions(query: String) {
        doAsync {
            val data = DaoLitepal.getAllSearchInfo(query)
            uiThread {
                mView?.showSearchSuggestion(data)
            }
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
