package com.cyl.musiclake.ui.music.charts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyl.musiclake.MusicApp.mContext
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.ui.music.charts.ChartsAdapter.Companion.ITEM_CHART
import com.cyl.musiclake.ui.music.charts.ChartsAdapter.Companion.ITEM_TITLE
import com.cyl.musiclake.utils.CoverLoader

/**
 * 排行榜列表
 */
class ChartsAdapter(val context: Context, val data: MutableList<GroupItemData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickListener: ((Int) -> Unit)? = null
    var tag: String? = null

    private val stringIds = arrayListOf(R.string.song_list_item_title_1, R.string.song_list_item_title_2, R.string.song_list_item_title_3)

    companion object {
        //标题
        val ITEM_TITLE = 0

        //排行榜单正方形
        val ITEM_CHART = 1

        //排行榜单长方形
        val ITEM_CHART_LARGE = 2
    }

    /**
     * 添加新数据
     */
    fun addNewData(newList: MutableList<GroupItemData>) {
        val startIndex = data.size
        data.addAll(newList)
        notifyItemRangeInserted(startIndex, newList.size)
    }

    /**
     * 清空数据
     */
    fun cleanData() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mInflater = LayoutInflater.from(context)
        return when (viewType) {
            ITEM_TITLE -> {
                val view = mInflater.inflate(R.layout.item_charts_title, parent, false)
                TitleViewHolder(view)
            }
            ITEM_CHART_LARGE -> {
                val view = mInflater.inflate(R.layout.item_charts_large, parent, false)
                ChartLargeViewHolder(view)
            }
            else -> {
                val view = mInflater.inflate(R.layout.item_charts, parent, false)
                ChartViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TitleViewHolder) {
            holder.titleTv.text = data[position].title
        } else if (holder is ChartViewHolder) {

            val playlist = data[position].data as Playlist

            CoverLoader.loadImageView(context, playlist.coverUrl, holder.coverIv)
            holder.titleTv.text = playlist.name
            holder.updateFrequencyTv.text = playlist.updateFrequency
            holder.coverIv.setOnClickListener {
                clickListener?.invoke(position)
            }
        } else if (holder is ChartLargeViewHolder) {
            val playlist = data[position].data as Playlist
            CoverLoader.loadImageView(context, playlist.coverUrl, holder.coverIv)
            playlist.updateFrequency?.let {
                holder.updateFrequencyTv.text = it
                holder.updateFrequencyTv.visibility = View.VISIBLE
            }

            for (i in 0 until playlist.musicList.size) {
                if (i >= 3) continue
                val music = playlist.musicList[i]
                when (i) {
                    0 -> holder.contentTv1.text = mContext.getString(stringIds[i], music.title, music.artist)
                    1 -> holder.contentTv2.text = mContext.getString(stringIds[i], music.title, music.artist)
                    2 -> holder.contentTv3.text = mContext.getString(stringIds[i], music.title, music.artist)
                }
            }

            holder.itemView.setOnClickListener {
                clickListener?.invoke(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].itemType
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTv = itemView.findViewById<TextView>(R.id.tv_title)
    }

    inner class ChartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTv = itemView.findViewById<TextView>(R.id.tv_title)
        var updateFrequencyTv = itemView.findViewById<TextView>(R.id.tv_update_frequency)
        var coverIv = itemView.findViewById<ImageView>(R.id.iv_cover)
    }

    inner class ChartLargeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var updateFrequencyTv = itemView.findViewById<TextView>(R.id.tv_update_frequency)
        var coverIv = itemView.findViewById<ImageView>(R.id.iv_cover)
        val contentTv1 = itemView.findViewById<TextView>(R.id.tv_music_1)
        val contentTv2 = itemView.findViewById<TextView>(R.id.tv_music_2)
        val contentTv3 = itemView.findViewById<TextView>(R.id.tv_music_3)
    }
}

@SuppressLint("ParcelCreator")
class GroupItemData() : Parcelable {
    var itemType = ITEM_TITLE
    var title: String? = ""
    var data: Any? = null

    constructor(parcel: Parcel) : this() {
        itemType = parcel.readInt()
        title = parcel.readString()
    }

    constructor(title: String) : this() {
        this.itemType = ITEM_TITLE
        this.title = title
    }

    constructor(playlist: Playlist?) : this() {
        this.itemType = ITEM_CHART
        this.data = playlist
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(itemType)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupItemData> {
        override fun createFromParcel(parcel: Parcel): GroupItemData {
            return GroupItemData(parcel)
        }

        override fun newArray(size: Int): Array<GroupItemData?> {
            return arrayOfNulls(size)
        }
    }

}