package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.utils.LogUtil

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class CreatePlaylistDialog : androidx.fragment.app.DialogFragment() {
    var successListener: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(activity!!)
                .title(R.string.create_playlist)
                .positiveButton(R.string.sure)
                .negativeButton(R.string.cancel)
                .input(hintRes = R.string.input_playlist, prefillRes = R.string.playlist_name,
                        inputType = InputType.TYPE_CLASS_TEXT) { dialog, text ->
                    LogUtil.e(TAG, text.toString())
                }
                .positiveButton {
                    val title = it.getInputField().text.toString()
                    LogUtil.d(TAG, title)
                    if (successListener != null) {
                        successListener?.invoke(title)
                    }
                }
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
