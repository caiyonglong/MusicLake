package com.cyl.musiclake.api.netease

import com.cyl.musicapi.netease.NeteaseApiService
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.db.ArtistBean
import com.cyl.musiclake.net.ApiManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by D22434 on 2018/1/5.
 */

object NeteaseApiServiceImpl {
    private val TAG = "NeteaseApiServiceImpl"

    val apiService: NeteaseApiService
        get() = ApiManager.getInstance().create(NeteaseApiService::class.java, Constants.BASE_NETEASE_URL)


    /**
     * 获取歌单歌曲
     */
    fun getTopArtists(limit: Int, offset: Int): Observable<List<ArtistBean>> {
        return apiService.getTopArtists(offset, limit)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<List<ArtistBean>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<ArtistBean>()
                                it.list.artists?.forEach {
                                    val artistsBean = ArtistBean()
                                    artistsBean.id = it.id.toLong()
                                    artistsBean.name = it.name
                                    artistsBean.picUrl = it.picUrl
                                    artistsBean.score = it.score
                                    artistsBean.albumSize = it.albumSize
                                    list.add(artistsBean)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

}
