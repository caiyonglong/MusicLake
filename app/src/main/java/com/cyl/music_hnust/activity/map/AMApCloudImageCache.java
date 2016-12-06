package com.cyl.music_hnust.activity.map;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class AMApCloudImageCache implements ImageCache{
//	int mMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//	int mCacheSize = mMaxMemory / 10;
	final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
			5*1024);

	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		if (url == null || url.equals("")) {
			return null;
		}
		return mImageCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		if (url == null || bitmap == null) {
			return;
		}
		if (getBitmap(url) == null) {
			mImageCache.put(url, bitmap);
		}
	}

}
