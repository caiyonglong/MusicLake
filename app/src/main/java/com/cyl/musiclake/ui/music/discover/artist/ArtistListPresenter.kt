package com.cyl.musiclake.ui.music.discover.artist

import com.cyl.musicapi.bean.Artists
import com.cyl.musiclake.api.music.MusicApiServiceImpl
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.api.net.RequestCallBack
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class ArtistListPresenter @Inject
constructor() : BasePresenter<ArtistListContract.View>(), ArtistListContract.Presenter {

    override fun loadArtists(offset: Int, params: Map<String, Int>) {
        mView?.showLoading()
        ApiManager.request(MusicApiServiceImpl.getArtists(offset, params), object : RequestCallBack<Artists> {
            override fun success(result: Artists) {
                val artists = mutableListOf<Artist>()
                result.singerList.forEach {
                    val artist = Artist().apply {
                        name = it.singer_name
                        artistId = it.singer_id
                        picUrl = it.singer_pic
                        type = Constants.QQ
                    }
                    artists.add(artist)
                }
                mView?.hideLoading()
                mView?.showArtistList(artists)
                mView?.showArtistTags(result.tags)
            }

            override fun error(msg: String) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }
        })
    }

}
