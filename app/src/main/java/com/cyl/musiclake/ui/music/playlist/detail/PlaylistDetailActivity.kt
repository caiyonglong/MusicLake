package com.cyl.musiclake.ui.music.playlist.detail

import android.content.Intent
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.deletePlaylist
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.edit.EditSongListActivity
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.search.PlaylistSearchActivity
import com.cyl.musiclake.ui.my.BindLoginActivity
import com.cyl.musiclake.ui.widget.ItemDecoration
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
import kotlinx.android.synthetic.main.frag_playlist_detail.*
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 歌单详情页
 */
class PlaylistDetailActivity : BaseActivity<PlaylistDetailPresenter>(), PlaylistDetailContract.View {
    /**
     * 显示异常UI
     */
    override fun showErrorTips(msg: String, hasTry: Boolean) {
        showError(msg, hasTry)
    }

    override fun showEmptyView(msg: String) {

    }

    override fun showTitle(title: String) {
        updateTitle(title)
    }

    override fun showCover(cover: String) {
        CoverLoader.loadImageView(context, coverUrl, album_art)
    }

    override fun showDescInfo(title: String) {

    }
    companion object {
        var isRemovedSongs: Boolean = false
    }

    private var mAdapter: SongAdapter? = null
    private val musicList = mutableListOf<Music>()
    private var mPlaylist: Playlist? = null
    private var mArtist: Artist? = null
    private var pid: String? = null
    private var mAlbum: Album? = null
    private var title: String? = null
    private var coverUrl: String? = null
    private var type: String? = null

    private var bottomDialogFragment: BottomDialogFragment? = null

    // 控制 CheckBox 状态
    private var isMusicListReversed: Boolean = false
    // 控制是否需要反转List
    private var needMusicListReversed: Boolean = false

    override fun getLayoutResID(): Int {
        return R.layout.frag_playlist_detail
    }

    override fun initData() {
        showLoading()
        needMusicListReversed = SPUtils.getPlaylistOrderReverse()
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

    /**
     * 歌单页标题
     */
    override fun setToolbarTitle(): String? {
        mPlaylist = intent.getParcelableExtra(Extras.PLAYLIST)
        mArtist = intent.getParcelableExtra(Extras.ARTIST)
        mAlbum = intent.getSerializableExtra(Extras.ALBUM) as Album?
        mPlaylist?.let {
            title = it.name
            pid = it.pid
            coverUrl = it.coverUrl
            CoverLoader.loadBigImageView(context, coverUrl, it.type, album_art)
        }
        mArtist?.let {
            title = it.name
            pid = it.artistId.toString()
            coverUrl = it.picUrl
            CoverLoader.loadBigImageView(context, coverUrl, it.type, album_art)
        }
        mAlbum?.let {
            title = it.name
            pid = it.albumId.toString()
            coverUrl = it.cover
            CoverLoader.loadBigImageView(context, coverUrl, it.type, album_art)
        }
        return title
    }

    override fun initView() {
        mAdapter = SongAdapter(musicList)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
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
            bottomDialogFragment = BottomDialogFragment.newInstance(musicList[position], mPlaylist?.type).apply {
                mPlaylist?.pid?.let {
                    pid = it
                }
            }
            bottomDialogFragment?.removeSuccessListener = {
                removeMusic(position)
            }
            bottomDialogFragment?.show(this)
        }
    }

