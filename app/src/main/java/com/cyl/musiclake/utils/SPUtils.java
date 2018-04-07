package com.cyl.musiclake.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.cyl.musiclake.R;

/**
 * 作者：yonglong on 2016/8/12 16:03
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SPUtils {
    private static final String MUSIC_ID = "music_id";
    private static final String PLAY_POSITION = "play_position";
    private static final String PLAY_MODE = "play_mode";
    private static final String SPLASH_URL = "splash_url";
    private static final String WIFI_MODE = "wifi_mode";
    private static final String LYRIC_MODE = "lyric_mode";
    private static final String NIGHT_MODE = "night_mode";
    private static final String POSITION = "position";
    private static final String DESKTOP_LYRIC_SIZE = "desktop_lyric_size";
    private static final String DESKTOP_LYRIC_COLOR = "desktop_lyric_color";

    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static int getPlayPosition() {
        return getInt(PLAY_POSITION, -1);
    }

    public static void setPlayPosition(int position) {
        saveInt(PLAY_POSITION, position);
    }


    public static String getCurrentSongId() {
        return getString(MUSIC_ID, "");
    }

    public static void saveCurrentSongId(String mid) {
        saveString(MUSIC_ID, mid);
    }

    public static long getPosition() {
        return getLong(POSITION, 0);
    }

    public static void savePosition(long id) {
        saveLong(POSITION, id);
    }


    public static int getPlayMode() {
        return getInt(PLAY_MODE, 0);
    }

    public static void savePlayMode(int mode) {
        saveInt(PLAY_MODE, (mode + 1) % 4);
    }

    public static String getSplashUrl() {
        return getString(SPLASH_URL, "");
    }

    public static void saveSplashUrl(String url) {
        saveString(SPLASH_URL, url);
    }

    public static boolean getWifiMode() {
        return getBoolean(sContext.getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void saveWifiMode(boolean enable) {
        saveBoolean(sContext.getString(R.string.setting_key_mobile_wifi), enable);
    }

    public static boolean isShowLyricView() {
        return getBoolean(sContext.getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void showLyricView(boolean enable) {
        saveBoolean(sContext.getString(R.string.setting_key_mobile_wifi), enable);
    }


    public static boolean isNightMode() {
        return getBoolean(NIGHT_MODE, false);
    }

    public static void saveNightMode(boolean on) {
        saveBoolean(NIGHT_MODE, on);
    }


    public static float getFontSize() {
        return getFloat(DESKTOP_LYRIC_SIZE, 30);
    }

    public static void saveFontSize(float size) {
        saveFloat(DESKTOP_LYRIC_SIZE, size);
    }


    public static void saveFontColor(int color) {
        saveInt(DESKTOP_LYRIC_COLOR, color);
    }

    public static int getFontColor() {
        return getInt(DESKTOP_LYRIC_COLOR, Color.RED);
    }


    /**
     * -------------------------------------------------------
     * <p>底层操作
     * -------------------------------------------------------
     */
    private static boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    private static void saveBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    private static float getFloat(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    private static void saveFloat(String key, float value) {
        getPreferences().edit().putFloat(key, value).apply();
    }

    private static int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    private static void saveInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    private static long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    private static void saveLong(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    private static String getString(String key, @Nullable String defValue) {
        return getPreferences().getString(key, defValue);
    }

    private static void saveString(String key, @Nullable String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }
}
