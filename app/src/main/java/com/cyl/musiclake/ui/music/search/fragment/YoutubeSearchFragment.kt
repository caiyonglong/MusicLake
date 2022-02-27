package com.cyl.musiclake.ui.music.search.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.HotSearchBean
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.SearchHistoryBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.search.SearchContract
import com.cyl.musiclake.ui.music.search.SearchPresenter
import com.cyl.musiclake.ui.youtube.YoutubeActivity
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class YoutubeSearchFragment : BaseLazyFragment<SearchPresenter>(), SearchContract.View {

    private var mAdapter: SongAdapter? = null

    /**
     * 歌曲列表
     */
    private val musicList = mutableListOf<Music>()

    /**
     * 分页偏移量
     */
    private var mCurrentCounter = 0

    private var searchInfo: String = ""
    private var TAG = "YoutubeSearchFragment"
    private var nextPagerToken: String = ""


    //上拉加载更多监听事件
    val listener = OnLoadMoreListener {
        recyclerView.postDelayed({
            LogUtil.d(TAG, "mCurrentCounter=$mCurrentCounter")
            if (mCurrentCounter == 0) {
                //数据全部加载完毕
                mAdapter?.loadMoreModule?.loadMoreEnd()
            } else {
                //成功获取更多数据
                searchByYouTube(searchInfo, nextPagerToken)
            }
        }, 1000)
    }

    private fun searchByYouTube(query: String, pageToken: String) {
        mPresenter?.searchByYouTube(query, pageToken, success = {
            nextPagerToken = it.nextPager ?: ""
            it.songs?.let { it1 -> showSearchResult(it1) }
        }, fail = {
            ToastUtils.show("NetWork error")
        })
    }

    companion object {
        fun newInstance(searchInfo: String?): YoutubeSearchFragment {
            val args = Bundle()
            val fragment = YoutubeSearchFragment()
            args.putString("searchInfo", searchInfo)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    override fun initViews() {
        searchInfo = arguments?.getString("searchInfo") ?: ""
        LogUtil.d(TAG, "初始化 $searchInfo")
        musicList.clear()
        showError("YouTube必须翻墙", false)
    }


    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
    }

    override fun onLazyLoad() {
        searchByYouTube(searchInfo, nextPagerToken)
    }


    /**
     * 更新歌曲列表
     */
    fun updateMusicList(songList: MutableList<Music>) {
        musicList.addAll(songList)

        if (mAdapter == null) {
            mAdapter = SongAdapter(musicList)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = mAdapter
            mAdapter?.loadMoreModule?.setOnLoadMoreListener(listener)

            mAdapter?.setOnItemClickListener { _, view, position ->
                if (musicList.size <= position) return@setOnItemClickListener
                startActivity<YoutubeActivity>("videoId" to musicList[position].mid)
            }
            mAdapter?.setOnItemChildClickListener { _, _, position ->
                //                val midusic = musicList[position]
//                BottomDialogFragmenttomDialogFragment.newInstance(music, Constants.PLAYLIST_SEARCH_ID).show(activity as AppCompatActivity)
            }

        } else {
            mAdapter?.setNewData(musicList)
        }
        hideLoading()
    }

    override fun showSearchResult(list: MutableList<Music>) {
        if (list.size != 0) {
        } else {
            mAdapter?.loadMoreModule?.loadMoreComplete()
        }
        //更新歌曲列表
        updateMusicList(list)

        mCurrentCounter = list.size
        if (musicList.size == 0) {
            mAdapter?.loadMoreModule?.loadMoreComplete()
            showEmptyState()
        }
    }

    override fun showHotSearchInfo(list: MutableList<HotSearchBean>) {
    }


    override fun showSearchHistory(list: MutableList<SearchHistoryBean>) {
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

}
