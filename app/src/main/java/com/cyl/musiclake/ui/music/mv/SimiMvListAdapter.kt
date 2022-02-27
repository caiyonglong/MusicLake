package com.cyl.musiclake.ui.music.mv

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.VideoInfoBean
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.FormatUtil

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SimiMvListAdapter(list: MutableList<VideoInfoBean>) : BaseQuickAdapter<VideoInfoBean, BaseViewHolder>(R.layout.item_simi_mv, list) {

    override fun convert(helper: BaseViewHolder, detail: VideoInfoBean) {
        helper.setText(R.id.tv_title, detail.title)
        helper.setText(R.id.tv_play_count, FormatUtil.formatPlayCount(detail.playCount))
        helper.setText(R.id.tv_duration, FormatUtil.formatTime(detail.durationms))
        helper.setText(R.id.tv_author, "by " + detail.artist[0].name)
        CoverLoader.loadImageView(context, detail.coverUrl, helper.getView<ImageView>(R.id.iv_cover))
    }
}