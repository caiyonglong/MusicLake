package com.cyl.music_hnust.callback;

import com.cyl.music_hnust.bean.music.Singer;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by yonglong on 2016/12/28.
 */

public abstract class SingerCallback extends Callback<Singer> {
    @Override
    public Singer parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Singer singer = new Gson().fromJson(string, Singer.class);
        return singer;
    }


    @Override
    public void onError(Call call, Exception e, int id) {
        onError(call, e);
    }

    protected abstract void onError(Call call, Exception e);

    @Override
    public void onResponse(Singer response, int id) {
        onResponse(response);
    }

    protected abstract void onResponse(Singer response);

}