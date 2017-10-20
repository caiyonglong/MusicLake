package com.cyl.music_hnust;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.UpdateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    private static MyApplication sInstance;
    private Intent intent;

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        intent = new Intent(this, MusicPlayService.class);
        UpdateUtils.init(this);
        Preferences.init(this);
        initImageLoader(this);
        startService(intent);
        PlayManager.bindToService(this);
        initOkhttp();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        PlayManager.unbindFromService(this);
        stopService(intent);
        Log.d("MyApplication", "onTerminate");
        super.onTerminate();

    }

    private void initOkhttp() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheSize(2 * 1024 * 1024) // 2MB
                .diskCacheSize(50 * 1024 * 1024) // 50MB
                .build();
        ImageLoader.getInstance().init(configuration);
    }

}
