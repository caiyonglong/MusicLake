package com.cyl.musiclake.utils;

import android.util.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;

import io.reactivex.Observable;

public class LyricUtil {

    private static final String lrcRootPath = FileUtils.getLrcDir();

    public static void writeLrcToLoc(String title, String artist, String lrcContext) {
        FileUtils.writeText(getLrcPath(title, artist), lrcContext);
    }

    public static boolean isLrcFileExist(String title, String artist) {
        File file = new File(getLrcPath(title, artist));
        return file.exists();
    }

    public static Observable<String> getLocalLyricFile(String title, String artist) {
        File file = new File(getLrcPath(title, artist));
        if (file.exists()) {
            String lyric = FileUtils.readFile(file);
            return Observable.just(lyric);
        } else {
            return Observable.error(new Throwable("lyric file not exist"));
        }
    }

    private static String getLrcPath(String title, String artist) {
        return lrcRootPath + title + " - " + artist + ".lrc";
    }

    public static String decryptBASE64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 解密
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
