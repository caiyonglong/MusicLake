package com.cyl.music_hnust.callback;

import com.cyl.music_hnust.model.music.Singer;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by yonglong on 2016/12/28.
 */

public abstract class SingerCallback extends Callback<Singer> {
    @Override
    public Singer parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        Singer singer = new Gson().fromJson(string, Singer.class);
        return singer;
    }

}