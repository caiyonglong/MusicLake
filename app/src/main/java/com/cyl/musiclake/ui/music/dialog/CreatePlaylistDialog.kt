package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.utils.LogUtil

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class CreatePlaylistDialog : DialogFragment() {
    var successListener:((String)->Unit)?=null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val music = arguments!!.getParcelable<Music>(TAG_MUSIC)
        return MaterialDialog.Builder(activity!!)
                .title(R.string.create_playlist)
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .inputRangeRes(2, 20, R.color.red)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.input_playlist, R.string.playlist_name, false) { dialog, input -> LogUtil.e(TAG, input.toString()) }
                .onPositive { dialog, which ->
                    val title = dialog.inputEditText!!.text.toString()
                    LogUtil.d(TAG, title)
                    if(successListener!=null){
                        successListener?.invoke(title)
                    }
                }.build()
    }

    companion object {
        private val TAG = "CreatePlaylistDialog"
        private val TAG_MUSIC = "music"

        @JvmOverloads
        fun newInstance(music: Music? = null): CreatePlaylistDialog {
            val dialog = CreatePlaylistDialog()
            val bundle = Bundle()
            bundle.putParcelable(TAG_MUSIC, music)
            dialog.arguments = bundle
            return dialog
        }
    }
}
