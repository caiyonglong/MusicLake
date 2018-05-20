package com.cyl.musiclake.musicapi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.music.online.DownloadDialog;
import com.cyl.musiclake.utils.ToastUtils;

/**
 * Created by master on 2018/4/7.
 */

public class MusicUtils {
    public static void checkDownload(AppCompatActivity activity, Music music) {
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!");
            return;
        }
        if (music.getType() == Music.Type.LOCAL) {
            ToastUtils.show(MusicApp.getAppContext(), "已经本地音乐!");
            return;
        }
        DownloadDialog.newInstance(music).show(activity.getSupportFragmentManager(), activity.getPackageName());
    }

    /**
     * 分享到QQ
     */
    public static void qqShare(Activity activity, Music music) {
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "暂无音乐播放!");
            return;
        }
        if (music.getType() == Music.Type.LOCAL) {
            ToastUtils.show(MusicApp.getAppContext(), "本地音乐不能分享!");
            return;
        }
        String stringBuilder = "我正在使用 音乐湖 听音乐，我觉得很不错，推荐给你用下，链接：https://github.com/caiyonglong/MusicLake/releases";

        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder);
        activity.startActivity(Intent.createChooser(textIntent, "分享"));
    }
}
