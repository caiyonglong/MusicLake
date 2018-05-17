package com.cyl.musiclake.musicApi;

import android.webkit.JavascriptInterface;

import wendu.dsbridge.CompletionHandler;

/**
 * Created by master on 2018/5/16.
 */

public class JsApi{
    //同步API
    @JavascriptInterface
    public String testSyn(Object msg)  {
        return msg + "［syn call］";
    }

    //异步API
    @JavascriptInterface
    public void testAsyn(Object msg, CompletionHandler<String> handler) {
        handler.complete(msg+" [ asyn call]");
    }
}