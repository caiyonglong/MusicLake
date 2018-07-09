package com.cyl.musicapi.netease

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by yonglong on 2017/9/11.
 */

interface NeteaseApiService {

    //    @Headers({"referer: http://music.163.com"})
    @GET("top/playlist/highquality")
    fun neteasePlaylist(): Observable<NeteasePlaylist>

    //    @Headers({"referer: http://music.163.com"})
    @GET("/toplist/artist")
    fun getTopArtists(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<ArtistsInfo>


    @GET("/top/mv")
    fun getTopMv(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<MvInfo>

    @GET("/mv")
    fun getMvDetailInfo(@Query("mvid") mvid: String): Observable<MvDetailInfo>


    @GET("simi/mv")
    fun getSimilarMv(@Query("mvid") mvid: String): Observable<SimilarMvInfo>

    @GET("comment/mv")
    fun getMvComment(@Query("id") id: String): Observable<MvComment>

    //    @Headers({"referer: http://music.163.com"})

    //
//    //    @Headers({"referer: http://music.163.com"})
//    @GET("music/url?")
//    fun getMusicUrl(@Query("id") id: String): Observable<NeteaseMusicUrl>
//
//    //
////    //    @Headers({"referer: http://music.163.com"})
//    @GET("lyric")
//    fun getMusicLyric(@Query("id") id: String): Observable<NeteaseLyric>


}
