package com.cyl.musiclake.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.PlayHistoryLoader
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.download.TasksManager
import com.cyl.musiclake.event.LoginEvent
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.download.ui.TaskItemAdapter
import com.cyl.musiclake.ui.my.user.User
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.*
import com.liulishuo.filedownloader.FileDownloader
import org.greenrobot.eventbus.EventBus

object UIUtils {
    /**
     * 防止快速点击却换歌曲
     */
    private var lastClickTime: Long = 0

    @Synchronized
    fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < 500) {
            return true
        }
        lastClickTime = time
        return false
    }

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
                        EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_LOVE_ID))
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
fun Context.deletePlaylist(playlist: Playlist, success: ((isHistory: Boolean) -> Unit)?, fail: (() -> Unit)? = null) {
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
fun AppCompatActivity.downloadMusic(music: Music?) {
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
                        .title(R.string.warning)
                        .content(R.string.download_network_tips)
                        .onPositive { _, _ ->
                            addDownloadQueue(result)
                        }
                        .positiveText("确定")
                        .negativeText("取消")
                        .show()
            } else if (result.uri != null && result.uri?.startsWith("http")!!) {
                MaterialDialog.Builder(this@downloadMusic)
                        .title(R.string.popup_download)
                        .content(R.string.download_content, music.title)
                        .onPositive { _, _ ->
                            addDownloadQueue(result)
                        }
                        .positiveText("确定")
                        .negativeText("取消")
                        .show()
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
    DaoLitepal.saveOrUpdateMusic(result, false)
    val path = FileUtils.getMusicDir() + result.artist + " - " + result.title + ".mp3"
    val task = FileDownloader.getImpl()
            .create(result.uri)
            .setPath(path)
            .setCallbackProgressTimes(100)
            .setListener(TaskItemAdapter.taskDownloadListener)
    val model = TasksManager.addTask(task.id, result.mid, result.title, result.uri, path)
    if (model != null) {
        TasksManager.addTaskForViewHolder(task)
        task.start()
    }
}

/**
 * 更新用户Token(主要用于在线歌单)
 */
fun updateLoginToken(accessToken: String, openId: String) {
    ApiManager.request(PlaylistApiServiceImpl.login(accessToken, openId),
            object : RequestCallBack<User> {
                override fun success(result: User?) {
                    if (result != null) {
                        //保存用户信息
                        UserStatus.saveUserInfo(MusicApp.getAppContext(), result)
                    }
                    EventBus.getDefault().post(LoginEvent(true, result))
                }

                override fun error(msg: String) {
                }
            }
    )
}

/**
 * 注销登录
 */
fun logout() {
    UserStatus.clearUserInfo(MusicApp.getAppContext())
    UserStatus.saveuserstatus(MusicApp.getAppContext(), false)
    SPUtils.putAnyCommit(SPUtils.QQ_ACCESS_TOKEN, "")
    SPUtils.putAnyCommit(SPUtils.QQ_OPEN_ID, "")
    MusicApp.mTencent.logout(MusicApp.getAppContext())
    EventBus.getDefault().post(LoginEvent(false, null))
}