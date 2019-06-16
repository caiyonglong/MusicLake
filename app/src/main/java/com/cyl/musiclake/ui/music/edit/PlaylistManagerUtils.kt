package com.cyl.musiclake.ui.music.edit

import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.api.playlist.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.NoticeInfo
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.data.PlaylistLoader
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.MyPlaylistEvent
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by master on 2018/4/8.
 * 封装添加到在线歌单功能
 */

object PlaylistManagerUtils {
    /**
     * 保存当前歌单列表
     */
    var playlists = mutableListOf<Playlist>()

    /**
     * 删除当前歌单
     */
    fun deletePlaylist(playlist: Playlist, success: (String) -> Unit) {
        if (playlist.type == Constants.PLAYLIST_LOCAL_ID) {
            doAsync {
                PlaylistLoader.deletePlaylist(playlist)
                uiThread {
                    EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_DELETE, playlist))
                    success.invoke("歌单删除成功")
                }
            }
            return
        }

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
     * 添加歌曲到在线歌单
     */
    fun addToPlaylist(activity: AppCompatActivity?, music: Music?) {
        music?.let {
            addToPlaylist(activity, mutableListOf(it))
        }
    }

    /**
     * 批量歌曲添加到歌单
     * @param musics 选择的歌曲
     */
    fun addToPlaylist(activity: AppCompatActivity?, musics: MutableList<Music>?) {
        if (activity == null) return
        //选择歌曲为空，则提示
        if (musics == null || musics.size == 0) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.no_song_to_add))
            return
        }
        showPlaylistSelectDialog(activity, callBack = {
            when (it) {
                "本地歌单" -> {
                    addToLocalPlaylist(activity, musics)
                }
                "在线歌单" -> {
                    addToOnlinePlaylist(activity, musics)
                }
                "网易云歌单" -> {
                    ToastUtils.show("暂不支持此功能")
                }
            }
        })
    }

    /**
     * 批量添加到本地歌单
     */
    private fun addToLocalPlaylist(activity: AppCompatActivity?, musics: MutableList<Music>?) {
        if (activity == null) return
        //选择歌曲为空，则提示
        if (musics == null || musics.size == 0) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.no_song_to_add))
            return
        }
        //显示本地歌单列表
        showLocalPlaylistDialog(activity, musicList = musics)
    }

    /**
     * 批量添加到在线歌单
     */
    private fun addToOnlinePlaylist(activity: AppCompatActivity?, musics: MutableList<Music>?) {
        if (activity == null) return
        //选择歌曲为空，则提示
        if (musics == null || musics.size == 0) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.no_song_to_add))
            return
        }
        //是否登录成功
        if (!UserStatus.getLoginStatus()) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.prompt_login))
            return
        }
        //过滤本地，百度等服务器不支持的歌曲
        musics.forEach {
            if (it.type == Constants.LOCAL && it.type == Constants.BAIDU) {
                ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.warning_add_playlist))
                showLocalPlaylistDialog(activity, musicList = musics)
                return
            }
        }
        //获取在线歌单列表，显示对话框
        getOnlinePlaylist(success = {
            showSelectDialog(activity, it, musicList = musics)
        }, fail = {
            ToastUtils.show(it)
        })
    }

    /**
     * 显示本地歌单列表
     */
    private fun showLocalPlaylistDialog(activity: AppCompatActivity, music: Music? = null, musicList: MutableList<Music>? = null) {
        doAsync {
            val playlist = PlaylistLoader.getAllPlaylist()
            uiThread {
                if (playlist.size==0){
                    ToastUtils.show("暂无本地歌单，请先创建哦😄")
                }else{
                    showSelectDialog(activity, playlist, music, musicList)
                }
            }
        }
    }

    /**
     * 显示所有的歌单列表
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
     * 显示歌单类型选择列表
     */
    private fun showPlaylistSelectDialog(activity: AppCompatActivity, callBack: ((String) -> Unit)) {
        val items = mutableListOf("本地歌单", "在线歌单", "网易云歌单")
        MaterialDialog.Builder(activity)
                .title(R.string.add_to_playlist)
                .items(items)
                .itemsCallback { _, _, which, _ ->
                    callBack.invoke(items[which])
                }
                .build().show()
    }

    /**
     * 歌曲添加到在线歌单，同步
     * 目前支持网易，虾米，qq
     */
    private fun collectMusic(playlist: Playlist, music: Music?) {
        if (playlist.type == Constants.PLAYLIST_LOCAL_ID) {
            playlist.pid?.let {
                PlaylistLoader.addToPlaylist(it, music!!)
                ToastUtils.show("成功添加到本地歌单")
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, playlist))
            }
        } else if (playlist.type == Constants.PLAYLIST_CUSTOM_ID) {
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
    }

    /**
     *
     * 歌曲批量添加到在线歌单，同类型
     * 目前支持网易，虾米，qq
     */
    fun collectBatchMusic(playlist: Playlist, vendor: String, musicList: MutableList<Music>?, success: (() -> Unit)? = null) {
        if (playlist.type == Constants.PLAYLIST_LOCAL_ID) {
            playlist.pid?.let {
                PlaylistLoader.addMusicList(it, musicList!!)
                success?.invoke()
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, playlist))
            }
        } else if (playlist.type == Constants.PLAYLIST_CUSTOM_ID) {
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
    }

    /**
     * 在线歌单
     * 歌曲批量添加到在线歌单，不同类型
     * 目前支持网易，虾米，qq
     */
    private fun collectBatch2Music(playlist: Playlist, musicList: MutableList<Music>?) {
        if (playlist.type == Constants.PLAYLIST_LOCAL_ID) {
            playlist.pid?.let {
                PlaylistLoader.addMusicList(it, musicList!!)
                EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_UPDATE, playlist))
            }
        } else if (playlist.type == Constants.PLAYLIST_CUSTOM_ID) {
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
    }

    /**
     *
     * 在线歌单
     * 新建歌单
     * @param name 歌单名
     * @param type 歌单类型名
     */
    fun createPlaylist(name: String, type: String, success: (Playlist) -> Unit) {
        val mIsLogin = UserStatus.getLoginStatus()
        if (type == Constants.PLAYLIST_CUSTOM_ID) {
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
        } else {
            val pid = System.currentTimeMillis().toString()
            doAsync {
                val success = PlaylistLoader.createPlaylist(pid, Constants.PLAYLIST_LOCAL_ID, name)
                uiThread {
                    if (success) {
                        EventBus.getDefault().post(MyPlaylistEvent(Constants.PLAYLIST_ADD, Playlist()))
                    }
                }
            }
        }
    }

    /**
     * 在线歌单的删除歌单（取消收藏）
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
