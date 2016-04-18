package com.cyl.music_hnust.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	private Context context; // 运行上下文
	private MemoryCache memoryCache = new MemoryCache();
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private ExecutorService executorService;

	public ImageLoader(Context context) {
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
	}

	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else if (!isLoadOnlyFromCache){
			Log.v("image", "1");
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		Log.v("image", "222"+url);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		try {
			Bitmap bitmap = null;
			File file = new File(getAlbumArt(Integer.parseInt(url)));
			bitmap = decodeFile(file);
			return bitmap;
		} catch (Exception ex) {
			Log.e("image", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);Log.v("image", "4");
		}
	}

	/**
	 * 防止图片错位
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null){
				photoToLoad.imageView.setImageBitmap(bitmap);
				Log.v("image", "5");
			}
		}
	}
	
	public String getAlbumArt(int trackId) {// trackId是音乐的id
		String mUriTrack = "content://media/external/audio/media/#";
		String[] projection = new String[] { "album_id" };
		String selection = "_id = ?";
		String[] selectionArgs = new String[] { Integer.toString(trackId) };
		Cursor cur = context.getContentResolver().query(Uri.parse(mUriTrack), projection, selection, selectionArgs, null);
		int album_id = 0;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_id = cur.getInt(0);
		}
		cur.close();
		cur = null;

		if (album_id < 0) {
			return null;
		}
		String mUriAlbums = "content://media/external/audio/albums";
		projection = new String[] { "album_art" };
		cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);

		String album_art = null;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_art = cur.getString(0);
		}
		cur.close();
		cur = null;
		return album_art;
	}
	
	public void clearCache() {
		memoryCache.clear();
	}

	/**
	 * 获取合适的Bitmap平时获取Bitmap就用这个方法吧.
	 * @param path 路径.
	 * @param data byte[]数组.
	 * @param context 上下文
	 * @param uri uri
	 * @param target 模板宽或者高的大小.
	 * @param width 是否是宽度
	 * @return
	 */
	public static Bitmap getResizedBitmap(String path, byte[] data,
										  Context context,Uri uri, int target, boolean width) {
		BitmapFactory.Options options = null;

		if (target > 0) {

			BitmapFactory.Options info = new BitmapFactory.Options();
			//这里设置true的时候，decode时候Bitmap返回的为空，
			//将图片宽高读取放在Options里.
			info.inJustDecodeBounds = false;

			decode(path, data, context,uri, info);

			int dim = info.outWidth;
			if (!width)
				dim = Math.max(dim, info.outHeight);
			int ssize = sampleSize(dim, target);

			options = new BitmapFactory.Options();
			options.inSampleSize = ssize;

		}

		Bitmap bm = null;
		try {
			bm = decode(path, data, context,uri, options);
		} catch(Exception e){
			e.printStackTrace();
		}
		return bm;

	}

	/**
	 * 解析Bitmap的公用方法.
	 * @param path
	 * @param data
	 * @param context
	 * @param uri
	 * @param options
	 * @return
	 */
	public static Bitmap decode(String path, byte[] data, Context context,
								Uri uri, BitmapFactory.Options options) {

		Bitmap result = null;

		if (path != null) {

			result = BitmapFactory.decodeFile(path, options);

		} else if (data != null) {

			result = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);

		} else if (uri != null) {
			//uri不为空的时候context也不要为空.
			ContentResolver cr = context.getContentResolver();
			InputStream inputStream = null;

			try {
				inputStream = cr.openInputStream(uri);
				result = BitmapFactory.decodeStream(inputStream, null, options);
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;
	}


	/**
	 * 获取合适的sampleSize.
	 * 这里就简单实现都是2的倍数啦.
	 * @param width
	 * @param target
	 * @return
	 */
	private static int sampleSize(int width, int target){
		int result = 1;
		for(int i = 0; i < 10; i++){
			if(width < target * 2){
				break;
			}
			width = width / 2;
			result = result * 2;
		}
		return result;
	}
}