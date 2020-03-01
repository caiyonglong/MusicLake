package com.cyl.musiclake.ui.music.edit

import com.afollestad.materialdialogs.MaterialDialog
import com.cyl.musiclake.R
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.utils.Mp3Util
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_music_edit.*

/**
 * Des    :
 * Author : master.
 * Date   : 2018/8/26 .
 */
class EditMusicActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    var baseMusicInfoInfo: BaseMusicInfo? = null

    override fun getLayoutResID(): Int {
        return R.layout.activity_music_edit
    }

    override fun setToolbarTitle(): String {
        return getString(R.string.tag_title)
    }

    override fun initView() {
        saveTagBtn.setOnClickListener {

            MaterialDialog(this).show {
                title(R.string.warning)
                message(R.string.tag_edit_tips)
                positiveButton(R.string.sure) {
                    baseMusicInfoInfo?.title = titleInputView.editText?.text.toString()
                    baseMusicInfoInfo?.artist = artistInputView.editText?.text.toString()
                    baseMusicInfoInfo?.album = albumInputView.editText?.text.toString()
                    baseMusicInfoInfo?.let { it1 ->
                        if (updateTagInfo(it1)) {
                            ToastUtils.show(getString(R.string.tag_edit_success))
                        } else {
                            ToastUtils.show(getString(R.string.tag_edit_tips))
                        }
                        this@EditMusicActivity.finish()
                    }
                }
            }
        }
    }

    override fun initData() {
        baseMusicInfoInfo = intent.getParcelableExtra(Extras.SONG)
        titleInputView.editText?.setText(baseMusicInfoInfo?.title)
        artistInputView.editText?.setText(baseMusicInfoInfo?.artist)
        albumInputView.editText?.setText(baseMusicInfoInfo?.album)

        baseMusicInfoInfo?.uri?.let { Mp3Util.getTagInfo(it) }
    }

    override fun initInjector() {
    }

    private fun updateTagInfo(baseMusicInfoInfo: BaseMusicInfo): Boolean {
        if (baseMusicInfoInfo.uri == null) return false
        val result = Mp3Util.updateTagInfo(baseMusicInfoInfo.uri!!, baseMusicInfoInfo)
        Mp3Util.getTagInfo(baseMusicInfoInfo.uri!!)
        if (result) {
            DaoLitepal.saveOrUpdateMusic(baseMusicInfoInfo)
        }
        return result
    }
}