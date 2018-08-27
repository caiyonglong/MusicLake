//package com.cyl.musiclake.api.baidu;
//
//import java.util.Map;
//
//import io.reactivex.Observable;
//import okhttp3.ResponseBody;
//import retrofit2.http.GET;
//import retrofit2.http.HeaderMap;
//import retrofit2.http.QueryMap;
//import retrofit2.http.Streaming;
//import retrofit2.http.Url;
//
///**
// * Created by yonglong on 2018/1/21.
// */
//
//public interface BaiduApiService {
//
//    //    @Headers({"referer: http://h.xiami.com/"})
//
//    @GET()
//    Observable<ResponseBody> getBaiduLyric(@Url String baseUrl);
//
//    @GET("v1/restserver/ting?")
//    Observable<OnlineArtistInfo> getArtistInfo(@Url String baseUrl, @QueryMap Map<String, String> params);
//
//    @GET("v1/restserver/ting?")
//    Observable<BaiduMusicList> getOnlinePlaylist(@QueryMap Map<String, String> params);
//
//    @GET("v1/restserver/ting?")
//    Observable<BaiduSongList> getOnlineSongs(@QueryMap Map<String, String> params);
//
//    //    http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.suggestion&query=&format=json&from=ios&version=2.1.1
//    @GET("v1/restserver/ting?")
//    Observable<Suggestion> getSearchSuggestion(@QueryMap Map<String, String> params);
//
//    @GET()
//    Observable<BaiduSongInfo> getTingSongInfo(@Url String baseUrl, @QueryMap Map<String, String> params);
//
//    @Streaming
//    @GET
//    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);
//}
