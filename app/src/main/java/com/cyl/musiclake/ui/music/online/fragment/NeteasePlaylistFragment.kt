package com.cyl.musiclake.ui.music.online.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.cyl.musicapi.BaseApiImpl
import com.cyl.musicapi.bean.ListItem
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.base.BaseFragment
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.ui.music.online.activity.NeteasePlaylistActivity
import com.cyl.musiclake.ui.music.online.adapter.OnlineAdapter
import com.cyl.musiclake.utils.LogUtil

import java.util.ArrayList

import butterknife.BindView
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class NeteasePlaylistFragment : BaseFragment<BasePresenter<BaseContract.BaseView>>() {
    //适配器
    private var mAdapter: OnlineAdapter? = null
    private var neteaseLists: MutableList<Playlist> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    public override fun initViews() {
        //初始化列表
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //适配器
        mAdapter = OnlineAdapter(neteaseLists)
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
    }

    override fun initInjector() {

    }

    override fun loadData() {
        val observable = PlaylistApiServiceImpl.getNeteaseRank(IntArray(21) { i -> i }, 3)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                neteaseLists = result
                mAdapter?.setNewData(result)
            }

            override fun error(msg: String) {
                println(msg)
            }
        })
    }

    override fun listener() {
        swipe_refresh?.setOnRefreshListener { swipe_refresh?.isRefreshing = false }
        mAdapter?.setOnItemClickListener { _, _, position ->
            val intent = Intent(activity, NeteasePlaylistActivity::class.java)
            intent.putExtra("title", neteaseLists[position].name)
            intent.putExtra("id", neteaseLists[position].pid)
            startActivity(intent)
        }
    }

    companion object {

        private val TAG = "NeteasePlaylistFragment"
        fun newInstance(): NeteasePlaylistFragment {
            val args = Bundle()
            val fragment = NeteasePlaylistFragment()
            fragment.arguments = args
            return fragment
        }
    }

}