package com.cyl.musiclake.ui.music.local.adapter

import android.app.Activity
import android.os.Build
import android.util.Pair
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApi
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.utils.CoverLoader

class ArtistAdapter(private val artistList: MutableList<Artist>) : BaseQuickAdapter<Artist, BaseViewHolder>(R.layout.item_playlist_grid, artistList) {

    override fun convert(helper: BaseViewHolder, artist: Artist) {
        helper.setText(R.id.name, artist.name)
        helper.setText(R.id.artist, artist.musicSize.toString() + "首歌")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView<ImageView>(R.id.album).transitionName = Constants.TRANSTITION_ALBUM
        }
        CoverLoader.loadImageView(context, artist.picUrl, R.drawable.default_cover_place_hor, helper.getView(R.id.album))
        if (artist.picUrl.isNullOrEmpty()) {
            artist.name?.let {
                MusicApi.getMusicAlbumPic(artist.name.toString(), success = {
                    artist.picUrl = it
                    artist.save()
                    CoverLoader.loadImageView(context, it, helper.getView(R.id.album))
                })
            }
        }
        helper.itemView.setOnClickListener {
            NavigationHelper.navigateToPlaylist(context as Activity, artist, Pair(helper.getView(R.id.album), Constants.TRANSTITION_ALBUM))
        }
    }
}