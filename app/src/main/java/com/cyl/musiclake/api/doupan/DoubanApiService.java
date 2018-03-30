package com.cyl.musiclake.api.doupan;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Author   : D22434
 * version  : 2018/3/30
 * function :
 */

public interface DoubanApiService {

    @GET("v2/music/{method}")
    Observable<DoubanMusic> searchMusic(@Path("method") String method, @QueryMap Map<String, String> params);

}
