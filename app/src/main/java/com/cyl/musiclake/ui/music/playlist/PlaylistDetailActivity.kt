package com.cyl.musiclake.ui.music.playlist

import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.deletePlaylist
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.edit.EditSongListActivity
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.view.ItemDecoration
import kotlinx.android.synthetic.main.frag_playlist_detail.*
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class PlaylistDetailActivity : BaseActivity<PlaylistDetailPresenter>(), PlaylistDetailContract.View, BottomDialogFragment.RemoveMusicListener {
    private var mAdapter: SongAdapter? = null
    private val musicList = mutableListOf<Music>()
    private var mPlaylist: Playlist? = null
    private var mArtist: Artist? = null
    private var pid: String? = null
    private var mAlbum: Album? = null
    private var title: String? = null
    private var coverUrl: String? = null

    private var bottomDialogFragment: BottomDialogFragment? = null

    override fun getLayoutResID(): Int {
        return R.layout.frag_playlist_detail
    }

    override fun initData() {
        showLoading()
        mPlaylist?.let {
            if (it.musicList.size > 0) {
                showPlaylistSongs(it.musicList)
            } else {
                mPresenter?.loadPlaylistSongs(it)
            }
        }
        mArtist?.let {
            mPresenter?.loadArtistSongs(it)
        }
        mAlbum?.let {
            mPresenter?.loadAlbumSongs(it)
        }
    }

    override fun setToolbarTitle(): String? {
        mPlaylist = intent.getParcelableExtra(Extras.PLAYLIST)
        mArtist = intent.getParcelableExtra(Extras.ARTIST)
        mAlbum = intent.getSerializableExtra(Extras.ALBUM) as Album?
        mPlaylist?.let {
            title = it.name
            pid = it.pid
            coverUrl = it.coverUrl
            CoverLoader.loadImageView(context, coverUrl, album_art)
        }
        mArtist?.let {
            title = it.name
            pid = it.artistId.toString()
            coverUrl = it.picUrl
            CoverLoader.loadImageView(context, coverUrl, album_art)
        }
        mAlbum?.let {
            title = it.name
            pid = it.albumId.toString()
            coverUrl = it.cover
            CoverLoader.loadImageView(context, coverUrl, album_art)
        }
        return title
    }

    override fun initView() {
        mAdapter = SongAdapter(musicList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ItemDecoration(this, ItemDecoration.VERTICAL_LIST))
        recyclerView.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
        fab.setOnClickListener { PlayManager.play(0, musicList, pid) }
    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
                when {
                    mPlaylist != null -> PlayManager.play(position, musicList, mPlaylist?.pid)
                    mArtist != null -> PlayManager.play(position, musicList, mArtist?.artistId.toString())
                    mAlbum != null -> PlayManager.play(position, musicList, mAlbum?.albumId.toString())
                }
                mAdapter?.notifyDataSetChanged()
                NavigationHelper.navigateToPlaying(this, view.findViewById(R.id.iv_cover))
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            val music = musicList[position]
            if (mPlaylist != null && mPlaylist?.type == Playlist.PT_MY) {
                bottomDialogFragment = BottomDialogFragment.newInstance(music, Constants.OP_PLAYLIST)
                bottomDialogFragment?.removeMusicListener = this
                bottomDialogFragment?.position = position
                bottomDialogFragment?.show(this)
            } else {
                bottomDialogFragment = BottomDialogFragment.newInstance(music, Constants.OP_ONLINE)
                bottomDialogFragment?.position = position
                bottomDialogFragment?.show(this)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_delete_playlist -> {
                LogUtil.e("action_delete_playlist")
                mPlaylist?.let {
                    deletePlaylist(it, success = { isHistory ->
                        if (isHistory) {
                            musicList.clear()
                            PlayHistoryLoader.clearPlayHistory()
                            mAdapter?.notifyDataSetChanged()
                            showEmptyState()
                            EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_HISTORY_ID))
                        } else if (mPresenter != null) {
                            OnlinePlaylistUtils.deletePlaylist(it) { _ ->
                                onBackPress()
                            }
                        }
                    })
                }
            }
            R.id.action_rename_playlist -> {
                LogUtil.e("action_rename_playlist")
                mPlaylist?.let {
                    MaterialDialog.Builder(this)
                            .title(R.string.playlist_rename)
                            .positiveText(R.string.sure)
                            .negativeText(R.string.cancel)
                            .inputRangeRes(2, 10, R.color.red)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input("输入歌单名", it.name, false) { dialog, input -> LogUtil.e("=====", input.toString()) }
                            .onPositive { dialog, _ ->
                                val title = dialog.inputEditText?.text.toString()
                                if (title != mPlaylist?.name) {
                                    mPresenter?.renamePlaylist(it, title)
                                }
                            }
                            .positiveText(R.string.sure)
                            .negativeText(R.string.cancel)
                            .show()
                }
            }
            R.id.action_batch -> {
                startActivity<EditSongListActivity>(Extras.SONG_LIST to musicList)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun onBackPress() {
        this.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_playlist_detail, menu)
        if (mPlaylist == null) {
            menu.removeItem(R.id.action_rename_playlist)
            menu.removeItem(R.id.action_delete_playlist)
        }
        mPlaylist?.let {
            when (it.pid) {
                Constants.PLAYLIST_HISTORY_ID -> {
                    menu.removeItem(R.id.action_rename_playlist)
                }
                Constants.PLAYLIST_LOVE_ID -> {
                    menu.removeItem(R.id.action_rename_playlist)
                    menu.removeItem(R.id.action_delete_playlist)
                }
            }
            if (it.type == Playlist.PT_BAIDU) {
                //百度电台
                menu.removeItem(R.id.action_rename_playlist)
                menu.removeItem(R.id.action_delete_playlist)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }


    override fun showPlaylistSongs(songList: MutableList<Music>?) {
        hideLoading()
        songList?.let {
            musicList.addAll(songList)
        }
        mAdapter?.setNewData(musicList)
        if (coverUrl == null) {
            mPlaylist?.coverUrl?.let {
                coverUrl = it
            }
            mArtist?.picUrl?.let {
                coverUrl = it
            }
            if (musicList.size > 0 && coverUrl == null) {
                coverUrl = musicList[0].coverUri
            }
            CoverLoader.loadImageView(context, coverUrl, album_art)
        }
        if (musicList.size == 0) {
            showEmptyState()
        }
    }

    override fun removeMusic(position: Int) {
        musicList.removeAt(position)
        mAdapter?.notifyDataSetChanged()
        if (musicList.size == 0) {
            showEmptyState()
        }
    }

    override fun success(type: Int) {
        onBackPress()
    }

    /**
     * 移除歌手歌曲
     */
    override fun remove(position: Int, music: Music?) {
        OnlinePlaylistUtils.disCollectMusic(pid, music) {
            removeMusic(position)
        }
    }

    /**
     * 点击重试按钮响应事件
     */
    override fun retryLoading() {
        super.retryLoading()
        showLoading()
        mPlaylist?.let {
            if (it.musicList.size > 0) {
                showPlaylistSongs(it.musicList)
            } else {
                mPresenter?.loadPlaylistSongs(it)
            }

        }
        mArtist?.let {
            mPresenter?.loadArtistSongs(it)
        }
        mAlbum?.let {
            mPresenter?.loadAlbumSongs(it)
        }
    }
}
