package com.cyl.musiclake.ui.music.online.activity

import android.os.Bundle
import android.os.Parcelable

import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.music.online.base.BasePlaylistActivity

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class NeteasePlaylistActivity : BasePlaylistActivity() {

    var playlist: Playlist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter?.loadPlaylist(intent.getStringExtra("id"), this)
    }

    override fun retryLoading() {
        super.retryLoading()
        playlist?.pid?.let { mPresenter?.loadPlaylist(it, this) }
    }

    override fun getToolBarTitle(): String? {
        playlist = intent.getParcelableExtra(Extras.PLAYLIST)
        return playlist?.name
    }

    override fun getmPlaylist(): Playlist? {
        playlist = intent.getParcelableExtra(Extras.PLAYLIST)
        return playlist
    }

    companion object {
        private val TAG = "BaiduMusicListActivity"
    }
}
