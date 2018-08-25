package com.cyl.musiclake.ui.music.discover

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class TopArtistListAdapter(list: List<Artist>) : BaseQuickAdapter<Artist, BaseViewHolder>(R.layout.item_discover_artist, list) {

    override fun convert(helper: BaseViewHolder, artist: Artist) {
        if (artist.name == null)
            return
        helper.setText(R.id.tv_name, artist.name)
        CoverLoader.loadImageView(mContext, MusicUtils.getAlbumPic(artist.picUrl, artist.type, 90), helper.getView(R.id.ic_cover))
    }
}

class ArtistListAdapter(artistList: MutableList<Artist>) : BaseQuickAdapter<Artist, BaseViewHolder>(R.layout.item_playlist, artistList) {

    override fun convert(helper: BaseViewHolder, artist: Artist) {
        helper.setText(R.id.tv_name, artist.name)
        CoverLoader.loadImageView(mContext, MusicUtils.getAlbumPic(artist.picUrl, artist.type, 90), helper.getView(R.id.ic_cover))
    }
}