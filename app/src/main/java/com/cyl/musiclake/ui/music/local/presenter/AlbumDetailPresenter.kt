package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.ui.music.local.contract.AlbumDetailContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class AlbumDetailPresenter @Inject
constructor() : BasePresenter<AlbumDetailContract.View>(), AlbumDetailContract.Presenter {

    override fun loadAlbumSongs(albumName: String) {
        doAsync {
            val data = SongLoader.getSongsForAlbum(albumName)
            uiThread {
                mView.showAlbumSongs(data)
            }
        }
//        CoverLoader.loadImageViewByDouban(MusicApp.getAppContext(), albumName, null) { resource -> mView.showAlbumArt(resource) }
    }

}
