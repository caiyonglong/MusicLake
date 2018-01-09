package com.cyl.musiclake.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.cyl.musiclake.R;
import com.cyl.musiclake.MyApplication;
import com.cyl.musiclake.data.model.Music;

import java.io.File;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：yonglong on 2016/9/9 04:02
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class FileUtils {
    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + "/hkMusic/";
    }

    public static String getMusicDir() {
        String dir = getAppDir() + "Music/";
        return mkdirs(dir);
    }
    public static String getImageDir() {
        String dir = getAppDir() + "cache/";
        return mkdirs(dir);
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "Lyric/";
        return mkdirs(dir);
    }

    public static String getLogDir() {
        String dir = getAppDir() + "Log/";
        return mkdirs(dir);
    }

    public static String getSplashDir(Context context) {
        String dir = context.getFilesDir() + "/splash/";
        return mkdirs(dir);
    }

    public static String getRelativeMusicDir() {
        String dir = "hkMusic/Music/";
        return mkdirs(dir);
    }

    /**
     * 获取歌词路径
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找
     */
    public static String getLrcFilePath(Music music) {
        String lrcFilePath = getLrcDir() + music.getFileName().replace(Constants.FILENAME_MP3, Constants.FILENAME_LRC);
        if (!new File(lrcFilePath).exists()) {
            lrcFilePath = music.getUri().replace(Constants.FILENAME_MP3, Constants.FILENAME_LRC);
        }
        return lrcFilePath;
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    /**
     * mp3文件格式名
     * @param artist
     * @param title
     * @return
     */
    public static String getMp3FileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = MyApplication.getInstance().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            title = MyApplication.getInstance().getString(R.string.unknown);
        }
        return artist + " - " + title + Constants.FILENAME_MP3;
    }

    /**
     * 歌词文件名
     * @param artist
     * @param title
     * @return
     */
    public static String getLrcFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = MyApplication.getInstance().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            title = MyApplication.getInstance().getString(R.string.unknown);
        }
        return artist + " - " + title + Constants.FILENAME_LRC;
    }

    /**
     * 音乐名格式化
     * @param title
     * @return
     */
    public static String getTitle(String title) {
        title = stringFilter(title);
        if (TextUtils.isEmpty(title)) {
            title = MyApplication.getInstance().getString(R.string.unknown);
        }
        return title;
    }
    /**
     * 歌手名格式化
     * @param artist
     * @return
     */
    public static String getArtist(String artist) {
        artist = stringFilter(artist);
        if (TextUtils.isEmpty(artist)) {
            artist = MyApplication.getInstance().getString(R.string.unknown);
        }
        return artist;
    }


    /**
     * 歌手专辑格式化
     * @param artist
     * @param album
     * @return
     */
    public static String getArtistAndAlbum(String artist, String album) {
        artist = stringFilter(artist);
        album = stringFilter(album);
        if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return "";
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return artist;
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            return album;
        } else {
            return artist + " - " + album;
        }
    }


    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx  = "<[^>]+>";
//        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static float b2mb(int b) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String MB = decimalFormat.format((float) b / 1024 / 1024);
        return Float.valueOf(MB);
    }
}
