package com.cyl.musiclake.ui.main;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Button;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musicapi.callback.musicApi.AjaxHandler;
import com.cyl.musiclake.utils.LogUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

public class TestActivity extends BaseActivity {

    @BindView(R.id.btn_test)
    Button btnTest;

    DWebView mWebView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btn_test)
    void test() {
        Log.e("tt", "test");
        mWebView.callHandler("asyn.searchSong", new Object[]{"周杰伦"}, (OnReturnValue<JSONObject>) retValue -> {
            btnTest.setText(retValue.toString());
        });
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        mWebView = new DWebView(this);
        mWebView.addJavascriptObject(new Object() {

            /**
             * Note: This method is for Fly.js
             * In browser, Ajax requests are sent by browser, but Fly can
             * redirect requests to native, more about Fly see  https://github.com/wendux/fly
             * @param requestData passed by fly.js, more detail reference https://wendux.github.io/dist/#/doc/flyio-en/native
             * @param handler
             */
            @JavascriptInterface
            public void onAjaxRequest(Object requestData, CompletionHandler handler) {
                LogUtil.e("TAG", "-----");
                // Handle ajax request redirected by Fly
                AjaxHandler.onAjaxRequest((JSONObject) requestData, handler);
            }

        }, null);
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/app.html");
    }

    @Override
    protected void initData() {

    }


}
