package com.cyl.musicapi

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import com.cyl.musicapi.bean.*
import com.google.gson.Gson
import org.json.JSONObject
import wendu.dsbridge.CompletionHandler
import wendu.dsbridge.DWebView

/**
 * Created by master on 2018/5/15.
 * 调用js方法请求数据
 * 原理js拼接url,接收返回参数，然后数据处理再返回到java中。
 */
class BaseApiImpl private constructor(val context: Context) {

    private val gson = Gson()

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var instance: BaseApiImpl? = null
        var mWebView: DWebView? = null

        fun getInstance(context: Context): BaseApiImpl {
            if (instance == null) {
                synchronized(BaseApiImpl::class) {
                    if (instance == null) {
                        instance = BaseApiImpl(context)
                    }
                    if (mWebView == null) {
                        mWebView = DWebView(context)
                        mWebView?.addJavascriptObject(object : Any() {
                            @JavascriptInterface
                            fun onAjaxRequest(requestData: Any, handler: CompletionHandler<*>) {
                                AjaxHandler.onAjaxRequest(requestData as JSONObject, handler)
                            }
                        }, null)
                        mWebView?.loadUrl("file:///android_asset/musicApi.html")
                    }
                }
            }
            return instance!!
        }
    }


    /**
     * 搜索
     *
     * @param query
     */
    fun searchSong(query: String, limit: Int, offset: Int, success: (result: SearchData) -> Unit) {
        mWebView?.callHandler("asyn.searchSong", arrayOf(query, limit, offset)) { retValue: JSONObject ->
            val result = gson.fromJson<SearchData>(retValue.toString(), SearchData::class.java)
            success.invoke(result)
        }
//        when (type) {
//            "ANY" -> {
//                mWebView?.callHandler("asyn.searchSong", arrayOf(query, limit, offset), { retValue: JSONObject ->
//                    val result = gson.fromJson<SearchData>(retValue.toString(), SearchData::class.java)
//                    success.invoke(result)
//                })
//            }
//            "QQ" -> {
//                mWebView?.callHandler("asyn.searchQQSong", arrayOf(query, limit, offset), { retValue: JSONObject ->
//                    val result = gson.fromJson<SearchData>(retValue.toString(), SearchData::class.java)
//                    success.invoke(result)
//                })
//            }
//            "XIAMI" -> {
//                mWebView?.callHandler("asyn.searchXiamiSong", arrayOf(query, limit, offset), { retValue: JSONObject ->
//                    val result = gson.fromJson<SearchData>(retValue.toString(), SearchData::class.java)
//                    success.invoke(result)
//                })
//            }
//            "NETEASE" -> {
//                mWebView?.callHandler("asyn.searchNeteaseSong", arrayOf(query, limit, offset), { retValue: JSONObject ->
//                    val result = gson.fromJson<SearchData>(retValue.toString(), SearchData::class.java)
//                    success.invoke(result)
//                })
//            }
//        }
    }


    /**
     * 获取歌曲详情
     */
    fun getSongDetail(vendor: String, id: String, success: (result: SongDetail) -> Unit, fail: (() -> Unit)? = null) {
        mWebView?.callHandler("asyn.getSongDetail", arrayOf<Any>(vendor, id)) { retValue: JSONObject ->
            try {
                val result = gson.fromJson<SongDetail>(retValue.toString(), SongDetail::class.java)
                success.invoke(result)
            } catch (e: Throwable) {
                fail?.invoke()
            }
        }
    }

    /**
     * 批量获取歌曲详情
     * [101126,16435051,139808]
     */
    fun getBatchSongDetail(query: String, ids: Array<String>, success: (result: BatchSongDetail) -> Unit) {
        mWebView?.callHandler("asyn.getBatchSongDetail", arrayOf<Any>(query, ids)) { retValue: JSONObject ->
            val result = gson.fromJson<BatchSongDetail>(retValue.toString(), BatchSongDetail::class.java)
            success.invoke(result)
        }
    }

    fun getTopList(id: String, success: (result: NeteaseBean) -> Unit) {
        mWebView?.callHandler("asyn.getTopList", arrayOf<Any>(id)) { retValue: JSONObject ->
            val result = gson.fromJson<NeteaseBean>(retValue.toString(), NeteaseBean::class.java)
            success.invoke(result)
        }
    }

    fun getLyricInfo(vendor: String, id: String, success: (result: LyricInfo) -> Unit) {
        mWebView?.callHandler("asyn.getLyric", arrayOf<Any>(vendor, id)) { retValue: JSONObject ->
            val result = gson.fromJson<LyricInfo>(retValue.toString(), LyricInfo::class.java)
            success.invoke(result)
        }
    }

    fun getComment(vendor: String, id: String, success: (result: SongComment) -> Unit) {
        mWebView?.callHandler("asyn.getComment", arrayOf(vendor, id, 1, 10)) { retValue: JSONObject ->
            val result = gson.fromJson<SongComment>(retValue.toString(), SongComment::class.java)
            success.invoke(result)
        }
    }

    fun getSongUrl(vendor: String, id: String, success: (result: SongBean) -> Unit, fail: (() -> Unit)? = null) {
        mWebView?.callHandler("asyn.getSongUrl", arrayOf<Any>(vendor, id)) { retValue: JSONObject ->
            try {
                val result = gson.fromJson<SongBean>(retValue.toString(), SongBean::class.java)
                success.invoke(result)
            } catch (e: Throwable) {
                fail?.invoke()
            }
        }
    }
}
