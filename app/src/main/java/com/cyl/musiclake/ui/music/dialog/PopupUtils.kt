package com.cyl.musiclake.ui.music.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.data.db.Playlist

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