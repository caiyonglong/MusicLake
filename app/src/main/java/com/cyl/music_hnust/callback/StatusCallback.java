package com.cyl.music_hnust.callback;

import com.cyl.music_hnust.model.user.StatusInfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 作者：yonglong on 2016/9/28 17:50
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */

public abstract class StatusCallback extends Callback<StatusInfo>
{
    @Override
    public StatusInfo parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        StatusInfo statusInfo = new Gson().fromJson(string, StatusInfo.class);
        return statusInfo;
    }
}
