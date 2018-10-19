package com.cyl.musicapi.netease

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by yonglong on 2017/9/11.
 */

interface NeteaseApiService {

    //    @Headers({"referer: http://music.163.com"})
    @GET("top/playlist")
    fun getTopPlaylist(@Query("cat") cat: String? = null, @Query("limit") limit: Int): Observable<NeteasePlaylist>

    @GET("/playlist/detail")
    fun getPlaylistDetail(@Query("id") id: String): Observable<NeteasePlaylistDetail>

    //    @Headers({"referer: http://music.163.com"})
    @GET("/toplist/artist")
    fun getTopArtists(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<ArtistsInfo>

    /**
     * 获取最新mv
     */
    @GET("/mv/first")
    fun getNewestMv(@Query("limit") limit: Int): Observable<MvInfo>

    /**
     * 搜索
     */
    @GET("/search")
    fun searchNetease(@Query("keywords") keywords: String, @Query("limit") limit: Int, @Query("type") type: Int): Observable<SearchInfo>

    /**
     * 获取mv排行榜
     */
    @GET("/top/mv")
    fun getTopMv(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<MvInfo>

    @GET("/mv")
    fun getMvDetailInfo(@Query("mvid") mvid: String): Observable<MvDetailInfo>

    @GET("simi/mv")
    fun getSimilarMv(@Query("mvid") mvid: String): Observable<SimilarMvInfo>

    @GET("comment/mv")
    fun getMvComment(@Query("id") id: String): Observable<MvComment>

    @GET("search/hot")
    fun getHotSearchInfo(): Observable<SearchInfo>

    @GET("playlist/catlist")
    fun getCatList(): Observable<CatListBean>

    @GET("banner")
    fun getBanner(): Observable<BannerResult>

}
