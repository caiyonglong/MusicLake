package com.cyl.musiclake.ui.music.discover

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Pair
import android.view.View
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseLazyFragment
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.ui.music.online.activity.BaiduMusicListActivity
import com.cyl.musiclake.ui.music.online.fragment.BaiduPlaylistFragment
import com.cyl.musiclake.ui.music.playlist.AllCategoryFragment
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.frag_discover.*
import java.util.*


/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class DiscoverFragment : BaseLazyFragment<DiscoverPresenter>(), DiscoverContract.View, View.OnClickListener {

    //    private var mBaiduAdapter: TopListAdapter? = null
    private var mNeteaseAdapter: TopPlaylistAdapter? = null
    private var mArtistListAdapter: TopArtistListAdapter? = null
    private var mRadioAdapter: BaiduRadioAdapter? = null
    private val playlist = ArrayList<Playlist>()
    private val artists = ArrayList<Artist>()
    private var channels: List<Playlist> = ArrayList()

    fun toCatTagAll() {
        AllCategoryFragment().apply {
            curCateName = this@DiscoverFragment.cateTagTv.text.toString()
            successListener = { result ->
                this@DiscoverFragment.updateCate(result)
            }
        }.showIt(mFragmentComponent.activity as FragmentActivity)
    }

    /**
     * 更新分类标签
     */
    private fun updateCate(name: String) {
        cateTagTv.text = name
        mPresenter?.loadNetease(name)
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_discover
    }

    override fun initViews() {
        //初始化列表
//        baiChartsRv?.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
//        //适配器
//        mBaiduAdapter = TopListAdapter(playlist)
//        baiChartsRv?.adapter = mBaiduAdapter
//        baiChartsRv?.isFocusable = false
//        baiChartsRv?.isNestedScrollingEnabled = false
//        mBaiduAdapter?.bindToRecyclerView(baiChartsRv)

        wangChartsRv?.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        //适配器
        mNeteaseAdapter = TopPlaylistAdapter(playlist)
        wangChartsRv?.adapter = mNeteaseAdapter
        wangChartsRv?.isFocusable = false
        wangChartsRv?.isNestedScrollingEnabled = false
        mNeteaseAdapter?.bindToRecyclerView(wangChartsRv)

        chartsArtistRcv?.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
        //适配器
        mArtistListAdapter = TopArtistListAdapter(artists)
        chartsArtistRcv?.adapter = mNeteaseAdapter
        chartsArtistRcv?.isFocusable = false
        chartsArtistRcv?.isNestedScrollingEnabled = false
        mArtistListAdapter?.bindToRecyclerView(chartsArtistRcv)

        //电台列表
        radioRsv?.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
        //适配器
        mRadioAdapter = BaiduRadioAdapter(channels)
        radioRsv?.adapter = mRadioAdapter
        radioRsv?.isFocusable = false
        radioRsv?.isNestedScrollingEnabled = false
        mRadioAdapter?.bindToRecyclerView(radioRsv)

        discoverContainerView.setOnScrollChangeListener { view: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            LogUtil.e("NestedScrollView", "$scrollY -----${wangChartsRv.layoutManager.height}-- ${wangChartsRv.height}")
            LogUtil.e("NestedScrollView", "$scrollY -----${discoverContainerView.layoutParams.height}-- ${discoverContainerView.height}")
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.seeAllArtistTv -> {
                activity?.let { NavigationHelper.navigateFragment(it, AllListFragment.newInstance(Constants.NETEASE_ARITIST_LIST)) }
            }
            R.id.seeAllRadioTv -> {
                activity?.let { NavigationHelper.navigateFragment(it, AllListFragment.newInstance(Constants.BAIDU_RADIO_LIST)) }
            }
//            R.id.seeAllBaiTv -> activity?.let { NavigationHelper.navigateFragment(it, BaiduPlaylistFragment.newInstance()) }
            R.id.catTag1Tv -> {
                updateCate("华语")
            }
            R.id.catTag2Tv -> {
                updateCate("流行")
            }
            R.id.catTag3Tv -> {
                updateCate("古风")
            }
            R.id.cateTagTv -> toCatTagAll()
        }
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun loadData() {
//        mPresenter?.loadBaidu()
        mPresenter?.loadNetease("全部")
        mPresenter?.loadArtists()
        mPresenter?.loadRaios()
    }

    override fun listener() {
//        mBaiduAdapter?.setOnItemClickListener { adapter, view, position ->
//            val playlist = adapter.getItem(position) as Playlist?
//            val intent = Intent(activity, BaiduMusicListActivity::class.java)
//            intent.putExtra(Extras.PLAYLIST, playlist)
//            startActivity(intent)
//        }

        mNeteaseAdapter?.setOnItemClickListener { adapter, view, position ->
            val playlist = adapter.data[position] as Playlist
            NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, playlist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
        }

        mArtistListAdapter?.setOnItemClickListener { adapter, view, position ->
            val artist = adapter.data[position] as Artist
            NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, artist, null)
        }

        mRadioAdapter?.setOnItemClickListener { _, view, position ->
            NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, channels[position], Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
        }

        catTag3Tv.setOnClickListener(this)
        cateTagTv.setOnClickListener(this)
        catTag1Tv.setOnClickListener(this)
        catTag2Tv.setOnClickListener(this)
//        seeAllBaiTv.setOnClickListener(this)
        seeAllRadioTv.setOnClickListener(this)
        seeAllArtistTv.setOnClickListener(this)
    }

    override fun onLazyLoad() {

    }

    override fun showEmptyView() {

    }

    override fun showBaiduCharts(charts: List<Playlist>) {
//        mBaiduAdapter?.setNewData(charts)
    }

    override fun showNeteaseCharts(charts: List<Playlist>) {
        mNeteaseAdapter?.setNewData(charts)
    }

    override fun showArtistCharts(charts: List<Artist>) {
        mArtistListAdapter?.setNewData(charts)
    }

    override fun showRadioChannels(channels: List<Playlist>) {
        this.channels = channels
        mRadioAdapter?.setNewData(channels)
    }

    companion object {

        private val TAG = "FoundFragment"

        fun newInstance(): DiscoverFragment {
            val args = Bundle()

            val fragment = DiscoverFragment()
            fragment.arguments = args
            return fragment
        }
    }


}