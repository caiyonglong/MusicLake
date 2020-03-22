package com.music.lake.musiclib.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.music.lake.musiclib.MusicPlayerManager;

import java.util.Set;

/**
 * 作者：yonglong on 2016/8/12 16:03
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 内部存儲工具類
 */
public class CommonUtils {
    /**
     * 桌面歌词锁定
     */
    private static final String MUSIC_ID = "music_id";
    private static final String PLAY_POSITION = "play_position";
    private static final String PLAY_MODE = "play_mode";
    private static final String WIFI_MODE = "wifi_mode";
    private static final String LYRIC_MODE = "lyric_mode";
    private static final String NIGHT_MODE = "night_mode";

    public static int getPlayPosition() {
        return getAnyByKey(PLAY_POSITION, -1);
    }

    public static void setPlayPosition(int position) {
        putAnyCommit(PLAY_POSITION, position);
    }

    public static String getCurrentSongId() {
        return getAnyByKey(MUSIC_ID, "");
    }

    public static void saveCurrentSongId(String mid) {
        putAnyCommit(MUSIC_ID, mid);
    }

    public static int getPlayMode() {
        return getAnyByKey(PLAY_MODE, 0);
    }

    public static void savePlayMode(int mode) {
        putAnyCommit(PLAY_MODE, mode);
    }

    /**
     * -------------------------------------------------------
     * <p>底层操作
     * -------------------------------------------------------
     */
    public static boolean getAnyByKey(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static void putAnyCommit(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public static float getAnyByKey(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static void putAnyCommit(String key, float value) {
        getPreferences().edit().putFloat(key, value).apply();
    }

    public static int getAnyByKey(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static void putAnyCommit(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    public static long getAnyByKey(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static void putAnyCommit(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    public static void putAnyCommit(String key, Set<String> defValue) {
        getPreferences().edit().putStringSet(key, defValue).apply();
    }

    public static String getAnyByKey(String key, @Nullable String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static Set<String> getAnyByKey(String key, Set<String> defValue) {
        return getPreferences().getStringSet(key, defValue);
    }

    public static void putAnyCommit(String key, @Nullable String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MusicPlayerManager.getInstance().getAppContext());
    }
}
