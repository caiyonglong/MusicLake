package com.cyl.music_hnust.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.Preferences;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;



public class MyApplication extends Application {
	private static MyApplication sInstance;
	private static Resources sRes;


	public static MyApplication getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance =this;
		sRes = getResources();
		Preferences.init(this);
		initImageLoader(this);


	}


	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheSize(2 * 1024 * 1024) // 2MB
				.diskCacheSize(50 * 1024 * 1024) // 50MB
				.build();
		ImageLoader.getInstance().init(configuration);
	}

	public static void updateNightMode(Context context,boolean on) {
		if (on){
			context.setTheme(R.style.MyThemeDark);
		}else {
			context.setTheme(R.style.MyThemeBlue);
		}

	}


}
