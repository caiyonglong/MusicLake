package com.cyl.musiclake.ui.music.discover

import android.app.Activity
import android.util.Pair
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.cyl.musicapi.netease.BannerBean
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.ui.music.discover.holder.DiscoverBannerBean
import com.cyl.musiclake.ui.music.discover.holder.DiscoverHotSingerBean
import com.cyl.musiclake.ui.music.discover.holder.DiscoverRecommendPlaylistBean
import com.zhouwei.mzbanner.MZBannerView

class DiscoverAdapter(data: MutableList<DiscoverEntry>?) : BaseMultiItemQuickAdapter<DiscoverEntry, BaseViewHolder>(data) {

    var mArtistListAdapter: TopArtistListAdapter? = null

    init {
        addItemType(DiscoverEntry.DISCOVER_BANNER, R.layout.frag_discover_banner)
        addItemType(DiscoverEntry.DISCOVER_OTHER, R.layout.frag_discover_hot_singer)
        addItemType(DiscoverEntry.DISCOVER_HOT_SINGER, R.layout.frag_discover_hot_singer)
        addItemType(DiscoverEntry.DISCOVER_RECOMMEND_PLAYLIST, R.layout.frag_discover_recommend_playlist)
    }

    override fun convert(helper: BaseViewHolder, item: DiscoverEntry) {
        if (item is DiscoverBannerBean) {
            helper?.getView<MZBannerView<BannerBean>>(R.id.mzBannerView)?.setPages(item?.data) { BannerViewHolder(context as Activity) }
        } else if (item is DiscoverHotSingerBean) {
            if (mArtistListAdapter == null) {
                helper?.getView<RecyclerView>(R.id.chartsArtistRcv)?.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false)
                //适配器
                mArtistListAdapter = TopArtistListAdapter(item.data)
                helper?.getView<RecyclerView>(R.id.chartsArtistRcv)?.adapter = mArtistListAdapter
                helper?.getView<RecyclerView>(R.id.chartsArtistRcv)?.isFocusable = false
                helper?.getView<RecyclerView>(R.id.chartsArtistRcv)?.isNestedScrollingEnabled = false
                mArtistListAdapter?.setOnItemClickListener { adapter, view, position ->
                    val artist = adapter.data[position] as Artist
                    NavigationHelper.navigateToArtist(context as Activity, artist, Pair<View, String>(view.findViewById<View>(R.id.iv_cover), context.getString(R.string.transition_album)))
                }
            } else {
                mArtistListAdapter?.setNewData(item.data)
            }
        } else if (item is DiscoverRecommendPlaylistBean) {
//            LogUtil.d(TAG, "获取推荐歌单 songs：" + playlists.size)
////            if (playlists.size > 6) {
//                this.recommendPlaylist = playlists.subList(0, 6)
//            } else {
//                this.recommendPlaylist = playlists
//            }
//            if (mPlaylistAdapter == null) {
//                mPlaylistAdapter = TopPlaylistAdapter(recommendPlaylist)
//                //推荐列表
//                recommendPlaylistRsv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)
//                recommendPlaylistRsv.adapter = mPlaylistAdapter
//                recommendPlaylistRsv.isFocusable = false
//                recommendPlaylistRsv.isNestedScrollingEnabled = false
//                mPlaylistAdapter?.bindToRecyclerView(recommendPlaylistRsv)
//                mPlaylistAdapter?.setOnItemClickListener { adapter, view, position ->
//                    val playlist = adapter.data[position] as Playlist
//                    NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, playlist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
//                }
//            } else {
//                mPlaylistAdapter?.setNewData(this.recommendPlaylist)
//            }
//            recommendPlaylistView.visibility = if (recommendPlaylist.size <= 0) View.GONE else View.VISIBLE

        }
    }
}

interface DiscoverEntry : MultiItemEntity {
    companion object {
        const val DISCOVER_BANNER = 1
        const val DISCOVER_OTHER = 2
        const val DISCOVER_HOT_SINGER = 3
        const val DISCOVER_RECOMMEND_PLAYLIST = 4
    }
}