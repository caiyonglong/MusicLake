package com.cyl.musiclake.ui.music.local.adapter

import android.os.Build
import android.view.View

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.db.Album
import com.cyl.musiclake.utils.CoverLoader

class AlbumAdapter(private val albumList: List<Album>) : BaseQuickAdapter<Album, BaseViewHolder>(R.layout.item_playlist_grid, albumList) {

    override fun convert(helper: BaseViewHolder, album: Album) {
        helper.setText(R.id.name, album.name)
        helper.setText(R.id.artist, album.artistName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView<View>(R.id.album).transitionName = Constants.TRANSTITION_ALBUM
        }
        CoverLoader.loadImageView(mContext, album.cover, helper.getView(R.id.album))
//        album.name?.let {
//            if (album.cover.isNullOrEmpty()) {
//                MusicApi.getMusicAlbumPic(it, success = {
//                    //                    albumList[helper.adapterPosition].cover = it
////                    notifyItemChanged(helper.adapterPosition)
//                    CoverLoader.loadImageView(mContext, it, helper.getView(R.id.album))
//                })
//            }
//        }
    }
}
