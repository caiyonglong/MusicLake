package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.VideoLoader
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.music.local.contract.FolderSongsContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/8.
 */

class FolderSongPresenter @Inject
constructor() : BasePresenter<FolderSongsContract.View>(), FolderSongsContract.Presenter {

    override fun loadSongs(path: String) {
        doAsync {
            val musicList = SongLoader.getSongListInFolder(mView.context, path)
            uiThread {
                mView?.showSongs(musicList)
            }
        }
    }

    fun loadSongs(path: String, isMusic: Boolean) {
        doAsync {
            val musicList = if (isMusic) {
                SongLoader.getSongListInFolder(mView.context, path)
            } else {
                VideoLoader.getAllLocalVideos(mView.context)
            }
            uiThread {
                mView?.showSongs(musicList)
            }
        }
    }
}
