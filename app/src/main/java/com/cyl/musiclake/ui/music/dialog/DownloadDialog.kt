package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.download.TasksManager
import com.cyl.musiclake.ui.music.download.FileDownloadListener
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.NetworkUtils
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import com.liulishuo.filedownloader.FileDownloader

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class DownloadDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val music = arguments!!.getParcelable<Music>("music")
        return MaterialDialog.Builder(context!!)
                .title("歌曲下载")
                .content("歌名：  " + music!!.title +
                        "\n歌曲id：" + music.id +
                        "\n下载地址：\n" + music.uri
                ).positiveText("确定")
                .negativeText("取消")
                .onPositive { materialDialog, dialogAction ->
                    if (music.uri!!.isEmpty()) {
                        ToastUtils.show(context, "下载地址异常！")
                    } else if (!NetworkUtils.isWifiConnected(context!!) && SPUtils.getWifiMode()) {
                        ToastUtils.show(context, "当前不在wifi环境，请在设置中关闭省流量模式")
                    } else {
                        ToastUtils.show("下载任务添加成功")
                        val path = FileUtils.getMusicDir() + music.title + ".mp3"
                        val task = FileDownloader.getImpl()
                                .create(music.uri)
                                .setPath(path)
                                .setCallbackProgressTimes(100)
                                .setListener(FileDownloadListener())
                        TasksManager
                                .addTaskForViewHolder(task)
                        val model = TasksManager.addTask(task.id, music.mid!!, music.title!!, music.uri!!, path)
                        task.start()
                    }
                }.build()
    }

    companion object {

        private val result = false

        fun newInstance(song: Music): DownloadDialog {
            val dialog = DownloadDialog()
            val bundle = Bundle()
            bundle.putParcelable("music", song)
            dialog.arguments = bundle
            return dialog
        }
    }

    fun show(context: AppCompatActivity) {
        val ft = context.supportFragmentManager
        show(ft, javaClass.simpleName)
    }
}