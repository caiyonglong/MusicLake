package com.cyl.musiclake.ui.music.local.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseLazyFragment
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.local.contract.SongsContract
import com.cyl.musiclake.ui.music.local.presenter.SongsPresenter
import com.cyl.musiclake.view.ItemDecoration

import java.util.ArrayList
import java.util.Random

import butterknife.BindView
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SongsFragment : BaseLazyFragment<SongsPresenter>(), SongsContract.View {

    private var mViewHeader: View? = null
    private var mReloadLocal: ImageView? = null
    private var mSongNumTv: TextView? = null
    private var mAdapter: SongAdapter? = null
    private val musicList = ArrayList<Music>()


    companion object {
        fun newInstance(): SongsFragment {
            val args = Bundle()
            val fragment = SongsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    override fun initViews() {
        mSwipeRefreshLayout?.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        mAdapter = SongAdapter(musicList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        recyclerView.addItemDecoration(ItemDecoration(mFragmentComponent.activity, ItemDecoration.VERTICAL_LIST))
        mAdapter?.bindToRecyclerView(recyclerView)
        initHeaderView()
        mAdapter?.addHeaderView(mViewHeader)
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        mSwipeRefreshLayout!!.setOnRefreshListener { mPresenter!!.loadSongs(false) }
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.PLAYLIST_LOCAL_ID)
                mAdapter!!.notifyDataSetChanged()
            }
        }
        mAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            val music = adapter.getItem(position) as Music?
            BottomDialogFragment.newInstance(music, Constants.OP_LOCAL)
                    .show(mFragmentComponent.activity as AppCompatActivity)
        }
    }

    override fun onLazyLoad() {
        mPresenter!!.loadSongs(false)
    }


    private fun initHeaderView() {
        mViewHeader = LayoutInflater.from(mFragmentComponent.activity).inflate(R.layout.header_local_list, null)
        val params = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        mReloadLocal = mViewHeader?.findViewById(R.id.reloadIv)
        mSongNumTv = mViewHeader?.findViewById(R.id.songNumTv)
        mViewHeader?.layoutParams = params
        mReloadLocal?.setOnClickListener { v ->
            showLoading()
            mPresenter!!.loadSongs(true)
        }
        mViewHeader?.setOnClickListener { v ->
            if (musicList.size == 0) return@setOnClickListener
            val id = Random().nextInt(musicList.size)
            PlayManager.play(id, musicList, Constants.PLAYLIST_LOCAL_ID)
        }
    }

    override fun showSongs(songList: MutableList<Music>) {
        musicList.clear()
        musicList.addAll(songList)
        mAdapter?.setNewData(songList)
        mSongNumTv?.text = getString(R.string.random_play_num, songList.size)
        hideLoading()
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
        mSwipeRefreshLayout?.isRefreshing = false
    }

    override fun setEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty)
    }
}
