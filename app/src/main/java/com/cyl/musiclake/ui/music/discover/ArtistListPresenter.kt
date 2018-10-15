package com.cyl.musiclake.ui.music.discover

import com.cyl.musiclake.api.MusicApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.utils.ToastUtils
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class ArtistListPresenter @Inject
constructor() : BasePresenter<ArtistListContract.View>(), ArtistListContract.Presenter {
    override fun loadArtists(offset: Int, params: Any) {
        ApiManager.request(MusicApiServiceImpl.getArtists(offset, params), object : RequestCallBack<MutableList<Artist>> {
            override fun success(result: MutableList<Artist>) {
                mView?.showArtistList(result)
            }

            override fun error(msg: String) {
                ToastUtils.show(msg)
            }
        })
    }

}
