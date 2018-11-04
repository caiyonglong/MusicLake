package com.cyl.musiclake.ui.music.local.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.edit.EditSongListActivity
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.local.contract.SongsContract
import com.cyl.musiclake.ui.music.local.presenter.SongsPresenter
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import kotlinx.android.synthetic.main.header_local_list.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SongsFragment : BaseLazyFragment<SongsPresenter>(), SongsContract.View {

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
        return R.layout.frag_local_song
    }

    override fun initViews() {
        mAdapter = SongAdapter(musicList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
        initHeaderView()
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.PLAYLIST_LOCAL_ID)
                mAdapter?.notifyDataSetChanged()
            }
        }
        mAdapter?.setOnItemChildClickListener { adapter, _, position ->
            val music = adapter.getItem(position) as Music?
            BottomDialogFragment.newInstance(music, Constants.PLAYLIST_LOCAL_ID)
                    .apply {
                        removeSuccessListener = {
                            this@SongsFragment.mAdapter?.notifyItemRemoved(position)
                        }
                    }
                    .show(mFragmentComponent.activity as AppCompatActivity)
        }
        menuIv.setOnClickListener {
            startActivity<EditSongListActivity>(Extras.SONG_LIST to musicList)
        }
    }

    override fun onLazyLoad() {
        mPresenter?.loadSongs(false)
    }


    private fun initHeaderView() {
        reloadIv?.setOnClickListener { v ->
            showLoading()
            mPresenter?.loadSongs(true)
        }
        iconIv.setOnClickListener { v ->
            if (musicList.size == 0) return@setOnClickListener
            val id = Random().nextInt(musicList.size)
            PlayManager.play(id, musicList, Constants.PLAYLIST_LOCAL_ID)
        }
    }

    override fun showSongs(songList: MutableList<Music>) {
        musicList.clear()
        musicList.addAll(songList)
        mAdapter?.setNewData(songList)
        songNumTv?.text = getString(R.string.random_play_num, songList.size)
        hideLoading()
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun setEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty)
    }
}
