package com.cyl.musiclake.ui

import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.NoticeInfo
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.MyPlaylistEvent
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by master on 2018/4/8.
 * 封装添加到在线歌单功能
 */

object OnlinePlaylistUtils {
    /**
     * 保存当前歌单列表
     */
    var playlists = mutableListOf<Playlist>()

    /**
     * 删除当前歌单
     */
    fun deletePlaylist(playlist: Playlist, success: (String) -> Unit) {
        ApiManager.request(playlist.pid?.let { PlaylistApiServiceImpl.deletePlaylist(it) }, object : RequestCallBack<String> {
            override fun success(result: String) {
                success.invoke(result)
                ToastUtils.show(result)
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_DELETE, playlist))
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    /**
     * 获取在线歌单歌曲
     */
    fun getPlaylistMusic(playlist: Playlist, success: (Playlist) -> Unit) {
        ApiManager.request(PlaylistApiServiceImpl.getMusicList(playlist.pid!!), object : RequestCallBack<MutableList<Music>> {
            override fun error(msg: String?) {
                success.invoke(playlist)
            }

            override fun success(musicList: MutableList<Music>) {
                if (musicList.size > 0) {
                    playlist.coverUrl = musicList[0].coverUri
                }
                playlist.musicList = musicList
                success.invoke(playlist)
            }
        })
    }


    /**
     * 获取最新通知消息
     */
    fun getMusicNoticeInfo(success: (NoticeInfo) -> Unit, fail: (String) -> Unit) {
        ApiManager.request(PlaylistApiServiceImpl.getMusicLakeNotice(), object : RequestCallBack<NoticeInfo> {
            override fun success(result: NoticeInfo) {
                val noticeCode = SPUtils.getAnyByKey(SPUtils.SP_KEY_NOTICE_CODE, -1)
                if (noticeCode < result.id) {
                    success.invoke(result)
                }
            }

            override fun error(msg: String) {
                fail.invoke(msg)
            }
        })
    }


    /**
     * 获取在线歌单
     */
    fun getOnlinePlaylist(success: (MutableList<Playlist>) -> Unit, fail: (String) -> Unit) {
        ApiManager.request(PlaylistApiServiceImpl.getPlaylist(), object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                playlists.clear()
                playlists.addAll(result)
                success.invoke(playlists)
            }

            override fun error(msg: String) {
                fail.invoke(msg)
            }
        })
    }

    /**
     * 批量歌曲添加到在线歌单
     */
    fun addToPlaylist(activity: AppCompatActivity?, musics: MutableList<Music>?) {
        if (activity == null) return
        if (!UserStatus.getLoginStatus()) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.prompt_login))
            return
        }
        if (musics == null || musics.size == 0) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.no_song_to_add))
            return
        }
        musics.forEach {
            if (it.type == Constants.LOCAL || it.type == Constants.BAIDU) {
                ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.warning_add_playlist))
                return
            }
        }
        getOnlinePlaylist(success = {
            showSelectDialog(activity, it, musicList = musics)
        }, fail = {
            ToastUtils.show(it)
        })
    }

    /**
     * 获取在线歌单
     */
    fun addToPlaylist(activity: AppCompatActivity?, music: Music?) {
        if (activity == null) return
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.resource_error))
            return
        }
        if (music.type == Constants.LOCAL || music.type == Constants.BAIDU) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.warning_add_playlist))
            return
        }
        if (!UserStatus.getLoginStatus()) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.prompt_login))
            return
        }
        getOnlinePlaylist(success = {
            showSelectDialog(activity, it, music)
        }, fail = {
            ToastUtils.show(it)
        })
    }

    /**
     * 显示歌列表
     */
    private fun showSelectDialog(activity: AppCompatActivity, playlists: MutableList<Playlist>, music: Music? = null, musicList: MutableList<Music>? = null) {
        val items = mutableListOf<String>()
        playlists.forEach {
            it.name?.let { it1 -> items.add(it1) }
        }
        MaterialDialog.Builder(activity)
                .title(R.string.add_to_playlist)
                .items(items)
                .itemsCallback { _, _, which, _ ->
                    if (playlists[which].pid == null) {
                        playlists[which].pid = playlists[which].id.toString()
                    }
                    if (musicList != null) {
                        collectBatch2Music(playlists[which], musicList)
                    } else {
                        collectMusic(playlists[which], music)
                    }
                }
                .build().show()
    }

    /**
     * 歌曲添加到在线歌单，同步
     * 目前支持网易，虾米，qq
     */
    private fun collectMusic(playlist: Playlist, music: Music?) {
        ApiManager.request(PlaylistApiServiceImpl.collectMusic(playlist.pid.toString(), music!!), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.show(result)
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, playlist))
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    /**
     * 歌曲批量添加到在线歌单，同类型
     * 目前支持网易，虾米，qq
     */
    fun collectBatchMusic(playlist: Playlist, vendor: String, musicList: MutableList<Music>?, success: (() -> Unit)? = null) {
        ApiManager.request(PlaylistApiServiceImpl.collectBatchMusic(playlist.pid.toString(), vendor, musicList), object : RequestCallBack<String> {
            override fun success(result: String?) {
                ToastUtils.show(result)
                success?.invoke()
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, playlist))
            }

            override fun error(msg: String?) {
                ToastUtils.show(msg)
            }

        })
    }

    /**
     * 歌曲批量添加到在线歌单，不同类型
     * 目前支持网易，虾米，qq
     */
    private fun collectBatch2Music(playlist: Playlist, musicList: MutableList<Music>?) {
        ApiManager.request(PlaylistApiServiceImpl.collectBatch2Music(playlist.pid.toString(), musicList), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.show(result)
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, playlist))
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    /**
     * 新建歌单
     */
    fun createPlaylist(name: String, success: (Playlist) -> Unit) {
        val mIsLogin = UserStatus.getLoginStatus()
        if (mIsLogin) {
            ApiManager.request(
                    PlaylistApiServiceImpl.createPlaylist(name),
                    object : RequestCallBack<Playlist> {
                        override fun success(result: Playlist) {
                            success.invoke(result)
                        }

                        override fun error(msg: String) {
                            ToastUtils.show(msg)
                        }
                    }
            )
        } else {
            ToastUtils.show(MusicApp.getAppContext().getString(R.string.un_login_tips))
        }
    }

    /**
     * 取消收藏
     */
    fun disCollectMusic(pid: String?, music: Music?, success: () -> Unit) {
        if (pid == null) return
        if (music == null) return
        ApiManager.request(PlaylistApiServiceImpl.disCollectMusic(pid, music), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.show(result)
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, Playlist().apply { this.pid = pid }))
                success.invoke()
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }
}
