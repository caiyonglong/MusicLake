package com.cyl.musiclake.ui.music.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.data.download.TasksManager
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.music.download.FileDownloadListener
import com.cyl.musiclake.utils.*
import com.liulishuo.filedownloader.FileDownloader

/**
 * Des    :
 * Author : master.
 * Date   : 2018/7/4 .
 */
fun Context.deletePlaylist(playlist: Playlist, success: ((isHistory: Boolean) -> Unit)?, fail: (() -> Unit)?) {
    when (playlist.pid) {
        Constants.PLAYLIST_HISTORY_ID -> {
            MaterialDialog.Builder(this)
                    .title("提示")
                    .content("是否清空播放历史？")
                    .onPositive { _, _ ->
                        PlayHistoryLoader.clearPlayHistory()
                        success?.invoke(true)
                    }
                    .positiveText("确定")
                    .negativeText("取消")
                    .show()
        }
        else -> {
            MaterialDialog.Builder(this)
                    .title("提示")
                    .content("是否删除这个歌单？")
                    .onPositive { _, _ ->
                        success?.invoke(false)
                    }
                    .positiveText("确定")
                    .negativeText("取消")
                    .show()
        }
    }
}

/**
 * 下载歌曲
 */
fun Context.downloadMusic(music: Music?) {
    if (music == null) {
        ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!")
        return
    }
    if (music.type == Constants.LOCAL) {
        ToastUtils.show(MusicApp.getAppContext(), "已经本地音乐!")
        return
    }

    ApiManager.request(MusicApi.getMusicInfo(music), object : RequestCallBack<Music> {
        override fun success(result: Music) {
            LogUtil.e(javaClass.simpleName, "-----${result.uri}")
            if (!NetworkUtils.isWifiConnected(this@downloadMusic) && SPUtils.getWifiMode()) {
                MaterialDialog.Builder(this@downloadMusic)
                        .title("提示")
                        .content(R.string.download_network_tips)
                        .onPositive { _, _ ->
                            addDownloadQueue(result)
                        }
                        .positiveText("确定")
                        .negativeText("取消")
                        .show()
            } else if (result.uri != null && result.uri?.startsWith("http")!!) {
                addDownloadQueue(result)
            } else {
                ToastUtils.show(this@downloadMusic, "下载地址异常！")
            }
        }

        override fun error(msg: String) {
            ToastUtils.show(msg)
        }
    })
}

fun Context.addDownloadQueue(result: Music) {
    ToastUtils.show(getString(R.string.popup_download))
    val path = FileUtils.getMusicDir() + result.title + ".mp3"
    val task = FileDownloader.getImpl()
            .create(result.uri)
            .setPath(path)
            .setCallbackProgressTimes(100)
            .setListener(FileDownloadListener())
    TasksManager
            .addTaskForViewHolder(task)
    val model = TasksManager.addTask(task.id, result.mid!!, result.title!!, result.uri!!, path)
    model?.saveAsync()
    task.start()
}