package com.cyl.musiclake.musicapi.impl;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.cyl.musiclake.musicapi.AjaxHandler;
import com.cyl.musiclake.musicapi.SearchResult;
import com.cyl.musiclake.musicapi.callback.BaseApiListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

/**
 * Created by master on 2018/5/15.
 * 调用js方法请求数据
 */
public class BaseApiImpl {

    private DWebView mWebView;
    private BaseApiListener musicApiListener;

    public BaseApiImpl(BaseApiListener listener) {
        musicApiListener = listener;
        mWebView = new DWebView((Context) listener);
        DWebView.setWebContentsDebuggingEnabled(true);
        mWebView.addJavascriptObject(new Object() {
            @JavascriptInterface
            public void onAjaxRequest(Object requestData, CompletionHandler handler) {
                AjaxHandler.onAjaxRequest((JSONObject) requestData, handler);
            }
        }, null);
        mWebView.loadUrl("file:///android_asset/app.html");
    }

    /**
     * 搜索
     *
     * @param query
     */
    public void searchSong(String query) {
        mWebView.callHandler("asyn.searchSong", new Object[]{query, 10, 1}, (OnReturnValue<JSONObject>) retValue -> {
            if (musicApiListener != null) {
                Gson gson = new Gson();
                SearchResult searchResult = gson.fromJson(retValue.toString(), SearchResult.class);
                musicApiListener.searchResult(searchResult);
            }
        });
    }

    public void getSongDetail(String query, String id) {
        mWebView.callHandler("asyn.getSongDetail", new Object[]{query, id}, (OnReturnValue<JSONObject>) retValue -> {
            if (musicApiListener != null) {
                musicApiListener.songDetail(retValue);
            }
        });
    }

    public void getTopList(String id) {
        mWebView.callHandler("asyn.getTopList", new Object[]{id}, (OnReturnValue<JSONObject>) retValue -> {
            if (musicApiListener != null) {
                musicApiListener.getOthor(retValue);
            }
        });
    }

    public void getLyricInfo(String vendor, String id) {
        mWebView.callHandler("asyn.getLyric", new Object[]{vendor, id}, (OnReturnValue<JSONObject>) retValue -> {
            if (musicApiListener != null) {
                musicApiListener.getLyric(retValue);
            }
        });
    }

    public void getComment(String vendor, String id) {
        mWebView.callHandler("asyn.getComment", new Object[]{vendor, id, 1, 10}, (OnReturnValue<JSONObject>) retValue -> {
            if (musicApiListener != null) {
                musicApiListener.getComment(retValue);
            }
        });
    }

    public void getSongUrl(String vendor, String id) {
        mWebView.callHandler("asyn.getSongUrl", new Object[]{vendor, id}, (OnReturnValue<JSONObject>) retValue -> {
            if (musicApiListener != null) {
                musicApiListener.songUrl(retValue);
            }
        });
    }
}
