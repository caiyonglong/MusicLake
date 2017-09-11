package com.cyl.music_hnust.api;

import android.content.Context;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by yonglong on 2017/9/11.
 */

public class ApiManager {

    private static final String BASE_URL = "";
    public ApiManagerService apiService;
    public Retrofit retrofit;


    private static ApiManager sApiManager;

    //获取ApiManager的单例
    public static ApiManager getInstence(Context context) {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager(context);
                }
            }
        }
        return sApiManager;
    }

    private ApiManager(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();
        apiService = retrofit.create(ApiManagerService.class);
    }

}
