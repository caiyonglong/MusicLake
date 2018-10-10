package com.cyl.musiclake.ui.music.search

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.bean.HotSearchBean
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.SearchHistoryBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.utils.AnimationUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.Tools
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.acitvity_search.*
import kotlinx.android.synthetic.main.toolbar_search_layout.*
import java.util.*

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SearchActivity : BaseActivity<SearchPresenter>(), SearchContract.View {
    //搜索信息
    private var queryString: String? = null
    private var historyAdapter: SearchHistoryAdapter? = null
    private var hotSearchAdapter: HotSearchAdapter? = null

    private val searchResults = mutableListOf<Music>()
    private var mAdapter: SongAdapter = SongAdapter(searchResults)
    private var searchHistory: MutableList<SearchHistoryBean> = ArrayList()

    private var mCurrentCounter = 10
    private val limit = 10
    private var mOffset = 0
    private var isSearchOnline = false

    internal var filter: SearchEngine.Filter = SearchEngine.Filter.ANY

    private var filterItemCheckedId = -1

    override fun getLayoutResID(): Int {
        return R.layout.acitvity_search
    }

    override fun initView() {
        showSearchOnStart()
    }

    private fun showSearchOnStart() {
        searchEditText.setText(queryString)

        if (TextUtils.isEmpty(queryString) || TextUtils.isEmpty(searchEditText.text)) {
            searchToolbarContainer.translationX = 100f
            searchToolbarContainer.alpha = 0f
            searchToolbarContainer.visibility = View.VISIBLE
            searchToolbarContainer.animate().translationX(0f).alpha(1f).setDuration(200).setInterpolator(DecelerateInterpolator()).start()
        } else {
            searchToolbarContainer.translationX = 0f
            searchToolbarContainer.alpha = 1f
            searchToolbarContainer.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        mAdapter.setEnableLoadMore(true)
        //初始化列表
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        resultListRcv.layoutManager = layoutManager
        resultListRcv.adapter = mAdapter
        mAdapter.bindToRecyclerView(resultListRcv)

        //获取搜索历史
        mPresenter?.getSearchHistory()

        //获取热搜
        mPresenter?.getHotSearchInfo()
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    /**
     * 监听事件
     */
    override fun listener() {
        clearAllIv.setOnClickListener {
            DaoLitepal.clearAllSearch()
            searchHistory.clear()
            historyAdapter?.setNewData(searchHistory)
        }
        clearSearchIv.setOnClickListener {
            queryString = ""
            searchEditText.setText("")
            clearSearchIv.visibility = View.GONE
        }
        mAdapter.setOnItemClickListener { _, view, position ->
            if (searchResults.size <= position) return@setOnItemClickListener

            PlayManager.playOnline(searchResults[position])
            NavigationHelper.navigateToPlaying(this, view.findViewById(R.id.iv_cover))
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val newText = searchEditText.text.toString()
                clearSearchIv.visibility = View.VISIBLE
                if (newText.isEmpty()) {
                    mPresenter?.getSearchHistory()
                    updateHistoryPanel(true)
                    clearSearchIv.visibility = View.GONE
                } else if (!isSearchOnline) {
                    searchLocal(newText)
                }
            }
        })

        searchEditText.setOnEditorActionListener { _, _, event ->
            if (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER || event.action == EditorInfo.IME_ACTION_SEARCH)) {
                isSearchOnline = true
                search(searchEditText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

        mAdapter.setOnItemChildClickListener { _, _, position ->
            val music = searchResults[position]
            BottomDialogFragment.newInstance(music, Constants.PLAYLIST_SEARCH_ID).show(this)
        }
    }

    val listener = BaseQuickAdapter.RequestLoadMoreListener {
        resultListRcv.postDelayed({
            if (mCurrentCounter == 0) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd()
            } else {
                mOffset++
                //成功获取更多数据
                queryString?.let { mPresenter?.search(it, filter, limit, mOffset) }
            }
        }, 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_search -> {
                isSearchOnline = true
                queryString = searchEditText.text.toString().trim { it <= ' ' }
                search(queryString)
            }
            R.id.menu_filter_baidu -> {
                changeFilter(item, SearchEngine.Filter.BAIDU)
            }
            R.id.menu_filter_qq -> {
                changeFilter(item, SearchEngine.Filter.QQ)
            }
            R.id.menu_filter_xiami -> {
                changeFilter(item, SearchEngine.Filter.XIAMI)
            }
            R.id.menu_filter_netease -> {
                changeFilter(item, SearchEngine.Filter.NETEASE)
            }
        }
        return true
    }

    private fun changeFilter(item: MenuItem, filter: SearchEngine.Filter) {
        this.filter = filter
        this.filterItemCheckedId = item.itemId
        item.isChecked = !item.isChecked

        if (!TextUtils.isEmpty(queryString)) {
            isSearchOnline = true
            search(queryString)
        }
    }


    /**
     * 本地搜索
     *
     * @param query
     */
    private fun searchLocal(query: String?) {
        if (query != null && query.isNotEmpty()) {
            searchResults.clear()
            queryString = query
            updateHistoryPanel(false)
            mPresenter?.searchLocal(query)
            mAdapter.setOnLoadMoreListener(null, resultListRcv)
        }
    }

    /**
     * 在线搜索
     */
    private fun search(query: String?) {
        if (query != null && query.isNotEmpty()) {
            showLoading()
            mOffset = 0
            searchResults.clear()
            queryString = query
            searchEditText.clearFocus()
            Tools.hideInputView(searchEditText)
            updateHistoryPanel(false)
            mPresenter?.saveQueryInfo(query)
            mPresenter?.search(query, filter, limit, mOffset)
            mAdapter.setOnLoadMoreListener(listener, resultListRcv)
        }
    }

    override fun showSearchResult(list: MutableList<Music>) {
        searchResults.addAll(list)
        mAdapter.setNewData(searchResults)
        mAdapter.loadMoreComplete()
        isSearchOnline = false
        mCurrentCounter = mAdapter.data.size
        if (searchResults.size == 0) {
            showEmptyState()
        }
        LogUtil.e("search", mCurrentCounter.toString() + "--" + mCurrentCounter + "--" + mOffset)
    }

    /**
     * 设置热搜
     */
    override fun showHotSearchInfo(result: MutableList<HotSearchBean>) {
        if (result.size > 0) {
            hotSearchView.visibility = View.VISIBLE
            AnimationUtils.animateView(hotSearchView, true, 600)
        } else
            hotSearchView.visibility = View.GONE
        if (hotSearchAdapter == null) {
            hotSearchAdapter = HotSearchAdapter(result)
            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            hotSearchRcv.layoutManager = layoutManager
            hotSearchRcv.adapter = hotSearchAdapter
            hotSearchAdapter?.bindToRecyclerView(hotSearchRcv)
            hotSearchAdapter?.setOnItemClickListener { _, _, _ -> }
            hotSearchAdapter?.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.titleTv) {
                    isSearchOnline = true
                    searchEditText.setText(result[position].title)
                    searchEditText.setSelection(result[position].title?.length ?: 0)
                    search(result[position].title)
                }
            }
        } else {
            hotSearchAdapter?.setNewData(result)
        }
    }

    override fun showSearchHistory(result: MutableList<SearchHistoryBean>) {
        searchHistory = result
        if (historyAdapter == null) {
            historyAdapter = SearchHistoryAdapter(searchHistory)
            historyRcv.layoutManager = LinearLayoutManager(this)
            historyRcv.adapter = historyAdapter
            historyAdapter?.bindToRecyclerView(historyRcv)
            historyAdapter?.setOnItemLongClickListener { _, _, _ -> false }
            historyAdapter?.setOnItemClickListener { _, _, _ -> }
            historyAdapter?.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.history_search) {
                    isSearchOnline = true
                    searchEditText.setText(searchHistory[position].title)
                    searchEditText.setSelection(searchHistory[position].title?.length ?: 0)
                    search(searchHistory[position].title)
                } else if (view.id == R.id.deleteView) {
                    searchHistory[position].title?.let { DaoLitepal.deleteSearchInfo(it) }
                    historyAdapter?.remove(position)
                }
            }
        } else {
            searchHistory = result
            historyAdapter?.setNewData(result)
        }
    }

    override fun onResume() {
        super.onResume()
        Tools.hideInputView(searchEditText)
    }

    private fun updateHistoryPanel(isShow: Boolean) {
        if (isShow) {
            resultListRcv.visibility = View.GONE
            historyPanel.visibility = View.VISIBLE
        } else {
            resultListRcv.visibility = View.VISIBLE
            historyPanel.visibility = View.GONE
        }
    }
}