    // TODO 逆序选择分开每个歌单存储
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (mPlaylist?.type != "queue" && mPlaylist?.type != "history" && mPlaylist?.type != "love") {
            menu?.getItem(4)?.subMenu?.getItem(0)?.isChecked = isMusicListReversed
        } else {
            menu?.getItem(3)?.subMenu?.getItem(0)?.isChecked = isMusicListReversed
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_playlist -> {
                LogUtil.e("action_delete_playlist")
                mPlaylist?.let {
                    deletePlaylist(it, success = {
                        if (mPlaylist?.type == Constants.PLAYLIST_HISTORY_ID) {
                            musicList.clear()
                            PlayHistoryLoader.clearPlayHistory()
                            mAdapter?.notifyDataSetChanged()
                            showEmptyState()
                            EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_HISTORY_ID, it))
                        } else if (mPresenter != null) {
                            PlaylistManagerUtils.deletePlaylist(it) {
                                onBackPress()
                            }
                        }
                    })
                }
            }
            R.id.action_rename_playlist -> {
                LogUtil.e("action_rename_playlist")
                mPlaylist?.let { playlist ->
                    MaterialDialog(this).show {
                        title(R.string.playlist_rename)
                        positiveButton(R.string.sure)
                        negativeButton(R.string.cancel)
                        input(hintRes = R.string.input_playlist, maxLength = 10, prefill = playlist.name,
                                inputType = InputType.TYPE_CLASS_TEXT) { dialog, input ->
                            LogUtil.e("=====", input.toString())
                        }
                        positiveButton {
                            val title = it.getInputField().text.toString()
                            if (title != mPlaylist?.name) {
                                mPresenter?.renamePlaylist(playlist, title)
                            }
                        }
                    }
                }
            }
            R.id.action_batch -> {
                EditSongListActivity.musicList = musicList
                EditSongListActivity.playlist = mPlaylist
                startActivity<EditSongListActivity>()
            }

            // TODO 记住排序方法
            R.id.action_order_reverse -> {
                item.isChecked = !item.isChecked
                isMusicListReversed = false
                needMusicListReversed = true
                SPUtils.setPlaylistOrderReverse(item.isChecked)
            }
            R.id.action_order_title -> {
                if (isMusicListReversed) {
                    musicList.sortByDescending { it.title }
                } else {
                    musicList.sortBy { it.title }
                }
                mAdapter?.notifyDataSetChanged()
            }
            R.id.action_order_album -> {
                if (isMusicListReversed) {
                    musicList.sortBy { it.album }
                } else {
                    musicList.sortByDescending { it.album }
                }
                mAdapter?.notifyDataSetChanged()
            }
            R.id.action_order_artist -> {
                if (isMusicListReversed) {
                    musicList.sortBy { it.artist }
                } else {
                    musicList.sortByDescending { it.artist }
                }
                mAdapter?.notifyDataSetChanged()
            }
            R.id.action_search -> {
                //设置歌单内搜索的歌曲
                PlaylistSearchActivity.musicList = musicList;
                val intent = Intent(this, PlaylistSearchActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
            }
        }
        if (needMusicListReversed) {
            needMusicListReversed = false
            // 这里不能直接等于true，要更新为 CheckBox 的 checked 状态，否则菜单上的 checked 状态会错误
            isMusicListReversed = SPUtils.getPlaylistOrderReverse()
            musicList.reverse()
            mAdapter?.notifyDataSetChanged()
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
            if (it.type == Constants.PLAYLIST_BD_ID) {
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
        if (needMusicListReversed) {
            musicList.reverse()
            needMusicListReversed = !needMusicListReversed
            isMusicListReversed = true
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
                type = musicList[0].type
            }
            CoverLoader.loadBigImageView(context, coverUrl, type, album_art)
        }
        if (musicList.size == 0) {
            showEmptyState()
        }
    }

    override fun removeMusic(position: Int) {
        musicList.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
        if (musicList.size == 0) {
            showEmptyState()
        }
    }

    override fun success(type: Int) {
        onBackPress()
    }

    /**
     * 点击重试按钮响应事件
     */
    override fun retryLoading() {
        super.retryLoading()
        if (mPlaylist?.type == Constants.PLAYLIST_WY_RECOMMEND_ID) {
            val intent = Intent(this, BindLoginActivity::class.java)
            startActivityForResult(intent, Constants.REQUEST_CODE_LOGIN)
            return
        }
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

    override fun onResume() {
        super.onResume()
        // 移出列表歌曲返回后要更新歌曲列表
        if (isRemovedSongs) {
            mAdapter?.notifyDataSetChanged()
            isRemovedSongs = false
        }
    }
}
