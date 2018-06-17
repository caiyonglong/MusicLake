package com.cyl.musiclake.ui.music.search

import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.api.MusicApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import javax.inject.Inject

/**
 * Created by yonglong on 2018/1/6.
 */

class SearchPresenter @Inject
constructor() : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    override fun search(key: String, type: SearchEngine.Filter, limit: Int, page: Int) {
        ApiManager.request(MusicApiServiceImpl
                .searchMusic(key, type, limit, page)
                .compose(mView.bindToLife()),
                object : RequestCallBack<List<Music>> {
                    override fun success(result: List<Music>) {
                        mView.showSearchResult(result)
                        mView.hideLoading()
                    }

                    override fun error(msg: String) {
                        mView.showEmptyView()
                        mView.hideLoading()
                    }
                })
    }

    override fun getSuggestions(query: String) {
//        DataSupport.select("title").where("title like ?", "%$query%")
//                .order("id desc").findAsync(SearchHistoryBean::class.java).listen(object : FindMultiCallback {
//                    override fun <T> onFinish(t: List<T>) {
//                        mView.showSearchSuggestion(t as List<SearchHistoryBean>)
//                    }
//                })
    }

    override fun saveQueryInfo(query: String) {
        DaoLitepal.addSearchInfo(query)
    }

    override fun getMusicInfo(type: Int, music: Music) {
        if (music.type === Constants.QQ || music.type === Constants.NETEASE) {
            ApiManager.request(MusicApi.getMusicInfo(music), object : RequestCallBack<Music> {
                override fun success(result: Music?) {
                    mView.showMusicInfo(type, music)
                }

                override fun error(msg: String?) {

                }
            })
        } else {
            mView.showMusicInfo(type, music)
        }
    }


}
