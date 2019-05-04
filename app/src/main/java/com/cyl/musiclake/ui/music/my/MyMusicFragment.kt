package com.cyl.musiclake.ui.music.my

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.NoticeInfo
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.data.PlaylistLoader
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.event.*
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.dialog.CreatePlaylistDialog
import com.cyl.musiclake.ui.music.playlist.PlaylistAdapter
import com.cyl.musiclake.ui.music.playlist.PlaylistManagerActivity
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.frag_local.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton


/**
 * Created by Monkey on 2015/6/29.
 */
class MyMusicFragment : BaseFragment<MyMusicPresenter>(), MyMusicContract.View {


    private var localPlaylists = mutableListOf<Playlist>()
    private var playlists = mutableListOf<Playlist>()
    private var wyPlaylists = mutableListOf<Playlist>()

    private var playlistTag = Constants.PLAYLIST_CUSTOM_ID
    private var mAdapter: PlaylistAdapter? = null

    override fun getLayoutId(): Int {
        return com.cyl.musiclake.R.layout.frag_local
    }


    override fun showNoticeInfo(notice: NoticeInfo) {
        alert {
            isCancelable = false
            title = notice.title
            message = notice.message
            if (notice.dismiss) {
                yesButton {
                    SPUtils.putAnyCommit(SPUtils.SP_KEY_NOTICE_CODE, notice.id)
                }
            }
        }.show()
    }

    public override fun initViews() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = false

        playlistRcv.layoutManager = LinearLayoutManager(context)
        playlistRcv.isNestedScrollingEnabled = false

        mAdapter = PlaylistAdapter(playlists)
        playlistRcv.adapter = mAdapter
        mAdapter?.bindToRecyclerView(playlistRcv)
        mAdapter?.setEmptyView(com.cyl.musiclake.R.layout.view_playlist_empty)

        //加载通知
        mPresenter?.loadMusicLakeNotice()

//        val linearLayout = playlistTab.getChildAt(0) as LinearLayout
//        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
//        linearLayout.dividerPadding = 40 // 设置分割线的pandding
//        linearLayout.dividerDrawable = context?.let { ContextCompat.getDrawable(it, com.cyl.musiclake.R.drawable.bg_divider_tab) }

        playlistTab.addTab(playlistTab.newTab().setText("本地歌单").setTag(Constants.PLAYLIST_LOCAL_ID))
        playlistTab.addTab(playlistTab.newTab().setText("自建歌单").setTag(Constants.PLAYLIST_CUSTOM_ID))
        playlistTab.addTab(playlistTab.newTab().setText("网易歌单").setTag(Constants.PLAYLIST_WY_ID))

