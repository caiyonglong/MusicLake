package com.cyl.musicapi.netease

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Created by yonglong on 2017/9/11.
 */

interface NeteaseApiService {
    //    @Headers({"referer: http://music.163.com"})
    @GET("top/playlist")
    fun getTopPlaylist(@Query("cat") cat: String? = null, @Query("limit") limit: Int): Observable<NeteasePlaylist>

    @GET("/top/playlist/highquality")
    fun getTopPlaylistHigh(@QueryMap map: MutableMap<String, Any>): Observable<NeteasePlaylist>

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
    @GET
    fun searchNetease(@Url url: String): Observable<SearchInfo>

    /**
     * 获取mv排行榜
     */
    @GET("/top/mv")
    fun getTopMv(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<MvInfo>

    @GET("/mv/detail")
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

    @GET("login/cellphone")
    fun loginPhone(@Query("phone") phone: String, @Query("password") password: String): Observable<LoginInfo>

    @GET("login")
    fun loginEmail(@Query("email") email: String, @Query("password") password: String): Observable<LoginInfo>

    /**
     * 获取登录状态
     */
    @GET("/login/status")
    fun getLoginStatus(): Observable<LoginInfo>

    /**
     * 获取每日推荐歌曲（需登录）
     */
    @GET("recommend/songs")
    fun recommendSongs(): Observable<RecommendSongsInfo>

    /**
     * 获取每日推荐歌单（需登录）
     */
    @GET("recommend/resource")
    fun recommendPlaylist(): Observable<RecommendPlaylist>

    /**
     * 获取推荐歌单
     */
    @GET("/personalized")
    fun personalizedPlaylist(): Observable<PersonalizedInfo>

    /**
     * 获取推荐Mv
     */
    @GET("/personalized/mv")
    fun personalizedMv(): Observable<PersonalizedInfo>

    /**
     * 获取用户歌单
     */
    @GET("/user/playlist")
    fun getUserPlaylist(@Query("uid") uid: String): Observable<NeteasePlaylist>

}
