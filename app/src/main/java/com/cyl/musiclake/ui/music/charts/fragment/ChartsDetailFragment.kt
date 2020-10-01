package com.cyl.musiclake.ui.music.charts.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.charts.ChartsAdapter
import com.cyl.musiclake.ui.music.charts.GroupItemData
import com.cyl.musiclake.ui.music.charts.activity.BaiduMusicListActivity
import com.cyl.musiclake.ui.music.charts.activity.NeteasePlaylistActivity
import com.cyl.musiclake.ui.music.charts.contract.OnlinePlaylistContract
import com.cyl.musiclake.ui.music.charts.presenter.OnlinePlaylistPresenter
import kotlinx.android.synthetic.main.fragment_charts.*

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class ChartsDetailFragment : BaseLazyFragment<OnlinePlaylistPresenter>(), OnlinePlaylistContract.View {
    //适配器
    private var mTopListAdapter: ChartsAdapter? = null
    private var chartsType: String = Constants.BAIDU

    override fun getLayoutId(): Int {
        return R.layout.fragment_charts
    }

    /**
     * 显示网易云榜单
     */
    override fun showNeteaseCharts(charts: MutableList<GroupItemData>?) {
        hideLoading()
        charts?.let { mTopListAdapter?.addNewData(it) }
    }

    /**
     * 显示QQ榜单
     */
    override fun showQQCharts(charts: MutableList<GroupItemData>?) {
        hideLoading()
        charts?.let { mTopListAdapter?.addNewData(it) }
    }

    override fun showBaiduCharts(charts: MutableList<GroupItemData>?) {
        hideLoading()
        charts?.let { mTopListAdapter?.addNewData(it) }
    }

    override fun initViews() {
        chartsType = arguments?.getString("type") ?: Constants.PLAYLIST_BD_ID
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun onLazyLoad() {
        if (mTopListAdapter == null) {
            mTopListAdapter = activity?.let { ChartsAdapter(it, mutableListOf()) }
            //初始化列表
            val layoutManager = GridLayoutManager(activity, 3)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mTopListAdapter?.data?.get(position)?.itemType != ChartsAdapter.ITEM_CHART) 3
                    else 1
                }
            }
            chartListRcv.adapter = mTopListAdapter
            chartListRcv.layoutManager = layoutManager
            mTopListAdapter?.clickListener = { position ->
                val intent = Intent()
                activity?.let {activity->
                    mTopListAdapter?.data?.get(position)?.data?.let {
                        when {
                            (it as Playlist).type == Constants.PLAYLIST_QQ_ID -> {
                                intent.setClass(activity, NeteasePlaylistActivity::class.java)
                            }
                            it.type == Constants.PLAYLIST_WY_ID -> {
                                intent.setClass(activity, NeteasePlaylistActivity::class.java)
                            }
                            else -> {
                                intent.setClass(activity, BaiduMusicListActivity::class.java)
                            }
                        }
                        intent.putExtra(Extras.PLAYLIST, it as Playlist)
                    }
                }
                startActivity(intent)
            }
        } else {
            mTopListAdapter?.cleanData()
        }
        showLoading()
        mPresenter?.loadBaiDuPlaylist()
        mPresenter?.loadQQList()
        mPresenter?.loadNeteaseTopList()
    }

    override fun loadData() {

    }

    override fun listener() {
    }

    override fun showErrorInfo(msg: String?) {
    }

    companion object {
        private val TAG = "ChartsDetailFragment"
        fun newInstance(): ChartsDetailFragment {
            return ChartsDetailFragment()
        }
    }

}