package com.cyl.musiclake;

import android.app.Application;

import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.utils.PreferencesUtils;
import com.cyl.musiclake.utils.UpdateUtils;

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private PlayManager.ServiceToken mToken;

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        UpdateUtils.init(this);
        PreferencesUtils.init(this);
    }

}
