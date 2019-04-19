package com.cyl.musiclake.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cyl.musiclake.BuildConfig
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music


/**
 * Des    : 分享、反馈工具类
 * Author : master.
 * Date   : 2018/5/19 .
 */
object Tools {

    fun getString(context: Context?, resourceId: Int): String {
        return if (context == null) "" else context.resources.getString(resourceId)
    }

    fun feeback(context: Context?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:caiyonglong@live.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "用户反馈")
        val content = context?.resources?.getString(R.string.feedback_content, android.os.Build.VERSION.RELEASE, android.os.Build.BRAND, android.os.Build.MODEL, "${BuildConfig.VERSION_CODE}-v${BuildConfig.VERSION_NAME}")
        intent.putExtra(Intent.EXTRA_TEXT, content)
        context?.startActivity(Intent.createChooser(intent,
                context.getString(R.string.about_feedback)))
    }

    /**
     * 分享到QQ
     */
    fun qqShare(activity: Activity, music: Music?) {
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!")
            return
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append(activity.getString(R.string.share_content))
        stringBuilder.append(activity.getString(R.string.share_song_content, music.artist, music.title))
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
//        if (music.type == Constants.LOCAL) {
//            textIntent.type = "video/mp3"
//            textIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(music.uri))
//        }
        activity.startActivity(Intent.createChooser(textIntent, "歌曲分享"))
    }

    /**
     *分享链接
     */
    fun share(context: Context?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, context?.getString(R.string.share_content))
        intent.type = "text/plain"
        context?.startActivity(Intent.createChooser(intent, context.getString(R.string.share_title)))
    }

    /**
     *分享链接
     */
    fun openBrowser(context: Context?, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    /**
     * 强制隐藏输入法
     */
    fun hideInputView(view: View) {
        val inputMethodManager = MusicApp.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
