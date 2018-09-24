package com.cyl.musiclake.ui.music.online.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.edit.EditSongListActivity
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.online.PlaylistContract
import com.cyl.musiclake.ui.music.online.PlaylistPresenter
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.FormatUtil
import com.cyl.musiclake.utils.SizeUtils
import kotlinx.android.synthetic.main.activity_online_playlist.*
import org.jetbrains.anko.startActivity

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
abstract class BasePlaylistActivity : BaseActivity<PlaylistPresenter>(), PlaylistContract.View {

    private var mViewHeader: View? = null
    private var mIvBackground: ImageView? = null
    private var mIvCover: ImageView? = null
    private var mTvTitle: TextView? = null
    private var mTvDate: TextView? = null
    private var mTvDesc: TextView? = null

    var mPlaylist: Playlist? = null
    var mAdapter: SongAdapter? = null
    var musicList = mutableListOf<Music>()

    var mOffset = 0
    var mCurrentCounter = 0
    var TOTAL_COUNTER = 0
    val limit = 10

    abstract fun getToolBarTitle(): String?

    override fun getLayoutResID(): Int {
        return R.layout.activity_online_playlist
    }

    abstract fun getmPlaylist(): Playlist?

    override fun initView() {
        initHeaderView()
    }

    override fun setToolbarTitle(): String? {
        return getToolBarTitle()
    }

    abstract fun setEnableMore(): Boolean

    override fun initData() {
        mAdapter = SongAdapter(musicList)
        mAdapter?.setEnableLoadMore(setEnableMore())
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
        showHeaderInfo(getmPlaylist())
    }


    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicList, mPlaylist?.name + mPlaylist?.pid)
                mAdapter?.notifyDataSetChanged()
                NavigationHelper.navigateToPlaying(this, view.findViewById(R.id.iv_cover))
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            val music = musicList[position]
            BottomDialogFragment.newInstance(music, mPlaylist?.type).show(this)
        }
    }

    private fun initHeaderView() {
        mViewHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null)
        val params = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this, 150f))
        mViewHeader?.layoutParams = params
        mIvCover = mViewHeader?.findViewById(R.id.iv_cover)
        mTvTitle = mViewHeader?.findViewById(R.id.tv_title)
//        mTvDate = mViewHeader?.findViewById(R.id.tv_update_date)
        mTvDesc = mViewHeader?.findViewById(R.id.tv_comment)
        mIvBackground = mViewHeader?.findViewById(R.id.coverBgIv)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_batch -> {
                startActivity<EditSongListActivity>(Extras.SONG_LIST to musicList)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_playlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showHeaderInfo(playlist: Playlist?) {
        if (playlist != null) {
            CoverLoader.loadBitmap(this, playlist.coverUrl) {
                mIvCover?.setImageBitmap(it)
                mIvBackground?.setImageDrawable(CoverLoader.createBlurredImageFromBitmap(it))
            }
            mTvTitle?.text = playlist.name
            if (playlist.date != 0L) {
                mTvDate?.visibility = View.VISIBLE
                mTvDate?.text = getString(R.string.recent_update, FormatUtil.distime(playlist.date))
            } else {
                mTvDate?.visibility = View.GONE
            }
            mTvDesc?.text = playlist.des
            mAdapter?.setHeaderView(mViewHeader, 0)
        }
    }

    override fun showPlayList(playlist: Playlist) {
        mAdapter?.setEnableLoadMore(false)
        mPlaylist = playlist
        musicList = playlist.musicList
        mAdapter?.setNewData(playlist.musicList)
    }

    override fun showOnlineMusicList(musicList: MutableList<Music>) {
        this.musicList.addAll(musicList)
        mAdapter?.setNewData(this.musicList)
        mOffset += limit
        mCurrentCounter = mAdapter?.data?.size ?: 0
        TOTAL_COUNTER = mOffset
        mAdapter?.loadMoreComplete()
    }

}
