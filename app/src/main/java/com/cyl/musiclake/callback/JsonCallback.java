package com.cyl.musiclake.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Json封装
 * Created by wcy on 2015/12/20.
 */
public abstract class JsonCallback<T> extends Callback<T> {
    private Class<T> mClass;
    private Gson mGson;

    public JsonCallback(Class<T> clazz) {
        this.mClass = clazz;
        mGson = new Gson();
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        try {
            String jsonString = response.body().string();
            Log.e("eeeeeee",jsonString);
            return mGson.fromJson(jsonString, mClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(call, e);
    }

    protected abstract void onError(Call call, Exception e);

    @Override
    public void onResponse(T response, int id) {
        onResponse(response);
    }

    protected abstract void onResponse(T response);


}
