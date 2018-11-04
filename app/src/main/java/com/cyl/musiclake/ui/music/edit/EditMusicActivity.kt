package com.cyl.musiclake.ui.music.edit

import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.bean.data.db.DaoLitepal
import com.cyl.musiclake.utils.Mp3Util
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_music_edit.*

/**
 * Des    :
 * Author : master.
 * Date   : 2018/8/26 .
 */
class EditMusicActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    var music: Music? = null

    override fun getLayoutResID(): Int {
        return R.layout.activity_music_edit
    }

    override fun setToolbarTitle(): String {
        return getString(R.string.tag_title)
    }

    override fun initView() {
        saveTagBtn.setOnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.warning)
                    .content(R.string.tag_edit_tips)
                    .positiveText(R.string.sure)
                    .onPositive { _, _ ->
                        music?.title = titleInputView.editText?.text.toString()
                        music?.artist = artistInputView.editText?.text.toString()
                        music?.album = albumInputView.editText?.text.toString()
                        music?.let { it1 ->
                            if (updateTagInfo(it1)) {
                                ToastUtils.show(getString(R.string.tag_edit_success))
                            } else {
                                ToastUtils.show(getString(R.string.tag_edit_tips))
                            }
                            this@EditMusicActivity.finish()
                        }
                    }.show()
        }
    }

    override fun initData() {
        music = intent.getParcelableExtra(Extras.SONG)
        titleInputView.editText?.setText(music?.title)
        artistInputView.editText?.setText(music?.artist)
        albumInputView.editText?.setText(music?.album)

        music?.uri?.let { Mp3Util.getTagInfo(it) }
    }

    override fun initInjector() {
    }

    private fun updateTagInfo(music: Music): Boolean {
        if (music.uri == null) return false
        val result = Mp3Util.updateTagInfo(music.uri!!, music)
        Mp3Util.getTagInfo(music.uri!!)
        if (result) {
            DaoLitepal.saveOrUpdateMusic(music)
        }
        return result
    }
}