package com.cyl.musiclake.ui.music.my

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.cyl.musiclake.R
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

    private var playlists = mutableListOf<Playlist>()
    private var mAdapter: PlaylistAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.frag_local
    }

    public override fun initViews() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = false

        playlistRcv.layoutManager = LinearLayoutManager(context)
        playlistRcv.isNestedScrollingEnabled = false

        mAdapter = PlaylistAdapter(playlists)
        playlistRcv.adapter = mAdapter
        mAdapter?.bindToRecyclerView(playlistRcv)

        //加载通知
        mPresenter?.loadMusicLakeNotice()
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
                ToastUtils.show(getString(R.string.prompt_login))
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
    }


    override fun showSongs(songList: MutableList<Music>) {
        localView.setSongsNum(songList.size, 0)
        localView.setOnItemClickListener { view, position ->
            if (view.id == R.id.iv_play) {
                PlayManager.play(0, songList, Constants.PLAYLIST_LOCAL_ID)
            } else {
                toFragment(position)
            }
        }
    }

    override fun showPlaylist(playlists: MutableList<Playlist>) {
        this.playlists = playlists
        mAdapter?.setNewData(playlists)
        if (playlists.size == 0) {
            showEmptyState()
            mAdapter?.setEmptyView(R.layout.view_playlist_empty)
        }
        hideLoading()
    }

    override fun showHistory(musicList: MutableList<Music>) {
        historyView.setSongsNum(musicList.size, 1)
        historyView.setOnItemClickListener { view, position ->
            if (view.id == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_HISTORY_ID)
            } else {
                toFragment(position)
            }
        }
    }

    override fun showLoveList(musicList: MutableList<Music>) {
        favoriteView.setSongsNum(musicList.size, 2)
        favoriteView.setOnItemClickListener { view, position ->
            if (view.id == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_LOVE_ID)
            } else {
                toFragment(position)
            }
        }
    }

    override fun showDownloadList(musicList: MutableList<Music>) {
        downloadView.setSongsNum(musicList.size, 3)
        downloadView.setOnItemClickListener { view, position ->
            if (view.id == R.id.iv_play) {
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
