package com.cyl.musiclake.ui.music.charts.fragment

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.charts.ChartsAdapter
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
    private var mNeteaseAdapter: ChartsAdapter? = null
    private var mQQAdapter: ChartsAdapter? = null
    private var mBaiduAdapter: OnlineAdapter? = null
    private var chartsType: String = Constants.BAIDU

    override fun getLayoutId(): Int {
        return R.layout.fragment_charts
    }

    override fun showNeteaseCharts(charts: MutableList<Playlist>?) {
        hideLoading()

        if (mNeteaseAdapter == null) {
            mNeteaseAdapter = charts?.let { activity?.let { it1 -> ChartsAdapter(it1, it) } }

            //初始化列表
            val layoutManager = GridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
            neteaseChartsRcv.adapter = mNeteaseAdapter
            neteaseChartsRcv.layoutManager = layoutManager
            neteaseChartsRcv.isNestedScrollingEnabled = false
            mNeteaseAdapter?.clickListener = { position ->
                val intent = Intent()
                charts?.let {
                    intent.setClass(activity, NeteasePlaylistActivity::class.java)
                    intent.putExtra(Extras.PLAYLIST, charts[position])
                }
                startActivity(intent)
            }
        }

    }

    override fun showQQCharts(charts: MutableList<Playlist>?) {
        hideLoading()

        if (mQQAdapter == null) {
            mQQAdapter = charts?.let { context?.let { it1 -> ChartsAdapter(it1, it) } }

            //初始化列表
            val layoutManager = GridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
            qqChartsRcv.adapter = mQQAdapter
            qqChartsRcv.layoutManager = layoutManager
            qqChartsRcv.isNestedScrollingEnabled = false
            mQQAdapter?.clickListener = { position ->
                val intent = Intent()

                charts?.let {
                    intent.setClass(activity, NeteasePlaylistActivity::class.java)
                    intent.putExtra(Extras.PLAYLIST, charts[position])
                }
                startActivity(intent)
            }
        }
    }

    override fun showBaiduCharts(charts: MutableList<Playlist>?) {
        hideLoading()

        if (mBaiduAdapter == null) {
            mBaiduAdapter = charts?.let { OnlineAdapter(it) }

            //初始化列表
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = RecyclerView.VERTICAL
            baiduChartsRcv.adapter = mBaiduAdapter
            baiduChartsRcv.layoutManager = layoutManager
            baiduChartsRcv.isNestedScrollingEnabled = false
            mBaiduAdapter?.bindToRecyclerView(baiduChartsRcv)

            mBaiduAdapter?.setOnItemClickListener { _, _, position ->
                val intent = Intent()
                charts?.let {
                    intent.setClass(activity, BaiduMusicListActivity::class.java)
                    intent.putExtra(Extras.PLAYLIST, charts[position])
                }
                startActivity(intent)
            }
        }
    }

    override fun initViews() {
        chartsType = arguments?.getString("type") ?: Constants.BAIDU

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