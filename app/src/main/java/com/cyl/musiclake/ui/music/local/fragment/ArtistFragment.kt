package com.cyl.musiclake.ui.music.local.fragment

import android.os.Bundle

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.ui.music.local.adapter.ArtistAdapter
import com.cyl.musiclake.ui.music.local.contract.ArtistContract
import com.cyl.musiclake.ui.music.local.presenter.ArtistPresenter

import java.util.ArrayList

import butterknife.BindView
import com.cyl.musiclake.event.FileEvent
import com.cyl.musiclake.ui.music.my.MyMusicFragment
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class ArtistFragment : BaseLazyFragment<ArtistPresenter>(), ArtistContract.View {

    private var mAdapter: ArtistAdapter? = null
    private val artists = ArrayList<Artist>()

    /**
     * 初始化视图
     *
     * @return
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    /**
     * 初始化控件
     */
    override fun initViews() {
        mAdapter = ArtistAdapter(artists)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = mAdapter
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {}

    override fun onLazyLoad() {
        mPresenter?.loadArtists("all")
    }


    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showArtists(artists: MutableList<Artist>) {
        mAdapter?.setNewInstance(artists)
        hideLoading()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateDownloadEvent(event: FileEvent) {
        mPresenter?.loadArtists("all")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun showEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty)
    }

    companion object {

        fun newInstance(): ArtistFragment {
            val args = Bundle()
            val fragment = ArtistFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
