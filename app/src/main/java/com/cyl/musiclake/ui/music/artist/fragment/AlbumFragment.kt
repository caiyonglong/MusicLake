package com.cyl.musiclake.ui.music.artist.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.AlbumAdapter
import com.cyl.musiclake.ui.music.local.contract.AlbumsContract
import com.cyl.musiclake.ui.music.local.presenter.AlbumPresenter
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import java.util.*

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 专辑
 */
class AlbumFragment : BaseFragment<AlbumPresenter>(), AlbumsContract.View {


    var artistID: Long = 0
    private var transitionName: String? = null
    private var title: String? = null
    private var mAdapter: AlbumAdapter? = null
    private var musicInfos: List<Music> = ArrayList()

    private var bottomDialogFragment: BottomDialogFragment? = null

    override fun loadData() {
        showEmptyState()
        emptyTextView?.text = "功能正在开发中..."
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    override fun showAlbums(albumList: MutableList<Album>?) {
        if (albumList?.size != 0) {
            hideLoading()
        }
        albumList?.let {
            mAdapter = AlbumAdapter(it)
            recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3, androidx.recyclerview.widget.GridLayoutManager.VERTICAL, false);
            recyclerView.adapter = mAdapter
        }
    }

    public override fun initViews() {
        artistID = arguments?.getLong(Extras.ARTIST_ID) ?: 0L
        transitionName = arguments?.getString(Extras.TRANSITIONNAME)
        title = arguments?.getString(Extras.PLAYLIST_NAME)

    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
        }
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    companion object {
        fun newInstance(id: String): AlbumFragment {
            val args = Bundle()
            args.putString(Extras.ARTIST_ID, id)
            val fragment = AlbumFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
