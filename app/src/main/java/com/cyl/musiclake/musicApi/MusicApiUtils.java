package com.cyl.musiclake.musicApi;

import android.support.v7.app.AppCompatActivity;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.music.online.DownloadDialog;
import com.cyl.musiclake.utils.ToastUtils;

/**
 * Created by master on 2018/4/7.
 */

public class MusicApiUtils {
    public static void checkDownload(AppCompatActivity activity, Music music) {
        if (music != null && music.getType() == Music.Type.LOCAL) {
            ToastUtils.show(MusicApp.getAppContext(), "已经本地音乐!");
            return;
        }
        DownloadDialog.newInstance(music).show(activity.getSupportFragmentManager(), activity.getPackageName());
    }
}
