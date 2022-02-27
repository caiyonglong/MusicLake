package com.cyl.musiclake.ui.music.search

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.HotSearchBean
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.SearchHistoryBean
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.main.PageAdapter
import com.cyl.musiclake.ui.music.search.fragment.SearchSongsFragment
import com.cyl.musiclake.ui.music.search.fragment.YoutubeSearchFragment
import com.cyl.musiclake.utils.AnimationUtils
import com.cyl.musiclake.utils.Tools
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search_layout.*
import java.util.*

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 描述:搜索功能
 */
class SearchActivity : BaseActivity<SearchPresenter>(), SearchContract.View {
    /**
     * 搜索信息
     */
    private var queryString: String? = null
    /**
     * 搜索结果
     */
    private val searchResults = mutableListOf<Music>()
    /**
     * 歌曲列表
     */
    private var songList = mutableListOf<Music>()

    /**
     * 适配器
     */
    private var historyAdapter: SearchHistoryAdapter? = null
    private var hotSearchAdapter: HotSearchAdapter? = null
    /**
     * 搜索历史
     */
    private var searchHistory: MutableList<SearchHistoryBean> = ArrayList()

    override fun getLayoutResID(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        showSearchAnimation()
    }

    /**
     * 显示搜索弹出动画
     */
    private fun showSearchAnimation() {
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
        //初始化列表
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//        resultListRcv.layoutManager = layoutManager
//        resultListRcv.adapter = mAdapter
//        mAdapter.bindToRecyclerView(resultListRcv)

        //获取搜索历史
        mPresenter?.getSearchHistory()

        if (!intent.getBooleanExtra("is_playlist", false)) {
            //获取热搜
            mPresenter?.getHotSearchInfo()
        } else {
        }

        //传值搜索
        if (intent.getStringExtra(Extras.SEARCH_INFO)?.isNotEmpty() == true) {
            searchEditText.setText(intent.getStringExtra(Extras.SEARCH_INFO))
        }
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
                }
            }
        })

        searchEditText.setOnEditorActionListener { _, _, event ->
            if (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER || event.action == EditorInfo.IME_ACTION_SEARCH)) {
                search(searchEditText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_search -> {
                queryString = searchEditText.text.toString().trim { it <= ' ' }
                search(queryString)
            }
        }
        return true
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
        }
    }

    /**
     * 在线搜索
     * @param query 搜索内容
     */
    private fun search(query: String?) {
        if (query != null && query.isNotEmpty()) {
            showLoading()
            searchResults.clear()
            queryString = query
            searchEditText.clearFocus()
            Tools.hideInputView(searchEditText)
            updateHistoryPanel(false)
            mPresenter?.saveQueryInfo(query)
            setupViewPager()
//            mPresenter?.search(query, SearchEngine.Filter.ANY, limit, mOffset)
        }
    }

    /**
     * 显示搜索记录
     */
    override fun showSearchResult(list: MutableList<Music>) {

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
            hotSearchAdapter?.setOnItemClickListener { _, _, _ -> }
            hotSearchAdapter?.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.titleTv) {
                    searchEditText.setText(result[position].title)
                    searchEditText.setSelection(result[position].title?.length ?: 0)
                    search(result[position].title)
                }
            }
        } else {
            hotSearchAdapter?.setNewData(result)
        }
    }

    /**
     * 显示历史
     */
    override fun showSearchHistory(result: MutableList<SearchHistoryBean>) {
        searchHistory = result
        if (historyAdapter == null) {
            historyAdapter = SearchHistoryAdapter(searchHistory)
            historyRcv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            historyRcv.adapter = historyAdapter
            historyAdapter?.setOnItemLongClickListener { _, _, _ -> false }
            historyAdapter?.setOnItemClickListener { _, _, _ -> }
            historyAdapter?.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.history_search) {
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
        //强制隐藏键盘输入法
        Tools.hideInputView(searchEditText)
    }

    /**
     * 是否显示搜索历史列表
     * @param isShow
     */
    private fun updateHistoryPanel(isShow: Boolean) {
        if (isShow) {
            tabs?.visibility = View.GONE
            viewPager?.visibility = View.GONE
            historyPanel.visibility = View.VISIBLE
        } else {
            historyPanel.visibility = View.GONE
            tabs?.visibility = View.VISIBLE
            viewPager?.visibility = View.VISIBLE
            tabs?.setupWithViewPager(viewPager)
            viewPager.currentItem = 0
        }
    }

    /**
     * 设置搜索结果列表
     */
    private fun setupViewPager() {
        val mAdapter = PageAdapter(supportFragmentManager)
        mAdapter.addFragment(SearchSongsFragment.newInstance(queryString, SearchEngine.Filter.QQ), "QQ")
        mAdapter.addFragment(SearchSongsFragment.newInstance(queryString, SearchEngine.Filter.NETEASE), "网易云")
        mAdapter.addFragment(SearchSongsFragment.newInstance(queryString, SearchEngine.Filter.XIAMI), "虾米")
        mAdapter.addFragment(SearchSongsFragment.newInstance(queryString, SearchEngine.Filter.BAIDU), "百度")
        mAdapter.addFragment(YoutubeSearchFragment.newInstance(queryString), "Youtube")
        viewPager?.adapter = mAdapter
        viewPager?.offscreenPageLimit = 4
        hideLoading()
    }
}
