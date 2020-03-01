package com.cyl.musiclake.ui.music.artist.presenter

import com.cyl.musiclake.ui.base.BasePresenter
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.ui.music.artist.contract.ArtistInfoContract

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class ArtistInfoPresenter @Inject
constructor() : BasePresenter<ArtistInfoContract.View>(), ArtistInfoContract.Presenter {

    override fun loadArtistInfo(baseMusicInfoInfo: BaseMusicInfo) {
        val info = baseMusicInfoInfo.title + "-" + baseMusicInfoInfo.artist

    }
}
