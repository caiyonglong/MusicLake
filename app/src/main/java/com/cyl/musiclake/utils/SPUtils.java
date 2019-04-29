package com.cyl.musiclake.utils;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;

import java.util.Set;

/**
 * 作者：yonglong on 2016/8/12 16:03
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 内部存儲工具類
 */
public class SPUtils {
    /**
     * 第一次进入应用
     */
    public static final String SP_KEY_FIRST_COMING = "first_coming";
    public static final String SP_KEY_FIRST_INIT_DATABASE = "first_init_database";
    public static final String SP_KEY_NOTICE_CODE = "notice_code";

    /**
     * 音乐接口
     */
    public static final String SP_KEY_PLATER_API_URL = "music_api";
    public static final String SP_KEY_NETEASE_API_URL = "netease_api";
    public static final String SP_KEY_NETEASE_UID = "netease_uid";
    /**
     * 桌面歌词锁定
     */
    public static final String SP_KEY_FLOAT_LYRIC_LOCK = "float_lyric_lock";
    public static final String SP_KEY_THEME_MODE = "theme_mode";
    public static final String SP_KEY_USER_NAME = "user_name";
    public static final String SP_KEY_PASSWORD = "pass_word";

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
    public static final String QQ_OPEN_ID = "qq_open_id";
    public static final String QQ_ACCESS_TOKEN = "qq_access_token";
    public static final String QQ_EXPIRES_IN = "expires_in";

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

    public static long getPosition() {
        return getAnyByKey(POSITION, 0L);
    }

    public static void savePosition(long id) {
        putAnyCommit(POSITION, id);
    }

    public static int getPlayMode() {
        return getAnyByKey(PLAY_MODE, 0);
    }

    public static void savePlayMode(int mode) {
        putAnyCommit(PLAY_MODE, mode);
    }

    public static String getSplashUrl() {
        return getAnyByKey(SPLASH_URL, "");
    }

    public static void saveSplashUrl(String url) {
        putAnyCommit(SPLASH_URL, url);
    }

    public static boolean getWifiMode() {
        return getAnyByKey(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void saveWifiMode(boolean enable) {
        putAnyCommit(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), enable);
    }

    public static boolean isShowLyricView() {
        return getAnyByKey(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void showLyricView(boolean enable) {
        putAnyCommit(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), enable);
    }


    public static boolean isNightMode() {
        return getAnyByKey(NIGHT_MODE, false);
    }

    public static void saveNightMode(boolean on) {
        putAnyCommit(NIGHT_MODE, on);
    }


    public static int getFontSize() {
        return getAnyByKey(DESKTOP_LYRIC_SIZE, 30);
    }

    public static void saveFontSize(int size) {
        putAnyCommit(DESKTOP_LYRIC_SIZE, size);
    }


    public static void saveFontColor(int color) {
        putAnyCommit(DESKTOP_LYRIC_COLOR, color);
    }

    public static int getFontColor() {
        return getAnyByKey(DESKTOP_LYRIC_COLOR, Color.RED);
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
        return PreferenceManager.getDefaultSharedPreferences(MusicApp.getAppContext());
    }
}
