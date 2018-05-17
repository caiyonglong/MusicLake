package com.cyl.musiclake.net;

import com.cyl.musiclake.api.ApiModel;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.ui.my.user.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yonglong on 2017/9/11.
 */

public interface ApiManagerService {

    @POST
    Observable<ApiModel<User>> getUserInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<ApiModel<List<Location>>> getNearPeopleInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);

}
