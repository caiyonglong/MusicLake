package com.cyl.musiclake.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.ImageView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musicapi.netease.LoginInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApi
import com.cyl.musiclake.api.music.netease.NeteaseApiServiceImpl
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import com.cyl.musiclake.api.playlist.PlaylistApiServiceImpl
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.data.PlayHistoryLoader
import com.cyl.musiclake.bean.data.SongLoader
import com.cyl.musiclake.bean.data.db.DaoLitepal
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.LoginEvent
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.ui.download.TasksManager
import com.cyl.musiclake.ui.download.ui.TaskItemAdapter
import com.cyl.musiclake.ui.main.MainActivity
import com.cyl.musiclake.ui.my.user.User
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.*
import com.liulishuo.filedownloader.FileDownloader
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


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
                        EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_LOVE_ID, null))
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
fun Context.deletePlaylist(playlist: Playlist, success: (() -> Unit)?, fail: (() -> Unit)? = null) {
    when (playlist.pid) {
        Constants.PLAYLIST_HISTORY_ID -> {
            MaterialDialog.Builder(this)
                    .title("提示")
                    .content("是否清空播放历史？")
                    .onPositive { _, _ ->
                        PlayHistoryLoader.clearPlayHistory()
                        success?.invoke()
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
                        success?.invoke()
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
fun AppCompatActivity.downloadMusic(music: Music?, isCache: Boolean = false) {
    if (music == null) {
        ToastUtils.show(MusicApp.getAppContext(), getString(R.string.download_empty_error))
        return
    }
    if (music.type == Constants.LOCAL) {
        ToastUtils.show(MusicApp.getAppContext(), getString(R.string.download_local_error))
        return
    }
    if (!music.isDl && !isCache) {
        ToastUtils.show(MusicApp.getAppContext(), getString(R.string.download_ban))
        return
    }
    ApiManager.request(MusicApi.getMusicDownloadUrl(music, isCache), object : RequestCallBack<String> {
        override fun success(result: String) {
            LogUtil.e(javaClass.simpleName, "-----$result")
            /**
             * 当前activity 销毁时 不显示
             */
            if (this@downloadMusic.isDestroyed || this@downloadMusic.isFinishing) return

            if (!NetworkUtils.isWifiAvaliable(MusicApp.getAppContext()) && SPUtils.getWifiMode()) {
                showTipsDialog(this@downloadMusic, R.string.download_network_tips) {
                    music.uri = result
                    addDownloadQueue(music, isCache = isCache)
                }
                return
            }
            if (result.isNotEmpty() && result.startsWith("http")) {
                val titleId = if (isCache) R.string.popup_cache else R.string.popup_download
                MaterialDialog.Builder(this@downloadMusic)
                        .title(titleId)
                        .content(R.string.download_content, music.title)
                        .onPositive { _, _ ->
                            music.uri = result
                            addDownloadQueue(music, isCache = isCache)
                        }
                        .positiveText(R.string.sure)
                        .negativeText(R.string.cancel)
                        .show()
                return
            }
            ToastUtils.show(getString(R.string.download_error))
        }

        override fun error(msg: String) {
            ToastUtils.show(msg)
        }
    })
}


/**
 * 删除歌曲
 */
fun AppCompatActivity.deleteMusic(music: Music?) {
    if (music == null) {
        ToastUtils.show(MusicApp.getAppContext(), getString(R.string.download_empty_error))
        return
    }
    if (music.type != Constants.LOCAL) {
        ToastUtils.show(MusicApp.getAppContext(), getString(R.string.delete_local_song_error))
        return
    }
    doAsync {
        val result = FileUtils.delFile(music.uri)
        uiThread {
            if (result) {
                ToastUtils.show(getString(R.string.delete_song_success))
            }
        }
    }
}

/**
 * 批量下载
 */
fun AppCompatActivity.downloadBatchMusic(downloadList: MutableList<Music>) {
    val tips = if (downloadList.size == 0) {
        getString(R.string.download_list_empty_tips)
    } else {
        getString(R.string.download_list_tips, downloadList.size.toString())
    }
    showTipsDialog(this@downloadBatchMusic, tips) {
        if (downloadList.size == 0) {
            return@showTipsDialog
        }
        if (!NetworkUtils.isWifiAvaliable(this@downloadBatchMusic) && SPUtils.getWifiMode()) {
            showTipsDialog(this@downloadBatchMusic, R.string.download_network_tips) {
                downloadList.forEach {
                    addDownloadQueue(it, true)
                }
                ToastUtils.show(getString(R.string.download_add_success))
            }
        } else {
            downloadList.forEach {
                addDownloadQueue(it, true)
            }
            ToastUtils.show(getString(R.string.download_add_success))
        }
    }
}

/**
 * 批量删除歌曲
 */
fun AppCompatActivity.deleteSingleMusic(music: Music?, success: (() -> Unit)? = null) {
    if (this.isFinishing || this.isDestroyed) return
    if (music == null) {
        showTipsDialog(this@deleteSingleMusic, R.string.delete_local_song_empty)
        return
    }
    showTipsDialog(this@deleteSingleMusic, R.string.delete_local_song) {
        doAsync {
            SongLoader.removeSong(music)
            uiThread {
                ToastUtils.show(MusicApp.getAppContext().getString(R.string.delete_song_success))
                EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_LOCAL_ID))
                success?.invoke()
            }
        }
    }
}

/**
 * 批量删除歌曲
 */
fun AppCompatActivity.deleteLocalMusic(deleteList: MutableList<Music>, success: (() -> Unit)? = null) {
    if (deleteList.size == 0) {
        showTipsDialog(this@deleteLocalMusic, R.string.delete_local_song_empty)
        return
    }
    val tips = if (deleteList.size == 0) {
        getString(R.string.delete_local_song_empty)
    } else {
        getString(R.string.delete_local_song_list, deleteList.size)
    }
    showTipsDialog(this@deleteLocalMusic, tips) {
        doAsync {
            SongLoader.removeMusicList(deleteList)
            uiThread {
                ToastUtils.show(getString(R.string.delete_song_success))
                EventBus.getDefault().post(PlaylistEvent(Constants.PLAYLIST_LOCAL_ID))
                success?.invoke()
            }
        }
    }
}


/**
 * 提示对话框显示tip
 */
fun showTipsDialog(context: AppCompatActivity, content: String, success: (() -> Unit)? = null) {
    if (context.isDestroyed || context.isFinishing) return
    MaterialDialog.Builder(context)
            .title(R.string.prompt)
            .content(content)
            .onPositive { _, _ ->
                success?.invoke()
            }
            .positiveText(R.string.sure)
            .negativeText(R.string.cancel)
            .show()
}

/**
 * 对话框显示tip
 */
fun showTipsDialog(context: AppCompatActivity, content: Int, success: (() -> Unit)? = null) {
    if (context.isDestroyed || context.isFinishing) return
    MaterialDialog.Builder(context)
            .title(R.string.warning)
            .content(content)
            .onPositive { _, _ ->
                success?.invoke()
            }
            .positiveText(R.string.sure)
            .negativeText(R.string.cancel)
            .show()
}


/**
 * 增加到增加到下载队列
 */
fun Context.addDownloadQueue(result: Music, isBatch: Boolean = false, isCache: Boolean = false) {
    LogUtil.e(javaClass.simpleName, "-----${result.uri}")

    if (result.uri == null) {
        ToastUtils.show(this@addDownloadQueue, "${result.title} 下载地址异常！")
        return
    }
    if (!isBatch) {
        ToastUtils.show(getString(R.string.download_add_success))
    }
    DaoLitepal.saveOrUpdateMusic(result)
    val path = if (isCache) FileUtils.getMusicCacheDir() + result.artist + " - " + result.title + "(" + result.quality + ")"
    else FileUtils.getMusicDir() + result.artist + " - " + result.title + ".mp3"
    val task = FileDownloader.getImpl()
            .create(result.uri)
            .setPath(path)
            .setCallbackProgressTimes(100)
            .setListener(TaskItemAdapter.taskDownloadListener)
    val model = TasksManager.addTask(task.id, result.mid, result.title, result.uri, path, isCache)
    if (model != null) {
        TasksManager.addTaskForViewHolder(task)
        task.start()
    }

}

/**
 * 更新用户Token(主要用于在线歌单)
 */
fun updateLoginToken() {
    ApiManager.request(PlaylistApiServiceImpl.checkLoginStatus(),
            object : RequestCallBack<User> {
                override fun success(result: User?) {
                    EventBus.getDefault().post(LoginEvent(true, result))
                }

                override fun error(msg: String) {
                    ToastUtils.show(msg)
                    EventBus.getDefault().post(LoginEvent(false, null))
                }
            }
    )
}

/**
 * 注销登录
 */
fun logout() {
    UserStatus.clearUserInfo()
    UserStatus.saveLoginStatus(false)
    SPUtils.putAnyCommit(SPUtils.QQ_ACCESS_TOKEN, "")
    SPUtils.putAnyCommit(SPUtils.QQ_OPEN_ID, "")
    MusicApp.socketManager.toggleSocket(false)
    MusicApp.mTencent.logout(MusicApp.getAppContext())
    AccessTokenKeeper.clear(MusicApp.getAppContext())
    EventBus.getDefault().post(LoginEvent(false, null))
}

/**
 * 倒计时弹窗
 */
fun Context.showCountDown(dismissListener: (checked: Boolean) -> Unit) {
    if (this is MainActivity && (this.isDestroyed || this.isFinishing)) {
        return
    }
    MaterialDialog.Builder(this)
            .title("定时关闭")
            .items(CountDownUtils.selectItems)
            .itemsCallbackSingleChoice(CountDownUtils.type) { dialog, _, which, _ ->
                CountDownUtils.type = which
                when (which) {
                    0 -> {
                        CountDownUtils.totalTime = 0
                        CountDownUtils.cancel()
                    }
                    5 -> {
                        dialog.cancel()
                        MaterialDialog.Builder(this)
                                .title(getString(R.string.custom_count_down_time))
                                .inputType(InputType.TYPE_CLASS_NUMBER)//可以输入的类型-电话号码
                                .input(getString(R.string.count_down_minutes), "") { dialog1, input ->
                                    val time = (input ?: 0).toString().toInt()
                                    dialog1.getActionButton(DialogAction.POSITIVE).isEnabled = time <= 24 * 60
                                }
                                .inputRange(1, 4)
                                .onPositive { dialog12, _ ->
                                    val time = (dialog12.inputEditText?.text
                                            ?: 0).toString().toInt()
                                    if (time == 0 || time > 24 * 60) {
                                        ToastUtils.show(getString(R.string.down_time_more))
                                    } else {
                                        CountDownUtils.starCountDownByTime(time)
                                    }
                                }.show()
                    }
                    else -> {
                        CountDownUtils.starCountDownById(which)
                    }
                }
                false
            }
            .dismissListener {
                dismissListener.invoke(CountDownUtils.type != 0)
            }
            .build()
            .show()
}

fun getNeteaseLoginStatus(success: ((User) -> Unit)?, fail: (() -> Unit)?) {
    val observer = NeteaseApiServiceImpl.getLoginStatus();
    ApiManager.request(observer, object : RequestCallBack<LoginInfo> {
        override fun success(result: LoginInfo?) {
            if (result?.code == 200) {
                val user = User()
                user.name = result.profile.nickname
                user.avatar = result.profile.avatarUrl
                user.id = result.profile.userId.toString()
                //登录成功
                success?.invoke(user)
            } else {
                fail?.invoke()
            }
        }

        override fun error(msg: String?) {
            fail?.invoke()
        }
    })
}

