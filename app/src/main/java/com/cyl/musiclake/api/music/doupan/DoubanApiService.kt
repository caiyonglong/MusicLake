package com.cyl.musiclake.api.music.doupan

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Author   : D22434
 * version  : 2018/3/30
 * function :
 */

interface DoubanApiService {
    @GET("v2/music/{method}")
    fun searchMusic(@Path("method") method: String, @QueryMap params: Map<String, String>): Observable<DoubanMusic>
}

