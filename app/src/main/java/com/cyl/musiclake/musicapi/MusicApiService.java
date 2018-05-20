package com.cyl.musiclake.musicapi;

import com.cyl.musiclake.api.ApiModel;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.musicapi.playlist.CollectionInfo;
import com.cyl.musiclake.musicapi.playlist.PlaylistInfo;
import com.cyl.musiclake.ui.my.user.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by master on 2018/4/5.
 */

public interface MusicApiService {
    /**
     * 获取歌单
     *
     * @param token
     * @return
     */
    @GET("playlist")
    Observable<ApiModel<List<Playlist>>> getOnlinePlaylist(@Header("accesstoken") String token);

    @GET("playlist/{id}")
    Observable<ApiModel<List<MusicInfo>>> getMusicList(@Header("accesstoken") String token
            , @Path("id") String id);

    @DELETE("playlist/{id}")
    Observable<ApiModel<Integer>> deleteMusic(@Header("accesstoken") String token
            , @Path("id") String id);


    @PUT("playlist/{id}")
    @Headers({"Content-Type: application/json"})
    Observable<ApiModel> renameMusic(@Header("accesstoken") String token
            , @Path("id") String id, @Body PlaylistInfo playlist);

    /**
     * 新建歌单
     *
     * @param token
     * @return
     */
    @POST("playlist")
    @Headers({"Content-Type: application/json"})
    Observable<ApiModel<Playlist>> createPlaylist(@Header("accesstoken") String token, @Body PlaylistInfo playlist);

    /**
     * 收藏歌曲
     *
     * @param token
     * @return
     */
    @POST("playlist/{id}")
    @Headers({"Content-Type: application/json"})
    Observable<ApiModel<Playlist>> collectMusic(@Header("accesstoken") String token, @Path("id") String id, @Body CollectionInfo collectionInfo);

    /**
     * 取消收藏歌曲
     *
     * @param token
     * @return
     */
    @DELETE("playlist/unCollection/{id}")
    Observable<ApiModel<Playlist>> uncollectMusic(@Header("accesstoken") String token, @Path("id") String id, @Query("song_id") String songid, @Query("vendor") String vendor);

    /**
     * 获取用户信息
     *
     * @param token
     * @param openid
     * @return
     */
    //https://player-node.zzsun.cc/auth/qq/android?access_token=9D6F0084618AACDE881FDCA267F5CFDD&openid=35300E15E9E245DF0B04031EF6032CD6
    @GET("auth/qq/android")
    Observable<ApiModel<User>> getUserInfo(@Query("access_token") String token,
                                           @Query("openid") String openid);


}
