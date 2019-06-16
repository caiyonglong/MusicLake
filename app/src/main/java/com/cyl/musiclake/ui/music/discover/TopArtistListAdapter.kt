package com.cyl.musiclake.ui.music.discover

import android.graphics.Color
import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.bean.SingerCate
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.api.music.MusicUtils.PIC_SIZE_SMALL
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
        CoverLoader.loadImageView(mContext, MusicUtils.getAlbumPic(artist.picUrl, artist.type, PIC_SIZE_SMALL), helper.getView(R.id.iv_cover))
    }
}

class ArtistListAdapter(artistList: MutableList<Artist>) : BaseQuickAdapter<Artist, BaseViewHolder>(R.layout.item_playlist, artistList) {

    override fun convert(helper: BaseViewHolder, artist: Artist) {
        helper.setText(R.id.tv_name, artist.name)
        CoverLoader.loadImageView(mContext, MusicUtils.getAlbumPic(artist.picUrl, artist.type, PIC_SIZE_SMALL), helper.getView(R.id.iv_cover))
    }
}

/**
 * 歌手分类适配器
 */
class ArtistCateAdapter(categories: MutableList<SingerCate>) : BaseQuickAdapter<SingerCate, BaseViewHolder>(R.layout.item_artist_cate, categories) {
    var flagId = -100
    var position = 0

    override fun convert(helper: BaseViewHolder, cate: SingerCate) {
        helper.setText(R.id.titleTv, cate.name)
        helper.getView<CheckedTextView>(R.id.titleTv).isChecked = position == helper.adapterPosition
        helper.setTextColor(R.id.titleTv, if (position == helper.adapterPosition) Color.WHITE else Color.BLACK)
    }
}
