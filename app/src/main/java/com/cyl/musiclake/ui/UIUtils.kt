package com.cyl.musiclake.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.data.download.TasksManager
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.ui.music.download.FileDownloadListener
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.*
import com.cyl.musiclake.view.custom.DisplayUtils
import com.liulishuo.filedownloader.FileDownloader
import java.lang.reflect.Field

object UIUtils {
    /**
     * 改变播放模式
     */
    fun updatePlayMode(imageView: ImageView, isChange: Boolean = false) {
        try {
            var playMode = PlayQueueManager.getPlayModeId()
            if (isChange) playMode = PlayQueueManager.updatePlayMode()
            when (playMode) {
                PlayQueueManager.PLAY_MODE_LOOP -> {
                    imageView.setImageResource(R.drawable.ic_repeat)
                    if (isChange) ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.play_mode_loop))
                }
                PlayQueueManager.PLAY_MODE_REPEAT -> {
                    imageView.setImageResource(R.drawable.ic_repeat_one)
                    if (isChange) ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.play_mode_repeat))
                }
                PlayQueueManager.PLAY_MODE_RANDOM -> {
                    imageView.setImageResource(R.drawable.ic_shuffle)
                    if (isChange) ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.play_mode_random))
                }
            }
        } catch (e: Throwable) {

        }
    }

    /**
     * 收藏歌曲
     */
    fun collectMusic(imageView: ImageView, music: Music?) {
        music?.let {
            imageView.setImageResource(if (!it.isLove) R.drawable.item_favorite_love else R.drawable.item_favorite)
        }
        ValueAnimator.ofFloat(1f, 1.3f, 0.8f, 1f).apply {
            duration = 600
            addUpdateListener {
                imageView.scaleX = it.animatedValue as Float
                imageView.scaleY = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    music?.let {
                        it.isLove = SongLoader.updateFavoriteSong(it)
                        RxBus.getInstance().post(PlaylistEvent(Constants.PLAYLIST_LOVE_ID))
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }.start()

    }

}

/**
 * 删除歌单
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

/**
 * 增加到增加到下载队列
 */
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