package com.cyl.musicapi

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import com.cyl.musicapi.bean.SearchData
import com.cyl.musicapi.bean.SongDetail
import com.cyl.musiclake.musicapi.bean.LyricInfo
import com.cyl.musiclake.musicapi.bean.NeteaseBean
import com.cyl.musiclake.musicapi.bean.SongBean
import com.cyl.musiclake.musicapi.bean.SongComment
import com.google.gson.Gson
import org.json.JSONObject
import wendu.dsbridge.CompletionHandler
import wendu.dsbridge.DWebView

/**
 * Created by master on 2018/5/15.
 * 调用js方法请求数据
 */
class BaseApiImpl(val context: Context) {

    private val mWebView: DWebView = DWebView(context)
    private val gson = Gson()

    init {
        DWebView.setWebContentsDebuggingEnabled(true)

        mWebView.addJavascriptObject(object : Any() {
            @JavascriptInterface
            fun onAjaxRequest(requestData: Any, handler: CompletionHandler<*>) {
                AjaxHandler.onAjaxRequest(requestData as JSONObject, handler)
            }
        }, null)
        mWebView.loadUrl("file:///android_asset/musicApi.html")
    }

    /**
     * 搜索
     *
     * @param query
     */
    fun searchSong(query: String, success: (result: SearchData) -> Unit) {
        mWebView.callHandler("asyn.searchSong", arrayOf(query, 10, 1), { retValue: JSONObject ->
            val result = gson.fromJson<SearchData>(retValue.toString(), SearchData::class.java)
            success.invoke(result)
        })
    }

    /**
     * 获取歌曲详情
     */
    fun getSongDetail(query: String, id: String, success: (result: SongDetail) -> Unit) {
        mWebView.callHandler("asyn.getSongDetail", arrayOf<Any>(query, id), { retValue: JSONObject ->
            val result = gson.fromJson<SongDetail>(retValue.toString(), SongDetail::class.java)
            success.invoke(result)

        })
    }

    fun getTopList(id: String, success: (result: NeteaseBean) -> Unit) {
        mWebView.callHandler("asyn.getTopList", arrayOf<Any>(id), { retValue: JSONObject ->
            val result = gson.fromJson<NeteaseBean>(retValue.toString(), NeteaseBean::class.java)
            success.invoke(result)
        })
    }

    fun getLyricInfo(vendor: String, id: String, success: (result: JSONObject) -> Unit) {
        mWebView.callHandler("asyn.getLyric", arrayOf<Any>(vendor, id), { retValue: JSONObject ->
            //            Log.e("TAG", retValue.toString())
            success.invoke(retValue)
//            val result = gson.fromJson<LyricInfo>(retValue.toString(), LyricInfo::class.java)
//            success.invoke(result)
        })
    }

    fun getComment(vendor: String, id: String, success: (result: SongComment) -> Unit) {
        mWebView.callHandler("asyn.getComment", arrayOf(vendor, id, 1, 10), { retValue: JSONObject ->
            val result = gson.fromJson<SongComment>(retValue.toString(), SongComment::class.java)
            success.invoke(result)
        })
    }

    fun getSongUrl(vendor: String, id: String, success: (result: SongBean) -> Unit) {
        mWebView.callHandler("asyn.getSongUrl", arrayOf<Any>(vendor, id), { retValue: JSONObject ->
            val result = gson.fromJson<SongBean>(retValue.toString(), SongBean::class.java)
            success.invoke(result)
        })
    }
}
