package com.cyl.musiclake.ui.music.artist.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.artist.contract.ArtistSongContract
import com.cyl.musiclake.ui.music.artist.presenter.ArtistSongsPresenter
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import java.util.*

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 专辑
 */
class ArtistSongsFragment : BaseFragment<ArtistSongsPresenter>(), ArtistSongContract.View {

    var artistID: Long = 0
    private var transitionName: String? = null
    private var title: String? = null
    private var mAdapter: SongAdapter? = null
    private var musicInfos: List<Music> = ArrayList()


    private var bottomDialogFragment: BottomDialogFragment? = null

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    public override fun initViews() {
        artistID = arguments!!.getLong(Extras.ARTIST_ID)
        transitionName = arguments!!.getString(Extras.TRANSITIONNAME)
        title = arguments?.getString(Extras.PLAYLIST_NAME)

        mAdapter = SongAdapter(musicInfos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {

        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicInfos, artistID.toString())
                mAdapter?.notifyDataSetChanged()
                activity?.let { NavigationHelper.navigateToPlaying(it, view.findViewById(R.id.iv_cover)) }
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            bottomDialogFragment = BottomDialogFragment.newInstance(musicInfos[position]).apply {
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

    override fun showSongs(songList: List<Music>) {
        musicInfos = songList
        mAdapter?.setNewData(songList)
        hideLoading()
    }

    companion object {
        fun newInstance(id: String): ArtistSongsFragment {
            val args = Bundle()
            args.putString(Extras.ARTIST_ID, id)
            val fragment = ArtistSongsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
