package com.cyl.musiclake.api;

import com.cyl.musiclake.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yonglong on 2017/9/11.
 */

public class ApiManager {

    private static final String BASE_URL = "";
    public ApiManagerService apiService;
    public Retrofit retrofit;


    private static ApiManager sApiManager;

    //获取ApiManager的单例
    public static ApiManager getInstance() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        return sApiManager;
    }

    private ApiManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();
        apiService = retrofit.create(ApiManagerService.class);
    }

}
