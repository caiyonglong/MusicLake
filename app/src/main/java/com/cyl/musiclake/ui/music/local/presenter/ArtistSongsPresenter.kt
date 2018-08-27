package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.ui.music.local.contract.ArtistSongContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class ArtistSongsPresenter @Inject
constructor() : BasePresenter<ArtistSongContract.View>(), ArtistSongContract.Presenter {
    override fun loadSongs(artistName: String) {
        mView.showLoading()
        doAsync {
            val data = SongLoader.getSongsForArtist(artistName)
            uiThread {
                mView.showSongs(data)
            }
        }
//        CoverLoader.loadImageViewByDouban(MusicApp.getAppContext(), artistName, null) { bitmap -> mView.showAlbumArt(bitmap) }
    }

}
