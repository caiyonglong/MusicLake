package com.cyl.musiclake.api

import android.support.v7.app.AppCompatActivity

import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.ToastUtils

/**
 * Created by master on 2018/4/8.
 * 封装添加到在线歌单功能
 */

object AddPlaylistUtils {

    fun getPlaylist(activity: AppCompatActivity?, music: Music?) {
        if (activity == null) return
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.resource_error))
            return
        }
        if (music.type == Constants.LOCAL || music.type == Constants.BAIDU) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.warning_add_playlist))
            return
        }
        if (!UserStatus.getstatus(activity)) {
            ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.prompt_login))
            return
        }
        ApiManager.request(PlaylistApiServiceImpl.getPlaylist(), object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                showSelectDialog(activity, result, music)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    private fun showSelectDialog(activity: AppCompatActivity?, playlists: MutableList<Playlist>, music: Music?) {
        val items = mutableListOf<String>()
        playlists.forEach {
            it.name?.let { it1 -> items.add(it1) }
        }
        MaterialDialog.Builder(activity!!)
                .title(R.string.add_to_playlist)
                .items(items)
                .itemsCallback { _, _, which, _ -> playlists[which].pid?.let { collectMusic(it, music) } }
                .build().show()
    }

    private fun collectMusic(pid: String, music: Music?) {
        ApiManager.request(PlaylistApiServiceImpl.collectMusic(pid, music!!), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.show("收藏成功")
                RxBus.getInstance().post(PlaylistEvent(Constants.PLAYLIST_LOVE_ID))
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

}
