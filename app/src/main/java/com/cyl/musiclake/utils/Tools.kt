package com.cyl.musiclake.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Constants


/**
 * Des    :
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
        val content = context?.resources?.getString(R.string.feedback_content, android.os.Build.VERSION.RELEASE, android.os.Build.BRAND, android.os.Build.MODEL)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        context?.startActivity(Intent.createChooser(intent,
                context.getString(R.string.about_feedback)))
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
        context?.startActivity(intent)
    }


}
