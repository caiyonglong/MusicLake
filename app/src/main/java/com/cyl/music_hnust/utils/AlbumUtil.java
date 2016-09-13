package com.cyl.music_hnust.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;



public class AlbumUtil {


	public static String imageToLocal(Bitmap bitmap, String name) {
		String path = null;
		try {
			if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				return null;
			}
			String filePath = Environment.getExternalStorageDirectory().getPath() + "/cache/";
			File file = new File(filePath, name);
			if (file.exists()) {
				path = file.getAbsolutePath();
				return path;
			} else {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();

			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}

			path = file.getAbsolutePath();
		} catch (Exception e) {
		}
		return path;
	}

}
