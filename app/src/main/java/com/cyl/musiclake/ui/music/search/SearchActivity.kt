package com.cyl.musiclake.ui.music.search

import android.app.PendingIntent.getActivity
import android.preference.PreferenceManager
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
import com.cyl.musiclake.bean.HotSearchBean
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.SearchHistoryBean
import com.cyl.musiclake.bean.data.db.DaoLitepal
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseActivity
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
    private var mAdapter: SongAdapter = SongAdapter(searchResults)
    /**
     * 搜索历史
     */
    private var searchHistory: MutableList<SearchHistoryBean> = ArrayList()

    /**
     * 分页偏移量
     */
    private var mCurrentCounter = 10
    private val limit = 10
    private var mOffset = 0
    private var isSearchOnline = false

    /**
     * 过滤
     */
    var filter = mutableMapOf(SearchEngine.Filter.QQ to true,
            SearchEngine.Filter.XIAMI to true,
            SearchEngine.Filter.NETEASE to true,
            SearchEngine.Filter.BAIDU to true,
            SearchEngine.Filter.REPEAT to true,
            SearchEngine.Filter.CP to true)


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

        mAdapter.setOnItemClickListener { _, view, position ->
            if (songList.size <= position) return@setOnItemClickListener

            PlayManager.playOnline(songList[position])
            NavigationHelper.navigateToPlaying(this, view.findViewById(R.id.iv_cover))
        }
        mAdapter.setOnItemChildClickListener { _, _, position ->
            val music = songList[position]
            BottomDialogFragment.newInstance(music, Constants.PLAYLIST_SEARCH_ID).show(this)
        }
    }

    val listener = BaseQuickAdapter.RequestLoadMoreListener {
        resultListRcv.postDelayed({
            if (mCurrentCounter == 0) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd()
            } else {
                //成功获取更多数据
                queryString?.let { mPresenter?.search(it, SearchEngine.Filter.ANY, limit, mOffset) }
            }
        }, 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        initSearchFilter(menu)
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
            R.id.menu_filter_repeat -> {
                changeFilter(item, SearchEngine.Filter.REPEAT)
            }
            R.id.menu_filter_copyright -> {
                changeFilter(item, SearchEngine.Filter.CP)
            }
        }
        return true
    }

    /**
     * 改变过滤设置
     */
    private fun changeFilter(item: MenuItem, filterType: SearchEngine.Filter) {
        filter.put(key = filterType, value = !(filter[filterType] ?: true))
        item.isChecked = filter[filterType] ?: true
        if (!TextUtils.isEmpty(queryString)) {
            isSearchOnline = true
        }


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val options = prefs.getStringSet("key_search_filter", mutableSetOf())
        val searchFilters = resources.getStringArray(R.array.pref_search_filter_select)

        for (i in 0 until searchFilters.size) {
            if (searchFilters[i] == item.title) {
                if (item.isChecked) options.add((i + 1).toString())
                else options.remove((i + 1).toString())
            }
        }
        options.forEach {
            LogUtil.d("保存过滤设置 ${searchFilters[it.toInt() - 1]}");
        }
        //保存
        prefs.edit().putStringSet("key_search_filter", options).apply()
        showFilterResult()
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
     * 歌单搜索
     *
     * @param query
     */
    private fun searchPlaylistSong(query: String?) {
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
            mAdapter.setEnableLoadMore(true)
            mPresenter?.saveQueryInfo(query)
            mPresenter?.search(query, SearchEngine.Filter.ANY, limit, mOffset)
            mAdapter.setOnLoadMoreListener(listener, resultListRcv)
        }
    }

    /**
     * 显示搜索记录
     */
    override fun showSearchResult(list: MutableList<Music>) {
        if (list.size != 0) {
            mOffset++
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }
        searchResults.addAll(list)
        showFilterResult()
        isSearchOnline = false
        mCurrentCounter = mAdapter.data.size
        if (songList.size == 0) {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            showEmptyState()
        }
        LogUtil.e("search", mCurrentCounter.toString() + "--" + mCurrentCounter + "--" + mOffset)
    }

    /**
     * 显示过滤后的搜索结果
     */
    private fun showFilterResult() {
        songList.clear()
        searchResults.forEach {
            if (filter[SearchEngine.Filter.QQ] == true && it.type == Constants.QQ) {
                if (filter[SearchEngine.Filter.CP] == false && !it.isCp) {
                    songList.add(it)
                } else if (filter[SearchEngine.Filter.CP] == true) {
                    songList.add(it)
                }
            }
            if (filter[SearchEngine.Filter.BAIDU] == true && it.type == Constants.BAIDU) {
                if (filter[SearchEngine.Filter.CP] == false && !it.isCp) {
                    songList.add(it)
                } else if (filter[SearchEngine.Filter.CP] == true) {
                    songList.add(it)
                }
            }
            if (filter[SearchEngine.Filter.NETEASE] == true && it.type == Constants.NETEASE) {
                if (filter[SearchEngine.Filter.CP] == false && !it.isCp) {
                    songList.add(it)
                } else if (filter[SearchEngine.Filter.CP] == true) {
                    songList.add(it)
                }
            }
            if (filter[SearchEngine.Filter.XIAMI] == true && it.type == Constants.XIAMI) {
                if (filter[SearchEngine.Filter.CP] == false && !it.isCp) {
                    songList.add(it)
                } else if (filter[SearchEngine.Filter.CP] == true) {
                    songList.add(it)
                }
            }
            if (filter[SearchEngine.Filter.REPEAT] == true) {
                songList = removeDuplicate(songList)
            }

        }
        mAdapter.setNewData(songList)
        mAdapter.loadMoreComplete()
    }

    /**
     * 初始化过滤条件
     */
    private fun initSearchFilter(menu: Menu) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val options = prefs.getStringSet("key_search_filter", null)
        val searchFilters = resources.getStringArray(R.array.pref_search_filter_select)
        val selectFilters = mutableListOf<String>()
        if (options != null) {
            for (t in options) {
                selectFilters.add(searchFilters[t.toInt() - 1])
            }
        }

        filter[SearchEngine.Filter.QQ] = selectFilters.contains(getString(R.string.search_filter_qq))// PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_search_filter_qq", true)
        filter[SearchEngine.Filter.XIAMI] = selectFilters.contains(getString(R.string.search_filter_xiami))// PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_search_filter_xiami", true)
        filter[SearchEngine.Filter.BAIDU] = selectFilters.contains(getString(R.string.search_filter_baidu))// PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_search_filter_baidu", true)
        filter[SearchEngine.Filter.NETEASE] = selectFilters.contains(getString(R.string.search_filter_netease))// PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_search_filter_netease", true)
        filter[SearchEngine.Filter.CP] = selectFilters.contains(getString(R.string.search_filter_cp))//selectFilters.contains(getString(R.string.search_filter_cp)) PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_search_filter_cp", true)
        filter[SearchEngine.Filter.REPEAT] = selectFilters.contains(getString(R.string.search_filter_repeat))// PreferenceManager.getDefaultSharedPreferences(this).getBoolean("key_search_filter_repeat", true)

        menu.findItem(R.id.menu_filter_qq).isChecked = filter[SearchEngine.Filter.QQ] ?: true
        menu.findItem(R.id.menu_filter_xiami).isChecked = filter[SearchEngine.Filter.XIAMI] ?: true
        menu.findItem(R.id.menu_filter_netease).isChecked = filter[SearchEngine.Filter.NETEASE]
                ?: true
        menu.findItem(R.id.menu_filter_baidu).isChecked = filter[SearchEngine.Filter.BAIDU] ?: true
        menu.findItem(R.id.menu_filter_copyright).isChecked = filter[SearchEngine.Filter.CP] ?: true
        menu.findItem(R.id.menu_filter_repeat).isChecked = filter[SearchEngine.Filter.REPEAT]
                ?: true
    }

    /**
     * 移除重复歌曲
     */
    private fun removeDuplicate(list: MutableList<Music>): MutableList<Music> {
        for (i in 0 until list.size - 1) {
            for (j in list.size - 1 downTo i + 1) {
                if (list[j].title == list[i].title && list[j].artist == list[i].artist && list[j].album == list[i].album) {
                    list.removeAt(j)
                }
            }
        }
        return list
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

    /**
     * 显示历史
     */
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
