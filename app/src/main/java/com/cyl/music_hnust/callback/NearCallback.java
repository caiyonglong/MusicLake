package com.cyl.music_hnust.callback;

import android.util.Log;

import com.cyl.music_hnust.bean.location.LocationInfo;
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

public abstract class NearCallback extends Callback<LocationInfo>
{
    @Override
    public LocationInfo parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        Log.e("eee",string);
        LocationInfo locationInfo = new Gson().fromJson(string, LocationInfo.class);
        return locationInfo;
    }
    @Override
    public void onError(Call call, Exception e, int id) {
        onError(call, e);
    }

    protected abstract void onError(Call call, Exception e);

    @Override
    public void onResponse(LocationInfo response, int id) {
        onResponse(response);
    }

    protected abstract void onResponse(LocationInfo response);

}
