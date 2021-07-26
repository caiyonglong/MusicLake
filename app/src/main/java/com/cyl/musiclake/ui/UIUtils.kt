package com.cyl.musiclake.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.text.InputType
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsSingleChoice
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
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.event.FileEvent
import com.cyl.musiclake.event.LoginEvent
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.socket.SocketManager
import com.cyl.musiclake.ui.download.TasksManager
import com.cyl.musiclake.ui.download.ui.TaskItemAdapter
import com.cyl.musiclake.ui.main.MainActivity
import com.cyl.musiclake.ui.my.user.User
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.*
import com.liulishuo.filedownloader.FileDownloader
import com.sina.weibo.sdk.auth.AccessTokenHelper
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
    fun updatePlayMode(imageView: ImageView?, isChange: Boolean = false) {
        if (imageView == null) return
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
            MaterialDialog(this).show {
                title(R.string.prompt)
                message(R.string.clear_history_playlist_tips)
                positiveButton {
                    success?.invoke()
                }
                positiveButton(R.string.sure)
                negativeButton(R.string.cancel)
            }
        }
        else -> {
            MaterialDialog(this).show {
                title(R.string.prompt)
                message(R.string.delete_playlist_tips)
                positiveButton {
                    success?.invoke()
                }
                positiveButton(R.string.sure)
                negativeButton(R.string.cancel)
            }
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
//    if (!music.isDl && !isCache) {
//        ToastUtils.show(MusicApp.getAppContext(), getString(R.string.download_ban))
//        return
//    }
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
                MaterialDialog(this@downloadMusic).show {
                    title(titleId)
                    message(text = getString(R.string.download_content, music.title))
                    positiveButton {
                        music.uri = result
                        addDownloadQueue(music, isCache = isCache)
                    }
                    positiveButton(R.string.sure)
                    negativeButton(R.string.cancel)
                }
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
 * 删除单个歌曲
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
                NavigationHelper.scanFileAsync(this@deleteSingleMusic)
                ToastUtils.show(MusicApp.getAppContext().getString(R.string.delete_song_success))
                //发送文件删除消息
                EventBus.getDefault().post(FileEvent())
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
                NavigationHelper.scanFileAsync(this@deleteLocalMusic)
                ToastUtils.show(getString(R.string.delete_song_success))
                //发送文件删除消息
                EventBus.getDefault().post(FileEvent())
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
    MaterialDialog(context).show {
        title(R.string.prompt)
        message(text = content)
        positiveButton {
            success?.invoke()
        }
        positiveButton(R.string.sure)
        negativeButton(R.string.cancel)
    }
}

/**
 * 对话框显示tip
 */
fun showTipsDialog(context: AppCompatActivity, contentId: Int? = null, content: String? = null, success: (() -> Unit)? = null) {
    if (context.isDestroyed || context.isFinishing) return
    MaterialDialog(context).show {
        title(R.string.prompt)
        message(contentId, content)
        positiveButton {
            success?.invoke()
        }
        positiveButton(R.string.sure)
        negativeButton(R.string.cancel)
    }
}


/**
 * 增加到增加到下载队列
 */
fun Context.addDownloadQueue(result: Music, isBatch: Boolean = false, isCache: Boolean = false) {
    LogUtil.e(javaClass.simpleName, "addDownloadQueue -----${result.uri}")

    if (result.uri == null) {
        ToastUtils.show(this@addDownloadQueue, "${result.title} 下载地址异常！")
        getMusicDownloadUrl(result) { url ->
            if (url.isNotEmpty()) {
                result.uri = url
                addDownloadQueue(result, isBatch, isCache)
            }
        }
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
 * 获取音乐播放地址/下载地址
 */
fun getMusicDownloadUrl(music: Music, success: ((String) -> Unit)?) {
    ApiManager.request(MusicApi.getMusicInfo(music), object : RequestCallBack<Music> {
        override fun success(result: Music) {
            LogUtil.e("Download", "-----$result")
            result.uri?.let { success?.invoke(it) }
        }

        override fun error(msg: String) {
            LogUtil.e("Download", "播放异常-----$msg")
        }
    })
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
    SocketManager.toggleSocket(false)
    MusicApp.mTencent?.logout(MusicApp.getAppContext())
    AccessTokenHelper.clearAccessToken(MusicApp.getAppContext())
    EventBus.getDefault().post(LoginEvent(false, null))
}

/**
 * 取消绑定网易云音乐
 */
fun logoutNetease(success: (() -> Unit)?) {
    val observer = NeteaseApiServiceImpl.logout()
    ApiManager.request(observer, object : RequestCallBack<Any> {
        override fun error(msg: String?) {
        }

        override fun success(result: Any?) {
            LogUtil.d("logoutNetease = " + result.toString())
            //重置本地用户ID
            SPUtils.putAnyCommit(SPUtils.SP_KEY_NETEASE_UID, "")
            success?.invoke()
        }
    })
}

/**
 * 倒计时弹窗
 */
fun Context.showCountDown(dismissListener: (checked: Boolean) -> Unit) {
    if (this is MainActivity && (this.isDestroyed || this.isFinishing)) {
        return
    }
    MaterialDialog(this).show {
        title(R.string.setting_timing)
        listItemsSingleChoice(CountDownUtils.type, items = CountDownUtils.selectItems) { dialog, index, text ->
            CountDownUtils.type = index
            when (index) {
                0 -> {
                    CountDownUtils.totalTime = 0
                    CountDownUtils.cancel()
                }
                5 -> {
                    dialog.cancel()
                    MaterialDialog(this@showCountDown).show {
                        title(R.string.custom_count_down_time)
                        input(
                            hintRes = R.string.count_down_minutes,
                            maxLength = 4,
                            inputType = InputType.TYPE_CLASS_NUMBER
                        ) { dialog, text ->
                            val isValid = text.toString().toInt() <= 24 * 60
                            dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                        }
                        positiveButton {
                            val time = (it.getInputField().text ?: 0).toString().toInt()
                            if (time == 0 || time > 24 * 60) {
                                ToastUtils.show(getString(R.string.down_time_more))
                            } else {
                                CountDownUtils.starCountDownByTime(time)
                            }
                        }
                    }
                }
                else -> {
                    CountDownUtils.starCountDownById(index)
                }
            }
        }
        onDismiss {
            dismissListener.invoke(CountDownUtils.type != 0)
        }
    }
}

/**
 * 获取网易云音乐账号绑定状态
 */
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


/**
 * 歌单重命名
 */
fun AppCompatActivity.showPlaylistRenameDialog(title: String? = null, success: ((String) -> Unit)? = null) {
    MaterialDialog(this@showPlaylistRenameDialog).show {
        title(R.string.playlist_rename)
        positiveButton(R.string.sure)
        negativeButton(R.string.cancel)
        input(
            hintRes = R.string.input_playlist, maxLength = 10, prefill = title,
            inputType = InputType.TYPE_CLASS_TEXT
        ) { dialog, input ->
            LogUtil.e("=====", input.toString())
        }
        positiveButton {
            val newTitle = it.getInputField().text.toString()
            if (newTitle != title) {
                success?.invoke(newTitle)
            }
        }
    }
}


/**
 * 信息兑换框
 */
fun AppCompatActivity.showInfoDialog(title: String? = null, message: String? = null, success: (() -> Unit)? = null) {
    MaterialDialog(this@showInfoDialog).show {
        title(text = title)
        message(text = message)
        positiveButton(R.string.sure)
        negativeButton(R.string.cancel)
        positiveButton {
            success?.invoke()
        }
    }
}
