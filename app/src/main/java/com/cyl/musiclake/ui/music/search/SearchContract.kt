package com.cyl.musiclake.ui.music.search


import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.bean.HotSearchBean
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.bean.SearchHistoryBean

interface SearchContract {

    interface View : BaseContract.BaseView {

        fun showSearchResult(list: MutableList<BaseMusicInfo>)
        fun showHotSearchInfo(list: MutableList<HotSearchBean>)
        fun showSearchHistory(list: MutableList<SearchHistoryBean>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun search(key: String, filter: SearchEngine.Filter, limit: Int, page: Int)

        fun searchByType(key: String, offset: Int, type: Int)

        fun searchLocal(key: String)

        fun getHotSearchInfo()

        fun saveQueryInfo(query: String)

        fun getSearchHistory()

    }
}
