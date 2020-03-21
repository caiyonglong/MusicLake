package com.cyl.musiclake.ui.music.artist.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyl.musiclake.R
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.music.lake.musiclib.MusicPlayerManager
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.artist.contract.ArtistSongContract
import com.cyl.musiclake.ui.music.artist.presenter.ArtistSongsPresenter
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.edit.EditSongListActivity
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import kotlinx.android.synthetic.main.frag_artist_songs.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 专辑
 */
class ArtistSongsFragment : BaseFragment<ArtistSongsPresenter>(), ArtistSongContract.View {

    var artistID: String? = "0"
    private var mAdapter: SongAdapter? = null
    private var baseMusicInfoInfoInfos: MutableList<BaseMusicInfo> = ArrayList()

    private var bottomDialogFragment: BottomDialogFragment? = null

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_artist_songs
    }

    public override fun initViews() {
        mAdapter = SongAdapter(baseMusicInfoInfoInfos)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)

        //播放按钮
        playIv?.setOnClickListener {
            MusicPlayerManager.getInstance().playMusic(baseMusicInfoInfoInfos, 0)
        }
        //播放按钮
        menuIv.setOnClickListener {
        }
        //批量管理
        menuIv.setOnClickListener {
            EditSongListActivity.musicList = baseMusicInfoInfoInfos
            startActivity<EditSongListActivity>()
        }
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
                MusicPlayerManager.getInstance().playMusic(baseMusicInfoInfoInfos, position)
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            bottomDialogFragment = BottomDialogFragment.newInstance(baseMusicInfoInfoInfos[position]).apply {
            }
            bottomDialogFragment?.show(activity as AppCompatActivity)
        }
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty)
    }

    override fun showSongs(songList: MutableList<BaseMusicInfo>) {
        baseMusicInfoInfoInfos = songList
        mAdapter?.setNewData(songList)
        hideLoading()
    }

    companion object {
        fun newInstance(): ArtistSongsFragment {
            val args = Bundle()
            val fragment = ArtistSongsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
