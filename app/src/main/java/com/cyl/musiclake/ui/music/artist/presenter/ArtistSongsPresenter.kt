package com.cyl.musiclake.ui.music.artist.presenter

import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.music.artist.contract.ArtistSongContract
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class ArtistSongsPresenter @Inject
constructor() : BasePresenter<ArtistSongContract.View>(), ArtistSongContract.Presenter {
    override fun loadSongs(artistName: String) {
    }

}
