package com.cyl.musiclake.ui.music.local.fragment


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.common.Extras
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.local.contract.FolderSongsContract
import com.cyl.musiclake.ui.music.local.presenter.FolderSongPresenter
import com.cyl.musiclake.ui.music.mv.BaiduMvDetailActivity
import com.cyl.musiclake.ui.widget.ItemDecoration

import java.util.ArrayList

import org.jetbrains.anko.support.v4.startActivity

class LocalVideoFragment : BaseFragment<FolderSongPresenter>(), FolderSongsContract.View {

    val mRecyclerView by lazy { rootView?.findViewById<RecyclerView>(R.id.recyclerView) }

    private var mAdapter: SongAdapter? = null
    private var path: String? = null
    private var baseMusicInfoInfoList: List<BaseMusicInfo> = ArrayList()


    override fun showEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty, mRecyclerView)
    }


    override fun loadData() {
        showLoading()
        path?.let { mPresenter?.loadSongs(it) }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview
    }

    public override fun initViews() {
        mAdapter = SongAdapter(baseMusicInfoInfoList)
        mRecyclerView?.layoutManager = LinearLayoutManager(activity)
        mRecyclerView?.adapter = mAdapter
        mRecyclerView?.addItemDecoration(ItemDecoration(mFragmentComponent.activity, ItemDecoration.VERTICAL_LIST))
        mAdapter?.bindToRecyclerView(mRecyclerView)
        setHasOptionsMenu(true)
    }

    override fun getToolBarTitle(): String? {
        if (arguments != null) {
            path = arguments?.getString(Extras.FOLDER_PATH)
        }
        return context?.getString(R.string.item_video)
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                startActivity<BaiduMvDetailActivity>(Extras.VIDEO_PATH to baseMusicInfoInfoList[position].uri,
                        Extras.MV_TITLE to baseMusicInfoInfoList[position].title)
            }
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position -> BottomDialogFragment.newInstance(baseMusicInfoInfoList[position]).show(mFragmentComponent.activity as AppCompatActivity) }
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showSongs(baseMusicInfoInfoList: List<BaseMusicInfo>) {
        this.baseMusicInfoInfoList = baseMusicInfoInfoList
        mAdapter?.setNewData(baseMusicInfoInfoList)
        hideLoading()
    }

    companion object {

        fun newInstance(path: String): LocalVideoFragment {

            val args = Bundle()
            args.putString(Extras.FOLDER_PATH, path)

            val fragment = LocalVideoFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
