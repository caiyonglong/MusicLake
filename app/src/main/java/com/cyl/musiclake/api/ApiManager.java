package com.cyl.musiclake.api;

import android.os.Build;
import android.webkit.WebSettings;

import com.cyl.musiclake.MyApplication;
import com.cyl.musiclake.utils.Constants;

import okhttp3.OkHttpClient;
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
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        builder.addInterceptor(chain -> {
            //获取本地user_agent;
//            String userAgentString = getUserAgent();
//            Log.e("user_agent",userAgentString);
//            Request newRequest = chain.request().newBuilder()
//                    .removeHeader("User-Agent")
//                    .addHeader("User-Agent", userAgentString)
//                    .build();
//            return chain.proceed(newRequest);
//        });

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();
        apiService = retrofit.create(ApiManagerService.class);
    }
    private static String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(MyApplication.getInstance().getApplicationContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
