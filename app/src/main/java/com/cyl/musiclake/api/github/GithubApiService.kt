package com.cyl.musiclake.api.github


import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by master on 2019/6/28.
 * des :github后台
 *
 */

interface GithubApiService {

    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
            @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String,
            @Query("code") code: String,
            @Query("state") state: String
    ): Observable<Response<OauthToken>>
}

class OauthToken {
    @SerializedName("access_token")
    var accessToken: String? = null
    var scope: String? = null
}