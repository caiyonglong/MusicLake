package com.cyl.musiclake.ui.music.local.adapter

import android.os.Build
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.db.Artist
import com.cyl.musiclake.utils.CoverLoader

class ArtistAdapter(private val artistList: List<Artist>) : BaseQuickAdapter<Artist, BaseViewHolder>(R.layout.item_playlist_grid, artistList) {

    override fun convert(helper: BaseViewHolder, artist: Artist) {
        helper.setText(R.id.name, artist.name)
        helper.setText(R.id.artist, artist.musicSize.toString() + "首歌")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView<ImageView>(R.id.album).transitionName = Constants.TRANSTITION_ALBUM
        }
        CoverLoader.loadImageView(mContext, artist.picUrl, helper.getView(R.id.album))
//        artist.name?.let {
//            if (artist.picUrl.isNullOrEmpty()) {
//                MusicApi.getMusicAlbumPic(it, success = {
////                    artistList[helper.adapterPosition].picUrl = it
////                    notifyItemChanged(helper.adapterPosition)
//
//                    CoverLoader.loadImageView(mContext, it, helper.getView(R.id.album))
//                })
//            }
//        }
    }
}