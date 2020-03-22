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
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.ui.deleteSingleMusic
import com.cyl.musiclake.ui.music.edit.EditMusicActivity
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils
import com.cyl.musiclake.ui.music.mv.VideoPlayerActivity
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import com.cyl.musiclake.utils.Tools
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.music.lake.musiclib.MusicPlayerManager
import com.music.lake.musiclib.bean.BaseMusicInfo
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

    var removeSuccessListener: ((baseMusicInfoInfo: BaseMusicInfo?) -> Unit)? = null

    companion object {
        var baseMusicInfoInfo: BaseMusicInfo? = null
        var TAG: String = "BottomDialogFragment"

        fun newInstance(baseMusicInfoInfo: BaseMusicInfo?): BottomDialogFragment {
            val args = Bundle()
            this.baseMusicInfoInfo = baseMusicInfoInfo
            val fragment = BottomDialogFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(baseMusicInfoInfo: BaseMusicInfo?, type: String?): BottomDialogFragment {
            val args = Bundle()
            this.baseMusicInfoInfo = baseMusicInfoInfo
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
        titleTv?.text = baseMusicInfoInfo?.title
        subTitleTv?.text = ConvertUtils.getArtistAndAlbum(baseMusicInfoInfo?.artist, baseMusicInfoInfo?.album)
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
        album.albumId = baseMusicInfoInfo?.albumId
        album.name = baseMusicInfoInfo?.album
        album.type = baseMusicInfoInfo?.type
        NavigationHelper.navigateToPlaylist(mContext, album, null)
    }

    /**
     * 打开歌手列表
     */
    private fun turnToArtist() {
        activity?.let { it1 ->
            if (baseMusicInfoInfo != null && baseMusicInfoInfo?.artistId != null && baseMusicInfoInfo?.artist != null) {
                if (baseMusicInfoInfo!!.type == Constants.LOCAL) {
                    val artist = Artist()
                    artist.artistId = baseMusicInfoInfo?.artistId
                    artist.name = baseMusicInfoInfo?.artist
                    artist.type = baseMusicInfoInfo?.type
                    NavigationHelper.navigateToArtist(it1, artist, null)
                } else if (baseMusicInfoInfo != null) {
                    LogUtil.e(TAG, baseMusicInfoInfo?.toString())
                    val artist = MusicUtils.getArtistInfo(baseMusicInfoInfo)
                    if (artist.size == 1) {
                        NavigationHelper.navigateToArtist(mContext, artist[0], null)
                    } else if (artist.size > 1) {
                        val artistNames = baseMusicInfoInfo?.artist?.let { it.split(",").dropLastWhile { it.isEmpty() }.toList() }
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
        startActivity<EditMusicActivity>(Extras.SONG to baseMusicInfoInfo)
    }

    /**
     *去删除
     */
    private fun turnToDelete(baseMusicInfoInfo: BaseMusicInfo?) {
        if (baseMusicInfoInfo?.type == Constants.LOCAL || baseMusicInfoInfo?.isOnline == false || type == Constants.PLAYLIST_DOWNLOAD_ID) {
            (activity as AppCompatActivity?)?.deleteSingleMusic(baseMusicInfoInfo) {
                removeSuccessListener?.invoke(baseMusicInfoInfo)
            }
        } else {
            PlaylistManagerUtils.disCollectMusic(pid, baseMusicInfoInfo) {
                removeSuccessListener?.invoke(baseMusicInfoInfo)
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
            if (baseMusicInfoInfo?.type == Constants.VIDEO) {
                itemData.clear()
            }
            //是否显示下载歌曲Item
            if (!BuildConfig.HAS_DOWNLOAD || type == Constants.PLAYLIST_DOWNLOAD_ID) {
                itemData.remove(R.string.popup_download)
            }
            //是否有mv
            if (baseMusicInfoInfo?.hasMv == 0) {
                itemData.remove(R.string.popup_mv)
            }

            if (baseMusicInfoInfo?.type == Constants.LOCAL) {
                itemData.remove(R.string.popup_download)
                itemData.remove(R.string.popup_add_to_playlist)
            } else if (type != Constants.PLAYLIST_DOWNLOAD_ID) {
                itemData.remove(R.string.popup_detail_edit)

                if (baseMusicInfoInfo?.isDl == false || baseMusicInfoInfo?.isOnline == false) {
                    itemData.remove(R.string.popup_download)
                }

                if (type != Constants.PLAYLIST_CUSTOM_ID && type != Constants.PLAYLIST_IMPORT_ID && baseMusicInfoInfo?.isOnline == true) {
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
//                    R.drawable.ic_queue_play_next -> MusicPlayerManager.nextPlay(music)
                    R.drawable.ic_playlist_add -> {
                        if (baseMusicInfoInfo?.type != Constants.LOCAL) {
                            PlaylistManagerUtils.addToPlaylist(mContext, baseMusicInfoInfo)
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
                        turnToDelete(baseMusicInfoInfo)
                    }
                    R.drawable.ic_video_label -> {
                        if (baseMusicInfoInfo?.type == Constants.BAIDU || baseMusicInfoInfo?.type == Constants.VIDEO) {
                            startActivity<VideoPlayerActivity>(Extras.MV_ID to baseMusicInfoInfo?.mid)
                        } else {

                        }
                    }
                    R.drawable.item_download -> {
                        if (baseMusicInfoInfo?.type != Constants.LOCAL) {
                            QualitySelectDialog.newInstance(baseMusicInfoInfo).apply {
                                isDownload = true
                            }.show(mContext)
                        }
                    }
                    R.drawable.ic_share_black -> {
                        Tools.qqShare(mContext, MusicPlayerManager.getInstance().getNowPlayingMusic())
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
