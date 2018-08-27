package com.cyl.musiclake.ui.music.mv

import android.graphics.Color
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
class TopMvListAdapter(list: List<MvInfoDetail>) : BaseQuickAdapter<MvInfoDetail, BaseViewHolder>(R.layout.item_mv_list, list) {

    override fun convert(helper: BaseViewHolder, detail: MvInfoDetail) {
        helper.setText(R.id.tv_title, detail.name)
        if (helper.adapterPosition > 2) {
            helper.setTextColor(R.id.tv_num, Color.WHITE)
        } else {
            helper.setTextColor(R.id.tv_num, Color.RED)
        }
        helper.setText(R.id.tv_num, (helper.adapterPosition + 1).toString())
        helper.setText(R.id.tv_playCount, "播放次数：" + FormatUtil.formatPlayCount(detail.playCount))
        CoverLoader.loadImageView(mContext, detail.cover, helper.getView<ImageView>(R.id.iv_cover))
    }
}