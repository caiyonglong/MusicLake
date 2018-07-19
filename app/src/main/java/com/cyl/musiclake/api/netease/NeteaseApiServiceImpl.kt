package com.cyl.musiclake.api.netease

import com.cyl.musicapi.netease.*
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.db.Artist
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
    fun getTopArtists(limit: Int, offset: Int): Observable<List<Artist>> {
        return apiService.getTopArtists(offset, limit)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<List<Artist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Artist>()
                                it.list.artists?.forEach {
                                    val artistsBean = Artist()
                                    artistsBean.id = it.id.toLong()
                                    artistsBean.name = it.name
                                    artistsBean.picUrl = it.picUrl
                                    artistsBean.score = it.score
                                    artistsBean.albumSize = it.albumSize
                                    artistsBean.type = Constants.NETEASE
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

    /**
     * 获取推荐mv
     */
    fun getNewestMv(limit: Int): Observable<MvInfo> {
        return apiService.getNewestMv(limit)
    }

    /**
     * 获取推荐mv
     */
    fun getTopMv(limit: Int, offset: Int): Observable<MvInfo> {
        return apiService.getTopMv(offset, limit)
    }

    /**
     * 获取mv信息
     */
    fun getMvDetailInfo(mvid: String): Observable<MvDetailInfo> {
        return apiService.getMvDetailInfo(mvid)
    }

    /**
     * 获取相似mv
     */
    fun getSimilarMv(mvid: String): Observable<SimilarMvInfo> {
        return apiService.getSimilarMv(mvid)
    }

    /**
     * 获取mv评论
     */
    fun getMvComment(mvid: String): Observable<MvComment> {
        return apiService.getMvComment(mvid)
    }
}
