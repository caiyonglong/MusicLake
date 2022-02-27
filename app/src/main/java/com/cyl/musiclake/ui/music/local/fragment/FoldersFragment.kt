package com.cyl.musiclake.ui.music.local.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.FolderInfo
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.edit.EditSongListActivity
import com.cyl.musiclake.ui.music.local.adapter.FolderAdapter
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.local.contract.FoldersContract
import com.cyl.musiclake.ui.music.local.presenter.FoldersPresenter
import kotlinx.android.synthetic.main.frag_local_song.*
import kotlinx.android.synthetic.main.header_local_list.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by D22434 on 2018/1/8.
 */

class FoldersFragment : BaseLazyFragment<FoldersPresenter>(), FoldersContract.View {
    private var mAdapter: FolderAdapter? = null
    private var mSongAdapter: SongAdapter? = null
    var folderInfos = mutableListOf<FolderInfo>()
    var songList = mutableListOf<Music>()
    var curFolderName: String? = null

    override fun getLayoutId(): Int {
        return R.layout.frag_folder
    }

    override fun initViews() {
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onLazyLoad() {
        mPresenter?.loadFolders()
    }

    override fun showEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty)
    }

    override fun showFolders(folderInfos: MutableList<FolderInfo>) {
        if (mAdapter == null) {
            this.folderInfos = folderInfos
            mAdapter = FolderAdapter(folderInfos)
            recyclerView?.adapter = mAdapter
            mAdapter?.setOnItemClickListener { adapter, _, position ->
                //打开文件夹
                val folderInfo = adapter.getItem(position) as FolderInfo?
                activity?.let { it1 ->
                    if (folderInfo != null) {
                        NavigationHelper.navigateToFolderSongs(it1, folderInfo)
                    }
                }
            }
        } else {
            recyclerView?.adapter = mAdapter
            mAdapter?.setNewData(folderInfos)
        }
    }

    override fun showSongs(musicList: MutableList<Music>?) {
        songList.clear()
        musicList?.let { songList = it }
        if (mSongAdapter == null) {
            mSongAdapter = musicList?.let {
                SongAdapter(it)
            }
            recyclerView?.adapter = mSongAdapter
            mSongAdapter?.setOnItemClickListener { adapter, view, position ->
                if (view.id != R.id.iv_more) {
                    PlayManager.play(position, songList, Constants.PLAYLIST_DOWNLOAD_ID + curFolderName)
                    mSongAdapter?.notifyDataSetChanged()
                }
            }
            mSongAdapter?.setOnItemChildClickListener { _, _, position ->
                BottomDialogFragment.newInstance(songList[position]).apply {
                    removeSuccessListener = {
                        this@FoldersFragment.mAdapter?.notifyItemRemoved(position)
                    }
                }.show(mFragmentComponent.activity as AppCompatActivity)

            }
        } else {
            recyclerView?.adapter = mSongAdapter
            mSongAdapter?.setNewData(songList)
        }
    }


    companion object {
        fun newInstance(): FoldersFragment {
            val args = Bundle()
            val fragment = FoldersFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
