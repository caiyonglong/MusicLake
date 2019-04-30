package com.cyl.musiclake.ui.music.comment

import android.support.v7.widget.LinearLayoutManager
import com.cyl.musicapi.bean.SongComment
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApi
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Extras
import kotlinx.android.synthetic.main.activity_song_comment.*

/**
 * Des    :
 * Author : master.
 * Date   : 2018/9/8 .
 */
class SongCommentActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    private var commentAdapter: SongCommentAdapter? = null

    override fun getLayoutResID(): Int {
        return R.layout.activity_song_comment
    }

    override fun setToolbarTitle(): String {
        return getString(R.string.song_comment_new)
    }

    override fun initView() {
//        hotCommentRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        newCommentRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        val music = intent.getParcelableExtra<Music>(Extras.SONG)
        MusicApi.getMusicCommentInfo(music, success = {
            updateSongComment(it)
        }, fail = {
            showError("未搜索到当前歌曲评论", false)
        })
    }

    private fun updateSongComment(comments: MutableList<SongComment>?) {
        if (comments == null) {
            showError("未搜索到当前歌曲评论", false)
            return
        }
        if (commentAdapter == null) {
            commentAdapter = SongCommentAdapter(comments)
            newCommentRsv.adapter = commentAdapter
        } else {
            commentAdapter?.setNewData(comments)
        }
    }

    override fun initInjector() {
    }
}