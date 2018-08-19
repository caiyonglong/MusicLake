package com.cyl.musiclake.ui.music.playlist

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.utils.ConvertUtils

/**
 * 作者：yonglong
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class PlaylistEditAdapter(list: MutableList<Playlist>) : BaseItemDraggableAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist_edit, list) {


    override fun convert(holder: BaseViewHolder, item: Playlist) {
        holder.setText(R.id.tv_name, ConvertUtils.getTitle(item.name))
        holder.getView<CheckBox>(R.id.cb_playlist).isChecked = (mContext as PlaylistManagerActivity).checkedMap.containsKey(item.id.toString())
    }
}