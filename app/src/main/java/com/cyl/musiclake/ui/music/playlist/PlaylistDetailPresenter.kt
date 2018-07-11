package com.cyl.musiclake.ui.music.playlist

import com.cyl.musiclake.RxBus
import com.cyl.musiclake.api.MusicApiServiceImpl
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.PlaylistLoader
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist
import com.cyl.musiclake.db.Album
import com.cyl.musiclake.db.Artist
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.event.StatusChangedEvent
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class PlaylistDetailPresenter @Inject
constructor() : BasePresenter<PlaylistDetailContract.View>(), PlaylistDetailContract.Presenter {


    private val netease = ArrayList<String>()
    private val qq = ArrayList<String>()
    private val xiami = ArrayList<String>()


    override fun attachView(view: PlaylistDetailContract.View?) {
        super.attachView(view)
        val tt = RxBus.getInstance().register(StatusChangedEvent::class.java)
                .compose(mView.bindToLife())
                .subscribe { mView.changePlayStatus(it.isPrepared) }
        disposables.add(tt)
    }

    override fun loadArtistSongs(artist: Artist) {
        if (artist.type == null || artist.type == Constants.LOCAL || artist.type == Constants.BAIDU) {
            mView?.showEmptyState()
            return
        }
        val observable = MusicApiServiceImpl.getArtistSongs(artist.type!!, artist.id.toString(), 50, 0)
        ApiManager.request(observable, object : RequestCallBack<Artist> {
            override fun success(result: Artist) {
                val musicLists = result.songs
                val iterator = musicLists.iterator()
                while (iterator.hasNext()) {
                    val temp = iterator.next()
                    if (temp.isCp) {
                        //list.remove(temp);// 出现java.util.ConcurrentModificationException
                        iterator.remove()// 推荐使用
                    }
                }
                mView?.showPlaylistSongs(musicLists)
            }

            override fun error(msg: String) {
                LogUtil.e(TAG, msg)
                mView?.showError(msg, true)
                ToastUtils.show(msg)
            }
        })
    }

    override fun loadAlbumSongs(album: Album) {
        val observable = MusicApiServiceImpl.getAlbumSongs(Constants.NETEASE, album.id.toString(), 30, 0)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Music>> {
            override fun success(result: MutableList<Music>) {
                val iterator = result.iterator()
                while (iterator.hasNext()) {
                    val temp = iterator.next()
                    if (temp.isCp) {
                        //list.remove(temp);// 出现java.util.ConcurrentModificationException
                        iterator.remove()// 推荐使用
                    }
                }
                mView?.showPlaylistSongs(result)
            }

            override fun error(msg: String) {
                LogUtil.e(TAG, msg)
                mView?.showError(msg, true)
                ToastUtils.show(msg)
            }
        })
    }


    override fun loadPlaylistSongs(playlist: Playlist) {
        if (playlist.type == 0) {
            doAsync {
                val data = playlist.pid?.let { PlaylistLoader.getMusicForPlaylist(it, playlist.order) }
                uiThread {
                    mView.showPlaylistSongs(data)
                }
            }
            return
        }
        ApiManager.request(playlist.pid?.let { PlaylistApiServiceImpl.getMusicList(it) }, object : RequestCallBack<MutableList<Music>> {
            override fun success(result: MutableList<Music>) {
                //                mView.showPlaylistSongs(result);
//                if (result.isEmpty()) {
                val iterator = result.iterator()
                while (iterator.hasNext()) {
                    val temp = iterator.next()
                    if (temp.isCp) {
                        //list.remove(temp);// 出现java.util.ConcurrentModificationException
                        iterator.remove()// 推荐使用
                    }
                }
                mView?.showPlaylistSongs(result)
//                    return
//                }
//                netease.clear()
//                qq.clear()
//                xiami.clear()
//                for (music in result) {
//                    when {
//                        music.type == Constants.NETEASE -> music.mid?.let { netease.add(it) }
//                        music.type == Constants.QQ -> music.mid?.let { qq.add(it) }
//                        music.type == Constants.XIAMI -> music.mid?.let { xiami.add(it) }
//                    }
//                }
//                getBatchSongDetail("netease", netease.toTypedArray())
//                getBatchSongDetail("qq", qq.toTypedArray())
//                getBatchSongDetail("xiami", xiami.toTypedArray())
            }

            override fun error(msg: String) {
                LogUtil.e(TAG, msg)
                mView?.showError(msg, true)
                ToastUtils.show(msg)
            }
        })
    }

    override fun deletePlaylist(playlist: Playlist) {
        ApiManager.request(playlist.pid?.let { PlaylistApiServiceImpl.deletePlaylist(it) }, object : RequestCallBack<String> {
            override fun success(result: String) {
                mView.success(1)
                RxBus.getInstance().post(PlaylistEvent(Constants.PLAYLIST_CUSTOM_ID))
                ToastUtils.show(result)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    override fun renamePlaylist(playlist: Playlist, title: String) {
        ApiManager.request(playlist.pid?.let { PlaylistApiServiceImpl.renamePlaylist(it, title) }, object : RequestCallBack<String> {
            override fun success(result: String) {
                mView.success(1)
                RxBus.getInstance().post(PlaylistEvent(Constants.PLAYLIST_CUSTOM_ID))
                ToastUtils.show(result)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    override fun disCollectMusic(pid: String, position: Int, music: Music) {
        ApiManager.request(PlaylistApiServiceImpl.disCollectMusic(pid, music), object : RequestCallBack<String> {
            override fun success(result: String) {
                mView?.removeMusic(position)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    fun getBatchSongDetail(vendor: String, ids: Array<String>) {
        ApiManager.request(MusicApiServiceImpl.getBatchMusic(vendor, ids), object : RequestCallBack<MutableList<Music>> {
            override fun success(result: MutableList<Music>) {
                mView.showPlaylistSongs(result)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

    companion object {
        private val TAG = "PlaylistDetailPresenter"
    }
}
