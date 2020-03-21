package com.cyl.musiclake.ui.music.playqueue

import android.graphics.Color
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.theme.ThemeStore
import com.cyl.musiclake.utils.ConvertUtils
import com.music.lake.musiclib.MusicPlayerManager

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class QueueAdapter(baseMusicInfoInfoList: List<BaseMusicInfo>) : BaseQuickAdapter<BaseMusicInfo, BaseViewHolder>(R.layout.item_queue, baseMusicInfoInfoList) {

    private var mSwatch: androidx.palette.graphics.Palette.Swatch? = null

    override fun convert(holder: BaseViewHolder, item: BaseMusicInfo) {
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))
        holder.setText(R.id.tv_artist, item.artist)
        //选中正在播放的歌曲
        if (MusicPlayerManager.getInstance().getNowPlayingMusic()?.mid == item.mid && MusicPlayerManager.getInstance().getNowPlayingIndex() == holder.adapterPosition) {
            holder.setTextColor(R.id.tv_title, Color.parseColor("#0091EA"))
            holder.setTextColor(R.id.tv_artist, Color.parseColor("#01579B"))
        } else {
            if (ThemeStore.THEME_MODE == ThemeStore.DAY) {
                holder.setTextColor(R.id.tv_title, Color.parseColor("#000000"))
            } else {
                holder.setTextColor(R.id.tv_title, Color.parseColor("#ffffff"))
            }
            holder.setTextColor(R.id.tv_artist, Color.parseColor("#9e9e9e"))
        }
        holder.addOnClickListener(R.id.iv_more)
        if (item.type == Constants.LOCAL) {
            holder.getView<View>(R.id.iv_resource).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.iv_resource).visibility = View.VISIBLE
            when {
                item.type == Constants.BAIDU -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.baidu)
                }
                item.type == Constants.NETEASE -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.netease)
                }
                item.type == Constants.QQ -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.qq)
                }
                item.type == Constants.XIAMI -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.xiami)
                }
            }
        }
    }

}