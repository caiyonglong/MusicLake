package com.cyl.musiclake.ui.download.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.download.TasksManagerModel
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter

import java.util.ArrayList

import butterknife.BindView
import com.cyl.musiclake.event.PlaylistEvent
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by yonglong on 2016/11/26.
 */

class DownloadedFragment : BaseFragment<DownloadPresenter>(), DownloadContract.View {

    private var mAdapter: SongAdapter? = null
    private var isCache: Boolean? = null
    private var musicList: MutableList<Music> = ArrayList()

    override fun listener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.PLAYLIST_DOWNLOAD_ID)
                mAdapter?.notifyDataSetChanged()
            }
        }
        mAdapter?.setOnItemChildClickListener { adapter, _, position ->
            val music = adapter.getItem(position) as Music?
            BottomDialogFragment.newInstance(music, Constants.PLAYLIST_DOWNLOAD_ID).apply {
                removeSuccessListener = {
                    EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_DOWNLOAD_ID))
                    this@DownloadedFragment.mAdapter?.notifyItemRemoved(position)
                }
            }.show(mFragmentComponent.activity as AppCompatActivity)
        }
    }

    override fun loadData() {
        isCache = arguments?.getBoolean(Constants.KEY_IS_CACHE)
        mPresenter?.loadDownloadMusic(isCache!!)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    public override fun initViews() {
        mAdapter = SongAdapter(musicList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }


    override fun showErrorInfo(msg: String) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_download, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_all) {
            if (mPresenter != null) {
                mPresenter?.deleteAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showSongs(musicList: MutableList<Music>) {
        this.musicList = musicList
        mAdapter?.setNewInstance(musicList)
        if (musicList.isEmpty()) {
            mAdapter?.setEmptyView(R.layout.view_song_empty)
        }
    }

    override fun showDownloadList(modelList: List<TasksManagerModel>) {

    }

    companion object {

        fun newInstance(isCache: Boolean?): DownloadedFragment {
            val args = Bundle()
            args.putBoolean(Constants.KEY_IS_CACHE, isCache!!)
            val fragment = DownloadedFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
