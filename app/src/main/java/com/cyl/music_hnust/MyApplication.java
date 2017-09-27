package com.cyl.music_hnust;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.UpdateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class MyApplication extends Application {
    private static MyApplication sInstance;

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        UpdateUtils.init(this);
        Preferences.init(this);
        initImageLoader(this);
        startService(new Intent(this, MusicPlayService.class));
        PlayManager.bindToService(this);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheSize(2 * 1024 * 1024) // 2MB
                .diskCacheSize(50 * 1024 * 1024) // 50MB
                .build();
        ImageLoader.getInstance().init(configuration);
    }

}
