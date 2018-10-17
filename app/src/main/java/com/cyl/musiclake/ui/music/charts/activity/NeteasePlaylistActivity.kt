package com.cyl.musiclake.ui.music.charts.activity

import android.os.Bundle

import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Extras

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class NeteasePlaylistActivity : BasePlaylistActivity() {
    override fun setEnableMore(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mPlaylist == null) {
            mPlaylist = intent.getParcelableExtra(Extras.PLAYLIST)
        }
        mPlaylist?.pid?.let {
            mPresenter?.loadPlaylist(it, mPlaylist?.type)
        }
    }

    override fun retryLoading() {
        super.retryLoading()
        mPlaylist?.pid?.let { mPresenter?.loadPlaylist(it, mPlaylist?.type) }
    }

    override fun getToolBarTitle(): String? {
        if (mPlaylist == null) {
            mPlaylist = intent.getParcelableExtra(Extras.PLAYLIST)
        }
        return mPlaylist?.name
    }

    override fun getmPlaylist(): Playlist? {
        if (mPlaylist == null) {
            mPlaylist = intent.getParcelableExtra(Extras.PLAYLIST)
        }
        return mPlaylist
    }

    companion object {
        private val TAG = "NeteasePlaylistActivity"
    }
}
