package com.cyl.music_hnust.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.images.Artwork;


public class AlbumUtil {
	/**
	 * 扫描音乐内嵌专辑图片
	 *
	 * @param path
	 *            音乐SD卡路径
	 * @return 有则返回图片，无则返回null
	 */
	public static Bitmap scanAlbumImage(String path) {
		File file = new File(path);
		Bitmap bitmap = null;

		if (file.exists()) {
			try {
				MP3File mp3File = (MP3File) AudioFileIO.read(file);
				if (mp3File.hasID3v1Tag()) {
					Tag tag = mp3File.getTag();
					Artwork artwork = tag.getFirstArtwork();// 获得专辑图片
					if (artwork != null) {
						byte[] byteArray = artwork.getBinaryData();// 将读取到的专辑图片转成二进制
						bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
								byteArray.length); // 通过BitmapFactory转成Bitmap
					}
				} else if (mp3File.hasID3v2Tag()) {// 如果上面的条件不成立，才执行下面的方法
					AbstractID3v2Tag v2Tag = mp3File.getID3v2Tag();
					Artwork artwork = v2Tag.getFirstArtwork();// 获得专辑图片
					if (artwork != null) {
						byte[] byteArray = artwork.getBinaryData();// 将读取到的专辑图片转成二进制
						bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
								byteArray.length); // 通过BitmapFactory转成Bitmap
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return bitmap;
	}

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
