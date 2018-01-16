package com.cyl.musiclake.api;

import com.cyl.musiclake.api.qq.QQApiKey;
import com.cyl.musiclake.api.qq.QQApiModel;
import com.cyl.musiclake.api.qq.QQLyricInfo;
import com.cyl.musiclake.api.xiami.XiamiModel;
import com.cyl.musiclake.ui.download.download.DownloadInfo;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineArtistInfo;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicList;
import com.cyl.musiclake.ui.onlinemusic.model.OnlinePlaylists;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by yonglong on 2017/9/11.
 */

public interface ApiManagerService {

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
    @GET
    Observable<QQApiModel> searchByQQ(@Url String baseUrl, @QueryMap Map<String, Object> params);

    @Headers({"referer: http://h.xiami.com/"})
    @GET
    Observable<XiamiModel> searchByXiaMi(@Url String baseUrl, @QueryMap Map<String, Object> params);

//    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET
    Observable<QQApiKey> getTokenKey(@Url String baseUrl);


    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET
    Observable<QQLyricInfo> getQQLyric(@Url String baseUrl);

    @POST
    Observable<ApiModel<User>> getUserInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<ApiModel<List<Location>>> getNearPeopleInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<ApiModel<OnlineArtistInfo>> getArtistInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET("ting")
    Observable<OnlinePlaylists> getOnlinePlaylist(@QueryMap Map<String, String> params);

    @GET("ting")
    Observable<OnlineMusicList> getOnlineSongs(@QueryMap Map<String, String> params);

    @GET("ting")
    Observable<DownloadInfo> getTingSongInfo(@QueryMap Map<String, String> params);

}
