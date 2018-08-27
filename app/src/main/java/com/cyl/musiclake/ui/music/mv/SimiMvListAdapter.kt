package com.cyl.musiclake.ui.music.mv

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.netease.MvInfoDetail
import com.cyl.musiclake.R
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.FormatUtil

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SimiMvListAdapter(list: List<MvInfoDetail>) : BaseQuickAdapter<MvInfoDetail, BaseViewHolder>(R.layout.item_simi_mv, list) {

    override fun convert(helper: BaseViewHolder, detail: MvInfoDetail) {
        helper.setText(R.id.tv_title, detail.name)
        helper.setText(R.id.tv_play_count, FormatUtil.formatPlayCount(detail.playCount))
        helper.setText(R.id.tv_duration, FormatUtil.formatTime(detail.duration.toLong()))
        helper.setText(R.id.tv_author, "by " + detail.artistName)
        CoverLoader.loadImageView(mContext, detail.cover, helper.getView<ImageView>(R.id.iv_cover))
    }
}