package com.cyl.musiclake.api.music.doupan

import com.cyl.musiclake.api.net.ApiManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.*

/**
 * Author   : D22434
 * version  : 2018/3/30
 * function :
 */

object DoubanApiServiceImpl {

    private val TAG = "DoubanApiServiceImpl"

    //https://api.douban.com/v2/music/search?q=素颜&count=1
    fun getDoubanPic(info: String): Observable<String> {
        val params = HashMap<String, String>()
        params["q"] = info
        params["count"] = "10"
        //        return ApiManager.getInstance().create(DoubanApiService.class, "https://api.douban.com/")
        return ApiManager.getInstance().create(DoubanApiService::class.java, "https://douban.uieee.com/")
                .searchMusic("search", params).flatMap {
                    Observable.create(ObservableOnSubscribe<String> { e ->
                        try {
                            if (it.musics?.first() != null) {
                                e.onNext(it.musics.first().image)
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
