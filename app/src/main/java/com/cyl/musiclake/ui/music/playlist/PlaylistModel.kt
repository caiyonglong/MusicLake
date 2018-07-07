package com.cyl.musiclake.ui.music.playlist

import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.ToastUtils

/**
 * Des    :
 * Author : master.
 * Date   : 2018/7/5 .
 */
object PlaylistModel {
    fun loadAllPlaylist(success: (MutableList<Playlist>) -> Unit, fail: (String) -> Unit) {
        val mIsLogin = UserStatus.getstatus(MusicApp.getAppContext())
        if (!mIsLogin) {
            fail.invoke("请先登录")
            return
        }
        ApiManager.request(PlaylistApiServiceImpl.getPlaylist(), object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                result.forEach {
                    it.pid = it.id.toString()
                    it.type = 1
                }
                success.invoke(result)
            }

            override fun error(msg: String) {
                fail.invoke(msg)
            }
        })
    }

    fun deletePlaylist(playlist: Playlist) {
        ApiManager.request(playlist.pid?.let { PlaylistApiServiceImpl.deletePlaylist(it) }, object : RequestCallBack<String> {
            override fun success(result: String) {
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }
}