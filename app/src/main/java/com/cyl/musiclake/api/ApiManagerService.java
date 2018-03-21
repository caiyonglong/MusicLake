package com.cyl.musiclake.api;

import com.cyl.musiclake.api.baidu.BaiduMusicList;
import com.cyl.musiclake.api.baidu.BaiduSongInfo;
import com.cyl.musiclake.api.baidu.BaiduSongList;
import com.cyl.musiclake.api.baidu.OnlineArtistInfo;
import com.cyl.musiclake.api.netease.NeteaseBase;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusicUrl;
import com.cyl.musiclake.api.qq.QQApiKey;
import com.cyl.musiclake.api.qq.QQApiModel;
import com.cyl.musiclake.api.qq.QQLyricInfo;
import com.cyl.musiclake.api.xiami.XiamiModel;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.map.location.Location;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
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
    Observable<QQApiModel> searchByQQ(@Url String baseUrl, @QueryMap Map<String, String> params);

    @Headers({"referer: http://h.xiami.com/"})
    @GET
    Observable<XiamiModel> searchByXiaMi(@Url String baseUrl, @QueryMap Map<String, String> params);

    //    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET
    Observable<QQApiKey> getTokenKey(@Url String baseUrl);


    @Headers({"referer: https://y.qq.com/portal/player.html"})
    @GET
    Observable<QQLyricInfo> getQQLyric(@Url String baseUrl);

    //    @Headers({"referer: http://h.xiami.com/"})
    @GET
    Observable<ResponseBody> getXiamiLyric(@Url String baseUrl);

    //    @Headers({"referer: http://h.xiami.com/"})
    @GET
    Observable<ResponseBody> getBaiduLyric(@Url String baseUrl);

    @POST
    Observable<ApiModel<User>> getUserInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<ApiModel<List<Location>>> getNearPeopleInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<OnlineArtistInfo> getArtistInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<BaiduMusicList> getOnlinePlaylist(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<BaiduSongList> getOnlineSongs(@Url String baseUrl, @QueryMap Map<String, String> params);

    @GET
    Observable<BaiduSongInfo> getTingSongInfo(@Url String baseUrl, @QueryMap Map<String, String> params);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String downloadUrl, @HeaderMap Map<String, String> params);

    @Headers({"referer: http://music.163.com"})
    @GET()
    Observable<NeteaseBase<NeteaseList>> getTopList(@Url String Url);

    @Headers({"referer: http://music.163.com"})
    @GET()
    Observable<NeteaseMusicUrl> getMusicUrl(@Url String Url);

}
