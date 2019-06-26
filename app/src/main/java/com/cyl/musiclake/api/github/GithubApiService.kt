package com.cyl.musiclake.api.github


import com.cyl.musicapi.playlist.UserInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by master on 2018/4/5.
 * des :自有后台歌单接口
 *
 */

interface GithubApiService {

    /**
     * 获取用户信息
     *
     * @param token
     * @param openid
     * @return
     */
    @GET("login/oauth/authorize?scope=user:email")
    fun login(@Query("client_id") client_id: String?,
                     @Query("uid") openid: String): Observable<UserInfo>

}
