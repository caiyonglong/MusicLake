package com.cyl.musiclake.api.netease;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;

/**
 * Created by D22434 on 2018/1/5.
 */

public interface NeteaseApi {
    @GET("top/list")
    Observable<NeteaseBase<NeteaseList>> getTopList(@Field("idx") int id);

    @GET("search")
    Observable<NeteaseBase<NeteaseList>> getTopList(@Field("keywords") String key);
}
