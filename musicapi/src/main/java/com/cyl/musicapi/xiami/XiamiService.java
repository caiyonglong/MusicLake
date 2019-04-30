package com.cyl.musicapi.xiami;

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
 * Created by yonglong on 2018/1/15.
 */

public interface XiamiService {

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

    @Headers({"referer: http://h.xiami.com/"})
    @GET("web?")
    Observable<XiamiModel> searchByXiaMi(@QueryMap Map<String, String> params);

    //    @Headers({"referer: http://h.xiami.com/"})
    @GET
    Observable<ResponseBody> getXiamiLyric(@Url String baseUrl);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);

}
