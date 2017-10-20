package com.cyl.music_hnust.callback;

import com.cyl.music_hnust.bean.user.UserInfo;
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

public abstract class UserCallBack extends Callback<UserInfo> {
    @Override
    public UserInfo parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        UserInfo userinfo = new Gson().fromJson(string, UserInfo.class);
        return userinfo;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(call, e);
    }

    protected abstract void onError(Call call, Exception e);

    @Override
    public void onResponse(UserInfo response, int id) {
        onResponse(response);
    }

    protected abstract void onResponse(UserInfo response);

}
