package com.cyl.musiclake.api.netease;

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
 * Created by yonglong on 2017/9/11.
 */

public interface NeteaseApiService {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);

//    @Headers({"referer: http://music.163.com"})
    @GET("top/list?")
    Observable<NeteaseBase<NeteaseList>> getTopList(@Query("idx") int idx);


//    @Headers({"referer: http://music.163.com"})
    @GET("music/url?")
    Observable<NeteaseMusicUrl> getMusicUrl(@Query("id") String song_id);

//    @Headers({"referer: http://music.163.com"})
    @GET("lyric")
    Observable<NeteaseLyric> getMusicLyric(@Query("id") String id);

//    @Headers({"referer: http://music.163.com"})
    @GET("search")
    Observable<NeteaseBase<NeteaseSearchMusic>> searchByNetease(@QueryMap Map<String, String> params);

//    @Headers({"referer: http://music.163.com"})
    @GET("top/playlist/highquality")
    Observable<NeteasePlaylist> getNeteasePlaylist();
}
