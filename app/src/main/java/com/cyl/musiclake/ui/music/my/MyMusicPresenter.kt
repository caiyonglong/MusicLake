package com.cyl.musiclake.ui.music.my

import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.data.PlaylistLoader
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.VideoLoader
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.download.DownloadLoader
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
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
     * 更新下载歌曲
     */
    fun updateDownload() {
        doAsync {
            val data = DownloadLoader.getDownloadList()
            uiThread {
                mView?.showDownloadList(data)
            }
        }
    }

    /**
     * 更新下载歌曲
     */
    fun updateLocalVideo() {
        doAsync {
            val musicList = VideoLoader.getAllLocalVideos(mView.context)
            uiThread {
                mView?.showVideoList(musicList)
            }
        }
    }


    override fun loadSongs() {
        updateLocal()
        updateHistory()
        updateFavorite()
        updateLocalVideo()
        updateDownload()
    }

    fun loadMusicLakeNotice() {
        PlaylistManagerUtils.getMusicNoticeInfo(
                success = {
                    mView?.showNoticeInfo(it)
                }, fail = {
        })
    }

    /**
     * 获取在线歌单
     */
    override fun loadPlaylist(playlist: Playlist?) {
        if (UserStatus.getLoginStatus() && UserStatus.getTokenStatus()) {
            val mIsLogin = UserStatus.getLoginStatus()
            if (mIsLogin) {
                PlaylistManagerUtils.getOnlinePlaylist(success = {
                    mView?.showPlaylist(it)
                }, fail = {
                    ToastUtils.show(it)
                    if (PlaylistManagerUtils.playlists.size == 0) {
                        mView?.showError(it, true)
                    }
                })
            } else {
                PlaylistManagerUtils.playlists.clear()
                mView?.showPlaylist(PlaylistManagerUtils.playlists)
            }
        } else {
            mView?.showPlaylist(mutableListOf())
        }
        loadWyUserPlaylist()
    }

    /**
     * 获取本地歌单
     */
    fun loadLocalPlaylist() {
        doAsync {
            val playlist = PlaylistLoader.getAllPlaylist()
            playlist.forEach {
                it.pid?.let { it1 ->
                    val list = PlaylistLoader.getMusicForPlaylist(it1)
                    it.total = list.size.toLong()
                }
            }
            uiThread {
                mView?.showLocalPlaylist(playlist)
            }
        }
    }


    /**
     * 加载网易云歌单
     */
    fun loadWyUserPlaylist() {
        val uid = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_UID, "")
        LogUtil.d("MyMusic", "uid = $uid")
        if (uid.isEmpty()) return
        val observable = NeteaseApiServiceImpl.getUserPlaylist(uid = uid)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showWyPlaylist(result)
            }

            override fun error(msg: String) {
                mView?.showWyPlaylist(mutableListOf())
            }
        })
    }
}
