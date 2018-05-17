package com.cyl.musiclake.api.qq;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by D22434 on 2018/1/5.
 */

public interface QQApiService {
    //http://c.y.qq.com/soso/fcgi-bin/search_cp?
    /*
        'p': page,
        'n': limit,
        'w': key,
        'aggr': 1,
        'lossless': 1,
        'cr': 1
     */
    @GET
    Observable<String> getSongUrl(@Url String baseUrl);

    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET("soso/fcgi-bin/search_cp?")
    Observable<QQApiModel> searchByQQ(@QueryMap Map<String, String> params);

    //    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET
    Observable<QQApiKey> getTokenKey(@Url String baseUrl);


    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET
    Observable<QQLyricInfo> getQQLyric(@Url String baseUrl);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);

}
