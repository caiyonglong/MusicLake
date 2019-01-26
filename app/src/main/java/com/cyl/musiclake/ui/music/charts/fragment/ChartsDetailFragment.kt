package com.cyl.musiclake.ui.music.charts.fragment

import android.animation.Animator
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.charts.OnlineAdapter
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
    private var mAdapter: OnlineAdapter? = null
    private var chartsType: String = Constants.BAIDU
    private var allPlaylist = mutableListOf<Playlist>()
    private var isShowing = true

    override fun getLayoutId(): Int {
        return R.layout.fragment_charts
    }

    override fun initViews() {
        chartsType = arguments?.getString("type") ?: Constants.BAIDU

        //初始化列表
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //适配器
        mAdapter = OnlineAdapter(allPlaylist)
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)

    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun onLazyLoad() {
        showLoading()
        mPresenter?.loadBaiDuPlaylist()
        mPresenter?.loadQQList()
        mPresenter?.loadTopList()
//        when (chartsType) {
//            Constants.BAIDU -> mPresenter?.loadBaiDuPlaylist()
//            Constants.QQ -> mPresenter?.loadQQList()
//            Constants.NETEASE -> mPresenter?.loadTopList()
//        }
    }

    override fun loadData() {
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { _, _, position ->
            val intent = Intent()
            when {
                allPlaylist[position].type == Constants.PLAYLIST_BD_ID -> {
                    intent.setClass(activity, BaiduMusicListActivity::class.java)
                    intent.putExtra(Extras.PLAYLIST, allPlaylist[position])
                }
                allPlaylist[position].type == Constants.PLAYLIST_WY_ID -> {
                    intent.setClass(activity, NeteasePlaylistActivity::class.java)
                    intent.putExtra(Extras.PLAYLIST, allPlaylist[position])
                }
                allPlaylist[position].type == Constants.PLAYLIST_QQ_ID -> {
                    intent.setClass(activity, NeteasePlaylistActivity::class.java)
                    intent.putExtra(Extras.PLAYLIST, allPlaylist[position])
                }
            }
            startActivity(intent)
        }
    }

    override fun showErrorInfo(msg: String?) {
    }

    override fun showCharts(charts: MutableList<Playlist>?) {
        hideLoading()
        charts?.let { allPlaylist.addAll(it) }
        mAdapter?.setNewData(allPlaylist)
    }

    companion object {
        private val TAG = "ChartsDetailFragment"
        fun newInstance(): ChartsDetailFragment {
            return ChartsDetailFragment()
        }
    }

}