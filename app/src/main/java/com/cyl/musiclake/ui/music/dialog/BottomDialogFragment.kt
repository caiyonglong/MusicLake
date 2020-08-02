package com.cyl.musiclake.ui.music.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.cyl.musiclake.BuildConfig
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.deleteSingleMusic
import com.cyl.musiclake.ui.music.edit.EditMusicActivity
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils
import com.cyl.musiclake.ui.music.mv.VideoPlayerActivity
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.cyl.musiclake.utils.Tools
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.jetbrains.anko.support.v4.startActivity

/**
 * 歌曲操作类
 *
 */
class BottomDialogFragment : BaseBottomSheetDialogFragment() {
    private val recyclerView by lazy { mRootView?.findViewById<RecyclerView>(R.id.bottomSheetRv) }
    private val titleTv by lazy { mRootView?.findViewById<TextView>(R.id.titleTv) }
    private val subTitleTv by lazy { mRootView?.findViewById<TextView>(R.id.subTitleTv) }

    var mAdapter: ItemAdapter? = null
    var type: String = Constants.PLAYLIST_LOCAL_ID
    /**
     * 歌单id
     */
    var pid: String = Constants.PLAYLIST_LOCAL_ID

    var removeSuccessListener: ((music: Music?) -> Unit)? = null

