package com.cyl.musiclake.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.cyl.musiclake.R


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
}
