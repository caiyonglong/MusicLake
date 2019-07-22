package com.cyl.musiclake.ui.music.charts

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.MusicApp.mContext
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 排行版適配器
 */
class OnlineAdapter(playlist: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_online_large, playlist) {

    private val viewIds = arrayListOf(R.id.tv_music_1, R.id.tv_music_2, R.id.tv_music_3)
    private val stringIds = arrayListOf(R.string.song_list_item_title_1, R.string.song_list_item_title_2, R.string.song_list_item_title_3)

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        CoverLoader.loadImageView(mContext, playlist.coverUrl, helper.getView(R.id.iv_cover))
        helper.setText(R.id.title, playlist.name)
        for (i in 0 until viewIds.size) {
            if (playlist.musicList.size <= i) continue
            val music = playlist.musicList[i]
            helper.setText(viewIds[i], mContext.getString(stringIds[i],
                    music.title, music.artist))
        }
    }
}

class ChartsAdapter(val context: Context, val playlist: List<Playlist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickListener: ((Int) -> Unit)? = null
    var tag: String? = null

    private val ITEM_TITLE = 1
    private val ITEM_CHART = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mInflater = LayoutInflater.from(context)
        return if (viewType == ITEM_TITLE) {
            val view = mInflater.inflate(R.layout.item_charts_title, parent, false)
            TitleViewHolder(view)
        } else {
            val view = mInflater.inflate(R.layout.item_charts, parent, false)
            ChartViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TitleViewHolder) {

        } else if (holder is ChartViewHolder) {
            CoverLoader.loadImageView(mContext, playlist[position].coverUrl, holder.coverIv)
            holder.titleTv.text = playlist[position].name
            holder.updateFrequencyTv.text = playlist[position].updateFrequency
            holder.coverIv.setOnClickListener {
                clickListener?.invoke(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_CHART
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTv = itemView.findViewById<TextView>(R.id.tv_title)
    }

    inner class ChartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTv = itemView.findViewById<TextView>(R.id.tv_title)
        var updateFrequencyTv = itemView.findViewById<TextView>(R.id.tv_update_frequency)
        var coverIv = itemView.findViewById<ImageView>(R.id.iv_cover)
    }

}