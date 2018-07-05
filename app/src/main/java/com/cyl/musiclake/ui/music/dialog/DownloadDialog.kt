//package com.cyl.musiclake.ui.music.dialog
//
//import android.app.Dialog
//import android.os.Bundle
//import android.support.v4.app.DialogFragment
//import android.support.v7.app.AppCompatActivity
//import com.afollestad.materialdialogs.MaterialDialog
//import com.cyl.musiclake.R
//import com.cyl.musiclake.api.MusicApi
//import com.cyl.musiclake.data.db.Music
//import com.cyl.musiclake.data.download.TasksManager
//import com.cyl.musiclake.net.ApiManager
//import com.cyl.musiclake.net.RequestCallBack
//import com.cyl.musiclake.ui.music.download.FileDownloadListener
//import com.cyl.musiclake.utils.*
//import com.liulishuo.filedownloader.FileDownloader
//
///**
// * 作者：yonglong on 2016/9/14 15:24
// * 邮箱：643872807@qq.com
// * 版本：2.5
// */
//class DownloadDialog : DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        return MaterialDialog.Builder(context!!)
//                .title(R.string.popup_download)
//                .content("歌名：  " + music!!.title +
//                        "\n歌曲id：" + music.id +
//                        "\n下载地址：\n" + music.uri
//                ).positiveText("确定")
//                .negativeText("取消")
//                .onPositive { _, _ ->
//                    if (music?.uri!!.isEmpty()) {
//                        ToastUtils.show(context, "下载地址异常！")
//                    } else if (!NetworkUtils.isWifiConnected(context!!) && SPUtils.getWifiMode()) {
//                        ToastUtils.show(context, "当前不在wifi环境，请在设置中关闭省流量模式")
//                    } else {
//
//                    }
//                }.build()
//    }
//
//    companion object {
//
//        private val result = false
//        var music: Music? = null
//
//        fun newInstance(song: Music): DownloadDialog {
//            val dialog = DownloadDialog()
//            val bundle = Bundle()
//            music = song
//            dialog.arguments = bundle
//            return dialog
//        }
//    }
//
//    fun show(context: AppCompatActivity) {
//        val ft = context.supportFragmentManager
//        show(ft, javaClass.simpleName)
//    }
//}