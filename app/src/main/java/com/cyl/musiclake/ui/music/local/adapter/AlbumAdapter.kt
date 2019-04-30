package com.cyl.musiclake.ui.music.local.adapter

import android.app.Activity
import android.os.Build
import android.util.Pair
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApi
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.utils.CoverLoader

class AlbumAdapter(private val albumList: List<Album>) : BaseQuickAdapter<Album, BaseViewHolder>(R.layout.item_playlist_grid, albumList) {

    override fun convert(helper: BaseViewHolder, album: Album) {
        helper.setText(R.id.name, album.name)
        helper.setText(R.id.artist, album.artistName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView<View>(R.id.album).transitionName = Constants.TRANSTITION_ALBUM
        }
        CoverLoader.loadImageView(mContext, album.cover, helper.getView(R.id.album))
        if (album.cover.isNullOrEmpty()) {
            album.name?.let {
                MusicApi.getMusicAlbumPic(album.name.toString(), success = {
                    album.cover = it
                    album.save()
                    CoverLoader.loadImageView(mContext, it, helper.getView(R.id.album))
                })
            }
        }

        helper.itemView.setOnClickListener {
            NavigationHelper.navigateToPlaylist(mContext as Activity, album, Pair(helper.getView(R.id.album), Constants.TRANSTITION_ALBUM))
        }
    }
}
