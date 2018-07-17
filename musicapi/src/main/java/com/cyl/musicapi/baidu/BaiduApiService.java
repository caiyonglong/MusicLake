package com.cyl.musicapi.baidu;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yonglong on 2018/1/21.
 */

public interface BaiduApiService {

    @GET()
    Observable<ResponseBody> getBaiduLyric(@Url String baseUrl);

    @GET("v1/restserver/ting?")
    Observable<BaiduArtistInfo> getArtistInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET("v1/restserver/ting?")
    Observable<BaiduList> getOnlinePlaylist(@QueryMap Map<String, String> params);

    @GET("v1/restserver/ting?")
    Observable<BaiduMusicList> getOnlineSongs(@QueryMap Map<String, String> params);

    //    http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.suggestion&query=&format=json&from=ios&version=2.1.1
    @GET("v1/restserver/ting?")
    Observable<Suggestion> getSearchSuggestion(@QueryMap Map<String, String> params);

    @GET("v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.radio.getCategoryList&format=json")
    Observable<RadioData> getRadioChannels();

    @GET("/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.radio.getChannelSong&format=json&pn=0&rn=10")
    Observable<RadioChannelData> getRadioChannelSongs(@Query("channelname") String channelName);

    @GET()
    Observable<BaiduSongInfo> getTingSongInfo(@Url String baseUrl);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);
}
