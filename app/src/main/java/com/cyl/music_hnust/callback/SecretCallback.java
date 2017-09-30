package com.cyl.music_hnust.callback;

import android.util.Log;

import com.cyl.music_hnust.model.community.SecretInfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：yonglong on 2016/9/28 17:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */

public abstract class SecretCallback extends Callback<SecretInfo>
{
    @Override
    public SecretInfo parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        Log.e("2222222",string);
        SecretInfo secretInfo = new Gson().fromJson(string, SecretInfo.class);
        return secretInfo;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(call, e);
    }

    protected abstract void onError(Call call, Exception e);

    @Override
    public void onResponse(SecretInfo response, int id) {
        onResponse(response);
    }

    protected abstract void onResponse(SecretInfo response);

}
