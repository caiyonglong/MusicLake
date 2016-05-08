package com.cyl.music_hnust.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.service.MusicPlayService;


public class MyApplication extends Application {
	private static RequestQueue mRequestQueue;

	private static ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
			}
			@Override
			public Bitmap getBitmap(String url) {
				return null;
			}
		});
	}
	public  RequestQueue getHttpQueues() {
		return mRequestQueue;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}
}