    companion object {
        var music: Music? = null
        var TAG: String = "BottomDialogFragment"

        fun newInstance(music: Music?): BottomDialogFragment {
            val args = Bundle()
            this.music = music
            val fragment = BottomDialogFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(music: Music?, type: String?): BottomDialogFragment {
            val args = Bundle()
            this.music = music
            val fragment = BottomDialogFragment()
            fragment.arguments = args
            args.putString(Extras.PLAYLIST_TYPE, type)
            return fragment
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.dialog_layout
    }


    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBehavior = BottomSheetBehavior.from(mRootView?.parent as View)
        initItems()
    }


    /**
     * 初始化items
     */
    private fun initItems() {
        titleTv?.text = music?.title
        subTitleTv?.text = ConvertUtils.getArtistAndAlbum(music?.artist, music?.album)
        arguments?.getString(Extras.PLAYLIST_TYPE, Constants.PLAYLIST_LOCAL_ID)?.let {
            type = it
        }
        mAdapter = ItemAdapter(type)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = mAdapter
    }

    /**
     * 跳转到专辑
     */
    private fun turnToAlbum() {
        val album = Album()
        album.albumId = music?.albumId
        album.name = music?.album
        album.type = music?.type
        NavigationHelper.navigateToPlaylist(mContext, album, null)
    }

    /**
     * 打开歌手列表
     */
    private fun turnToArtist() {
        activity?.let { it1 ->
            if (music != null && music?.artistId != null && music?.artist != null) {
                if (music!!.type == Constants.LOCAL) {
                    val artist = Artist()
                    artist.artistId = music?.artistId
                    artist.name = music?.artist
                    artist.type = music?.type
                    NavigationHelper.navigateToArtist(it1, artist, null)
                } else if (music != null) {
                    LogUtil.e(TAG, music?.toString())
                    val artist = MusicUtils.getArtistInfo(music)
                    if (artist.size == 1) {
                        NavigationHelper.navigateToArtist(mContext, artist[0], null)
                    } else if (artist.size > 1) {
                        val artistNames = music?.artist?.let { it.split(",").dropLastWhile { it.isEmpty() }.toList() }
                        context?.let {
                            artistNames?.let { it2 ->
                                MaterialDialog(it).show {
                                    title(R.string.choose_singer)
                                    listItems(items = it2) { dialog, position, text ->
                                        NavigationHelper.navigateToArtist(mContext, artist[position], null)
                                    }
                                }
                            }
                        }
                    } else {
                        ToastUtils.show("歌手为空")
                    }
                }
            }
        }
    }

    /**
     * 跳转编辑
     */
    private fun turnToEdit() {
        startActivity<EditMusicActivity>(Extras.SONG to music)
    }

    /**
     *去删除
     */
    private fun turnToDelete(music: Music?) {
        if (music?.type == Constants.LOCAL || music?.isOnline == false || type == Constants.PLAYLIST_DOWNLOAD_ID) {
            (activity as AppCompatActivity?)?.deleteSingleMusic(music) {
                removeSuccessListener?.invoke(music)
            }
        } else {
            PlaylistManagerUtils.disCollectMusic(pid, music) {
                removeSuccessListener?.invoke(music)
            }
        }
    }

    /**
     * 下拉列表适配器
     */
    inner class ItemAdapter(type: String = Constants.PLAYLIST_LOCAL_ID) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
        private var itemData = mutableMapOf(
                R.string.popup_play_next to R.drawable.ic_queue_play_next,
                R.string.popup_add_to_playlist to R.drawable.ic_playlist_add,
                R.string.popup_album to R.drawable.ic_album,
                R.string.popup_artist to R.drawable.ic_art_track,
                R.string.popup_detail_edit to R.drawable.ic_mode_edit,
                R.string.popup_download to R.drawable.item_download,
                R.string.popup_mv to R.drawable.ic_video_label,
                R.string.popup_delete to R.drawable.ic_delete,
                R.string.popup_share to R.drawable.ic_share_black)
        val data = mutableListOf<PopupItemBean>()

        init {
            //是否是本地视频
            if (music?.type == Constants.VIDEO) {
                itemData.clear()
            }
            //是否显示下载歌曲Item
            if (!BuildConfig.HAS_DOWNLOAD || type == Constants.PLAYLIST_DOWNLOAD_ID) {
                itemData.remove(R.string.popup_download)
            }
            //是否有mv
            if (music?.hasMv == 0) {
                itemData.remove(R.string.popup_mv)
            }

            if (music?.type == Constants.LOCAL) {
                itemData.remove(R.string.popup_download)
                itemData.remove(R.string.popup_add_to_playlist)
            } else if (type != Constants.PLAYLIST_DOWNLOAD_ID) {
                itemData.remove(R.string.popup_detail_edit)

                if (music?.isDl == false || music?.isOnline == false) {
                    itemData.remove(R.string.popup_download)
                }

                if (type != Constants.PLAYLIST_CUSTOM_ID && type != Constants.PLAYLIST_IMPORT_ID && music?.isOnline == true) {
                    itemData.remove(R.string.popup_delete)
                }
            }

            itemData.forEach {
                data.add(PopupItemBean(getString(it.key), it.value))
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
            holder.textView.text = data[position].title
            holder.icon.setImageResource(data[position].icon)
            holder.icon.setColorFilter(Color.parseColor("#0091EA"))
            holder.itemView.setOnClickListener {
                when (data[position].icon) {
                    R.drawable.ic_queue_play_next -> PlayManager.nextPlay(music)
                    R.drawable.ic_playlist_add -> {
                        if (music?.type != Constants.LOCAL) {
                            PlaylistManagerUtils.addToPlaylist(mContext, music)
                        }
                    }
                    R.drawable.ic_art_track -> {
                        turnToArtist()
                    }
                    R.drawable.ic_album -> {
                        turnToAlbum()
                    }
                    R.drawable.ic_mode_edit -> {
                        turnToEdit()
                    }
                    R.drawable.ic_delete -> {
                        turnToDelete(music)
                    }
                    R.drawable.ic_video_label -> {
                        if (music?.type == Constants.BAIDU || music?.type == Constants.VIDEO) {
                            startActivity<VideoPlayerActivity>(Extras.VIDEO_VID to music?.mid)
                        } else {

                        }
                    }
                    R.drawable.item_download -> {
                        if (music?.type != Constants.LOCAL) {
                            QualitySelectDialog.newInstance(music).apply {
                                isDownload = true
                            }.show(mContext)
                        }
                    }
                    R.drawable.ic_share_black -> {
                        Tools.qqShare(mContext, PlayManager.getPlayingMusic())
                    }
                }
                mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }


        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView = itemView.findViewById(R.id.tv_title)
            var icon: ImageView = itemView.findViewById(R.id.iv_icon)
        }
    }

}

data class PopupItemBean(val title: String = "", val icon: Int = 0)