        playlistTab.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.tag?.let {
                    playlistTag = it.toString()
                }
                when (p0?.tag) {
                    Constants.PLAYLIST_LOCAL_ID -> {
                        mAdapter?.setNewData(localPlaylists)
                        playlistAddIv.visibility = View.VISIBLE
                    }
                    Constants.PLAYLIST_CUSTOM_ID -> {
                        mAdapter?.setNewData(playlists)
                        playlistAddIv.visibility = View.VISIBLE
                    }
                    Constants.PLAYLIST_WY_ID -> {
                        mAdapter?.setNewData(wyPlaylists)
                        playlistAddIv.visibility = View.INVISIBLE
                    }
                }
                if (mAdapter?.data?.size == 0) {
                    mAdapter?.setEmptyView(com.cyl.musiclake.R.layout.view_playlist_empty)
                }
            }
        })
    }


    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        playlistAddIv.setOnClickListener {
            if (UserStatus.getLoginStatus()) {
                val dialog = CreatePlaylistDialog.newInstance()
                dialog.show(childFragmentManager, TAG_CREATE)
            } else {
                ToastUtils.show(getString(com.cyl.musiclake.R.string.prompt_login))
            }
        }
        playlistManagerIv.setOnClickListener {
            val intent = Intent(activity, PlaylistManagerActivity::class.java)
            startActivity(intent)
        }


    }

    override fun loadData() {
        mPresenter?.loadSongs()
        if (UserStatus.getLoginStatus() && UserStatus.getTokenStatus()) {
            mPresenter?.loadPlaylist()
        }
        mPresenter?.loadWyUserPlaylist()
    }


    override fun showSongs(songList: MutableList<Music>) {
        localView.setSongsNum(songList.size, 0)
        localView.setOnItemClickListener { view, position ->
            if (view.id == com.cyl.musiclake.R.id.iv_play) {
                PlayManager.play(0, songList, Constants.PLAYLIST_LOCAL_ID)
            } else {
                toFragment(position)
            }
        }
    }

    override fun showLocalPlaylist(playlists: MutableList<Playlist>) {

    }

    /**
     * 显示网易歌单
     */
    override fun showWyPlaylist(playlists: MutableList<Playlist>) {
        this.wyPlaylists = playlists
//        mAdapter?.setNewData(wyPlaylists)
//        if (playlists.size == 0) {
//            showEmptyState()
//            mAdapter?.setEmptyView(com.cyl.musiclake.R.layout.view_playlist_empty)
//        }
//        hideLoading()
    }

    /**
     * 显示在线歌单
     */
    override fun showPlaylist(playlists: MutableList<Playlist>) {
        this.playlists = playlists
        mAdapter?.setNewData(playlists)
        if (playlists.size == 0) {
            mAdapter?.setEmptyView(com.cyl.musiclake.R.layout.view_playlist_empty)
        }
        hideLoading()
    }

    override fun showHistory(musicList: MutableList<Music>) {
        historyView.setSongsNum(musicList.size, 1)
        historyView.setOnItemClickListener { view, position ->
            if (view.id == com.cyl.musiclake.R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_HISTORY_ID)
            } else {
                toFragment(position)
            }
        }
    }

    override fun showLoveList(musicList: MutableList<Music>) {
        favoriteView.setSongsNum(musicList.size, 2)
        favoriteView.setOnItemClickListener { view, position ->
            if (view.id == com.cyl.musiclake.R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_LOVE_ID)
            } else {
                toFragment(position)
            }
        }
    }

    override fun showDownloadList(musicList: MutableList<Music>) {
        downloadView.setSongsNum(musicList.size, 3)
        downloadView.setOnItemClickListener { view, position ->
            if (view.id == com.cyl.musiclake.R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_DOWNLOAD_ID)
            } else {
                toFragment(position)
            }
        }
    }

    private fun toFragment(position: Int) {
        when (position) {
            0 -> NavigationHelper.navigateToLocalMusic(mFragmentComponent.activity, null)
            1 -> NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, PlaylistLoader.getHistoryPlaylist(), null)
            2 -> NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, PlaylistLoader.getFavoritePlaylist(), null)
            3 -> NavigationHelper.navigateToDownload(mFragmentComponent.activity)
        }
    }

    override fun showError(message: String, showRetryButton: Boolean) {
        super.showError(message, showRetryButton)
    }

    override fun retryLoading() {
        super.retryLoading()
        if (UserStatus.getLoginStatus() && UserStatus.getTokenStatus()) {
            mPresenter?.loadPlaylist()
        } else {
            ToastUtils.show("请先登录")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMetaChangedEvent(event: MetaChangedEvent) {
        if (mPresenter != null) {
            mPresenter?.updateHistory()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoChangedEvent(event: LoginEvent) {
        if (mPresenter != null) {
            mPresenter?.loadPlaylist()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlaylistChangedEvent(event: PlaylistEvent) {
        when (event.type) {
            Constants.PLAYLIST_LOCAL_ID -> mPresenter?.loadSongs()
            Constants.PLAYLIST_CUSTOM_ID -> mPresenter?.loadPlaylist()
            Constants.PLAYLIST_LOVE_ID -> mPresenter?.updateFavorite()
            Constants.PLAYLIST_HISTORY_ID -> mPresenter?.updateHistory()
            Constants.PLAYLIST_DOWNLOAD_ID -> mPresenter?.updateDownload()
        }
    }

    /**
     * 更新在线歌单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlaylistChangedEvent(event: MyPlaylistEvent) {
        when (event.operate) {
            Constants.PLAYLIST_ADD -> mPresenter?.loadPlaylist()
            Constants.PLAYLIST_DELETE -> {
                for (i in 0 until playlists.size) {
                    if (playlists[i].pid == event.playlist?.pid) {
                        playlists.removeAt(i)
                        mAdapter?.notifyItemRemoved(i)
                        return
                    }
                }
            }
            Constants.PLAYLIST_UPDATE -> mPresenter?.loadPlaylist()
            Constants.PLAYLIST_RENAME -> {
                for (i in 0 until playlists.size) {
                    if (playlists[i].pid == event.playlist?.pid) {
                        playlists[i].name = event.playlist?.name
                        mAdapter?.notifyItemChanged(i)
                        return
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateDownloadEvent(event: DownloadEvent) {
        mPresenter?.updateDownload()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    companion object {

        private val TAG_CREATE = "create_playlist"

        fun newInstance(): MyMusicFragment {
            val args = Bundle()
            val fragment = MyMusicFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
