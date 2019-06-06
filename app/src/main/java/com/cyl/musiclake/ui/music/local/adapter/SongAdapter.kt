package com.cyl.musiclake.ui.music.local.adapter

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicApi
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.event.MetaChangedEvent
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.theme.ThemeStore
import com.cyl.musiclake.ui.widget.fastscroll.FastScrollRecyclerView
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.dip


/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SongAdapter(val musicList: List<Music>) : BaseQuickAdapter<Music, BaseViewHolder>(R.layout.item_music, musicList), FastScrollRecyclerView.SectionedAdapter {
    override fun convert(holder: BaseViewHolder, item: Music) {
        CoverLoader.loadImageView(mContext, item.coverUri, holder.getView(R.id.iv_cover))
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))

        //音质图标显示
        val quality = when {
            item.sq -> mContext.resources.getDrawable(R.drawable.sq_icon, null)
            item.hq -> mContext.resources.getDrawable(R.drawable.hq_icon, null)
            else -> null
        }
        quality?.let {
            quality.setBounds(0, 0, quality.minimumWidth + mContext.dip(2), quality.minimumHeight)
            holder.getView<TextView>(R.id.tv_artist).setCompoundDrawables(quality, null, null, null)
        }
        //设置歌手专辑名
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.artist, item.album))
        //设置播放状态
        if (PlayManager.getPlayingId() == item.mid) {
            holder.getView<View>(R.id.v_playing).visibility = View.VISIBLE
            holder.setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.app_green))
            holder.setTextColor(R.id.tv_artist, ContextCompat.getColor(mContext, R.color.app_green))

            recyclerView.scrollToPosition(holder.adapterPosition)
        } else {
            holder.getView<View>(R.id.v_playing).visibility = View.GONE
            if (ThemeStore.THEME_MODE == ThemeStore.DAY) {
                holder.setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.black))
            } else {
                holder.setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.white))
            }
            holder.setTextColor(R.id.tv_artist, ContextCompat.getColor(mContext, R.color.grey))
        }

        holder.addOnClickListener(R.id.iv_more)

        //是否有mv（现只支持百度音乐）
        if (item.hasMv == 1) {
            holder.getView<View>(R.id.iv_mv).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.iv_mv).visibility = View.GONE
        }
        //是否可播放
        if (item.isCp) {
            holder.setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.grey))
            holder.setTextColor(R.id.tv_artist, ContextCompat.getColor(mContext, R.color.grey))
        }

        //歌曲类型
        if (item.type == Constants.LOCAL) {
            holder.getView<View>(R.id.iv_resource).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.iv_resource).visibility = View.VISIBLE
            when {
                item.type == Constants.BAIDU -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.baidu)
                }
                item.type == Constants.NETEASE -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.netease)
                }
                item.type == Constants.QQ -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.qq)
                }
                item.type == Constants.XIAMI -> {
                    holder.setImageResource(R.id.iv_resource, R.drawable.xiami)
                }
                //如果是视频，跳转到视频播放界面
                item.type == Constants.VIDEO -> {
                    holder.getView<View>(R.id.iv_resource).visibility = View.GONE
                    item.uri?.let {
                        holder.getView<ImageView>(R.id.iv_cover).setImageBitmap(getVideoThumbnail(it))
                    }
                }
                else -> {
                    holder.getView<View>(R.id.iv_resource).visibility = View.GONE
                }
            }
        }
        if (item.coverUri != null) {
            CoverLoader.loadImageView(mContext, item.coverUri, holder.getView(R.id.iv_cover))
        }
        if (item.coverUri.isNullOrEmpty()) {
            //加载歌曲专辑图
            item.title?.let {
                MusicApi.getMusicAlbumPic(item.title.toString(), success = {
                    item.coverUri = it
                    CoverLoader.loadImageView(mContext, it, holder.getView(R.id.iv_cover))
                })
            }
        }
        if (item.isCp) {
            holder.itemView.setOnClickListener {
                ToastUtils.show("歌曲无法播放")
            }
        }
    }

    fun getVideoThumbnail(videoPath: String): Bitmap? {
        LogUtil.d(TAG, "videoPath= $videoPath")
        var bitmap: Bitmap? = null;
        var mediaMetadataRetriever: MediaMetadataRetriever? = null;
        try {
            mediaMetadataRetriever = MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
            bitmap = mediaMetadataRetriever.frameAtTime;
        } catch (e: Exception) {
            LogUtil.e(TAG, "Exception in getVideoThumbnail(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUserInfo(event: MetaChangedEvent) {
        notifyDataSetChanged()
    }


    override fun getSectionName(position: Int): String {
        return musicList[position].title?.get(0).toString()
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
}