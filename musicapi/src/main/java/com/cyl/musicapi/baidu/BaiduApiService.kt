package com.cyl.musicapi.baidu

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by yonglong on 2018/1/21.
 */

interface BaiduApiService {

    @GET("v1/restserver/ting?from=qianqian&version=5.6.5.0&method=baidu.ting.radio.getCategoryList&format=json")
    fun getRadioChannels(): Observable<RadioData>

    @GET
    fun getBaiduLyric(@Url baseUrl: String): Observable<ResponseBody>

    @GET("v1/restserver/ting?")
    fun getArtistInfo(@Url baseUrl: String, @QueryMap params: Map<String, String>): Observable<BaiduArtistInfo>


    @GET("v1/restserver/ting?")
    fun getOnlinePlaylist(@QueryMap params: Map<String, String>): Observable<BaiduList>

    @GET("v1/restserver/ting?")
    fun getOnlineSongs(@QueryMap params: Map<String, String>): Observable<BaiduMusicList>

    //http://musicapi.qianqian.com/v1/restserver/ting?from=android&version=5.6.5.0&method=baidu.ting.search.suggestion&query=Music&page_no=1&page_size=1&type=-1&data_source=0&use_cluster=1
    @GET("v1/restserver/ting?")
    fun getSearchSuggestion(@QueryMap params: Map<String, String>): Observable<Suggestion>

    @GET("v1/restserver/ting?from=android&version=5.6.5.0&method=baidu.ting.search.merge&type=-1&data_source=0&use_cluster=1")
    fun getSearchMerge(@QueryMap params: MutableMap<String, Any>): Observable<BaiduSearchMergeInfo>

    @GET("/v1/restserver/ting?from=qianqian&version=5.6.5.0&method=baidu.ting.radio.getChannelSong&format=json&pn=0&rn=10")
    fun getRadioChannelSongs(@Query("channelname") channelName: String): Observable<RadioChannelData>

    @GET
    fun getTingSongInfo(@Url baseUrl: String): Observable<BaiduSongInfo>

    @Streaming
    @GET
    fun downloadFile(@Url downloadUrl: String, @HeaderMap params: Map<String, String>): Observable<ResponseBody>

    /**
     * 获取专辑信息
     */
    @GET("/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json")
    fun getAlbumSongList(@Query("album_id") albumId: String): Observable<AlbumSongList>

    /**
     * 获取歌手歌曲信息
     */
    @GET("/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.getSongList&format=json")
    fun getArtistSongList(@QueryMap params: MutableMap<String, Any>): Observable<ArtistMusicList>
}
