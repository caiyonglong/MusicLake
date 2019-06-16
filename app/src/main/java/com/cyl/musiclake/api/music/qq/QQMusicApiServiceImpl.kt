package com.cyl.musiclake.api.music.qq

import com.cyl.musicapi.bean.Artists
import com.cyl.musicapi.qq.QQApiService
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.LogUtil
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe


/**
 * Created by D22434 on 2018/1/5.
 */

object QQMusicApiServiceImpl {
    private val TAG = "QQMusicApiServiceImpl"

    val apiService by lazy { ApiManager.getInstance().create(QQApiService::class.java, Constants.BASE_URL_QQ_MUSIC_URL) }

    /**
     * 获取歌单歌曲
     */
    fun getArtists(offset: Int, params: Map<String, Int>): Observable<Artists> {
        val data = ArtistsData(
                comm = Comm(ct = 24
                        , cv = 0
                ), singerList = SingerList(
                module = "Music.SingerListServer",
                method = "get_singer_list",
                param = Param(
                        area = params["area"] ?: -100,
                        sex = params["sex"] ?: -100,
                        genre = params["genre"] ?: -100,
                        index = params["index"] ?: -100,
                        sin = offset * 80,
                        curPage = offset + 1
                ))
        )
        LogUtil.d(TAG, MusicApp.GSON.toJson(data))
        return apiService.getQQArtists(data = MusicApp.GSON.toJson(data))
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<Artists> { e ->
                        try {
                            LogUtil.d("QQMusicApiServiceImpl", it.toString())
                            if (it.code == 0) {
                                val list = mutableListOf<Artist>()
                                e.onNext(it.singerList.data)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }


}

data class ArtistsData(
        @SerializedName("comm")
        val comm: Comm,
        @SerializedName("singerList")
        val singerList: SingerList
)

data class Comm(
        @SerializedName("ct")
        val ct: Int,
        @SerializedName("cv")
        val cv: Int
)

data class SingerList(
        @SerializedName("param")
        val `param`: Param,
        @SerializedName("method")
        val method: String,
        @SerializedName("module")
        val module: String
)

data class Param(
        @SerializedName("area")
        val area: Int,
        @SerializedName("cur_page")
        val curPage: Int,
        @SerializedName("genre")
        val genre: Int,
        @SerializedName("index")
        val index: Int,
        @SerializedName("sex")
        val sex: Int,
        @SerializedName("sin")
        val sin: Int
)
