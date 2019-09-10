package com.cyl.musiclake.ui.music.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class AddPlaylistDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val music = arguments?.getParcelable<Music>("music")
        return if (music?.type === Constants.LOCAL) {
            showInfoDialog(getString(R.string.add_to_playlist), getString(R.string.add_un_support_local))
        } else Dialog(context!!)
    }


    private fun showInfoDialog(title1: String, msg1: String): MaterialDialog {
        var title = title1
        var msg = msg1
        if (title.isEmpty()) {
            title = getString(R.string.prompt)
        }
        if (msg.isEmpty()) {
            msg = getString(R.string.loading)
        }
        return MaterialDialog(activity!!)
                .title(text = title)
                .message(text = msg)
                .positiveButton(R.string.sure)
    }

    companion object {

        private val TAG_CREATE = "create_playlist"
        private val result = false

        fun newInstance(song: Music): AddPlaylistDialog {
            val dialog = AddPlaylistDialog()
            val bundle = Bundle()
            bundle.putParcelable("music", song)
            dialog.arguments = bundle
            return dialog
        }
    }

}