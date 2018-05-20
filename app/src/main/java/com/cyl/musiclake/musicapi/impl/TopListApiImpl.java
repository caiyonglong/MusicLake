package com.cyl.musiclake.musicapi.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.cyl.musiclake.musicapi.AjaxHandler;
import com.cyl.musiclake.musicapi.callback.TopListApiListener;
import com.cyl.musiclake.utils.LogUtil;

import org.json.JSONObject;

import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

/**
 * Created by master on 2018/5/15.
 * 调用js方法请求数据
 */
public class TopListApiImpl {

    private DWebView mWebView;
    private TopListApiListener listener;
    private Context context;

    @SuppressLint("SetJavaScriptEnabled")
    public TopListApiImpl(Activity context, TopListApiListener listener) {
        this.listener = listener;
        this.context = context;
    }

    public void getTopList(String id) {
        if (mWebView == null) {
            mWebView = new DWebView(context);
            DWebView.setWebContentsDebuggingEnabled(true);
            mWebView.loadUrl("file:///android_asset/app.html");
            mWebView.addJavascriptObject(new Object() {
                @JavascriptInterface
                public void onAjaxRequest(Object requestData, CompletionHandler handler) {
                    AjaxHandler.onAjaxRequest((JSONObject) requestData, handler);
                }
            }, null);
        }
        mWebView.callHandler("asyn.getTopList", new Object[]{id}, (OnReturnValue<JSONObject>) retValue -> {
            LogUtil.d(retValue.toString());
        });
    }
}
