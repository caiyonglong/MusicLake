package com.cyl.musiclake.ui.music.discover

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.netease.BannerBean
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApi
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.api.music.MusicUtils.PIC_SIZE_NORMAL
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.music.mv.MvDetailActivity
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.Tools
import com.zhouwei.mzbanner.holder.MZViewHolder
import org.jetbrains.anko.startActivity

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class TopListAdapter(list: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_top_list, list) {

    override fun convert(helper: BaseViewHolder, neteaseList: Playlist) {
        if (neteaseList.name == null)
            return
        helper.setText(R.id.title, neteaseList.name)
        if (neteaseList.coverUrl == null)
            return
        val url = when (neteaseList.type) {
            Constants.PLAYLIST_WY_ID -> MusicUtils.getAlbumPic(neteaseList.coverUrl, Constants.NETEASE, PIC_SIZE_NORMAL)
            else -> neteaseList.coverUrl
        }
        CoverLoader.loadImageView(mContext, url, helper.getView(R.id.iv_cover))
    }
}

class TopPlaylistAdapter(list: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_play_list, list) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        val url = when (playlist.type) {
            Constants.PLAYLIST_WY_ID -> MusicUtils.getAlbumPic(playlist.coverUrl, Constants.NETEASE, PIC_SIZE_NORMAL)
            else -> playlist.coverUrl
        }
        CoverLoader.loadImageView(mContext, url, helper.getView(R.id.iv_cover))
        helper.setText(R.id.tv_title, playlist.name)
        helper.setText(R.id.tv_playCount, "播放次数：${playlist.playCount}")
    }
}

/**
 * 轮播图viewHolder
 */
class BannerViewHolder(val activity: Activity) : MZViewHolder<BannerBean> {
    private var mImageView: ImageView? = null
    private var mTypeTitle: TextView? = null
    override fun createView(context: Context): View {
        // 返回页面布局
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, null)
        mImageView = view.findViewById(R.id.banner_image) as ImageView
        mTypeTitle = view.findViewById(R.id.banner_title_tv) as TextView
        return view
    }

    override fun onBind(context: Context, position: Int, data: BannerBean?) {
        // 数据绑定
        CoverLoader.loadImageView(context, data?.picUrl, mImageView)
        mTypeTitle?.text = data?.typeTitle
        mImageView?.setOnClickListener {
            when {
                data?.targetType == "3000" -> Tools.openBrowser(MusicApp.getAppContext(), data.url)
                data?.targetType == "10" -> {
                    //专辑
                    NavigationHelper.navigateToPlaylist(activity, Album().apply {
                        albumId = data.targetId
                        type = Constants.NETEASE
                    }, null)
                }
                data?.targetType == "1000" -> {
                    //歌单
                    NavigationHelper.navigateToPlaylist(activity, Playlist().apply {
                        pid = data.targetId
                        type = Constants.PLAYLIST_WY_ID
                    }, null)
                }
                data?.targetType == "1004" -> {
                    //mv
                    context.startActivity<MvDetailActivity>(Extras.MV_ID to data.targetId)
                }
                data?.targetType == "1" -> {
                    //单曲
                    MusicApi.loadSongDetailInfo(Constants.NETEASE, data.targetId) { result ->
                        PlayManager.playOnline(result)
                    }
                }
            }
        }
    }
}


class PlaylistAdapter(playlists: List<Playlist>) : BaseQuickAdapter<Playlist, BaseViewHolder>(R.layout.item_playlist, playlists) {

    override fun convert(helper: BaseViewHolder, playlist: Playlist) {
        helper.setText(R.id.tv_name, playlist.name)
        CoverLoader.loadImageView(mContext, playlist.coverUrl, helper.getView(R.id.iv_cover))
        helper.setText(R.id.tv_num, playlist.musicList.size.toString() + "首")
    }
}