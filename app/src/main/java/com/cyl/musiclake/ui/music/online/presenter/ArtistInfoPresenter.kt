package com.cyl.musiclake.ui.music.online.presenter

import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.api.doupan.DoubanMusic
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.ui.music.online.contract.ArtistInfoContract

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by D22434 on 2018/1/4.
 */

class ArtistInfoPresenter @Inject
constructor() : BasePresenter<ArtistInfoContract.View>(), ArtistInfoContract.Presenter {

    override fun loadArtistInfo(music: Music) {
        val info = music.title + "-" + music.artist

    }
}
