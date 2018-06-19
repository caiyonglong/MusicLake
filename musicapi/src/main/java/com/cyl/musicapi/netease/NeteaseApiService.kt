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
