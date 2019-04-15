package com.cyl.musiclake.ui.music.my

import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.data.PlayHistoryLoader
import com.cyl.musiclake.bean.data.SongLoader
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.download.DownloadLoader
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.ToastUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by yonglong on 2018/1/6.
 */

class MyMusicPresenter @Inject
constructor() : BasePresenter<MyMusicContract.View>(), MyMusicContract.Presenter {
    /**
     * 更新播放历史
     */
    fun updateHistory() {
        doAsync {
            val data = PlayHistoryLoader.getPlayHistory()
            uiThread {
                mView?.showHistory(data)
            }
        }
    }

    /**
     * 更新播放历史
     */
    private fun updateLocal() {
        doAsync {
            val data = SongLoader.getLocalMusic(mView.context)
            uiThread {
                mView?.showSongs(data)
            }
        }
    }

    /**
     * 更新本地歌单
     */
    fun updateFavorite() {
        doAsync {
            val data = SongLoader.getFavoriteSong()
            uiThread {
                mView?.showLoveList(data)
            }
        }
    }


    /**
     * 更新本地歌单
     */
    fun updateDownload() {
        doAsync {
            val data = DownloadLoader.getDownloadList()
            uiThread {
                mView?.showDownloadList(data)
            }
        }
    }


    override fun loadSongs() {
        updateLocal()
        updateHistory()
        updateFavorite()
        updateDownload()
    }

    fun loadMusicLakeNotice() {
        OnlinePlaylistUtils.getMusicNoticeInfo(
                success = {
                    mView?.showNoticeInfo(it)
                }, fail = {
        }
        )
    }

    override fun loadPlaylist(playlist: Playlist?) {
        val mIsLogin = UserStatus.getLoginStatus()
        if (mIsLogin) {
            OnlinePlaylistUtils.getOnlinePlaylist(success = {
                mView?.showPlaylist(it)
            }, fail = {
                ToastUtils.show(it)
                if (OnlinePlaylistUtils.playlists.size == 0) {
                    mView?.showError(it, true)
                }
            })
        } else {
            OnlinePlaylistUtils.playlists.clear()
            mView?.showPlaylist(OnlinePlaylistUtils.playlists)
        }
    }
}
