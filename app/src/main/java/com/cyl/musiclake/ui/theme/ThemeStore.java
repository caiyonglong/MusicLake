package com.cyl.musiclake.ui.theme;

import com.cyl.musiclake.utils.SPUtils;

/**
 * 作者：yonglong
 * 包名：com.cyl.musiclake.ui.theme
 * 时间：2019/4/9 13:28
 * 描述：
 */
public class ThemeStore {
    public final static int DAY = 0; //日间模式
    public final static int NIGHT = 1;//夜间模式
    public final static int SYSTEM = 2;//跟随系统

    public static int THEME_MODE = DAY;

    public static void updateThemeMode(int themeMode) {
        SPUtils.putAnyCommit(SPUtils.SP_KEY_THEME_MODE, themeMode);
    }

    public static int getThemeMode() {
        return SPUtils.getAnyByKey(SPUtils.SP_KEY_THEME_MODE, DAY);
    }
}
