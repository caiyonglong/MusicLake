package com.cyl.musiclake.musicApi;

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

public class MusicApiUtils {
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
        String stringBuilder = "音乐湖分享\n歌名:" +
                music.getTitle() + "\n歌手:" +
                music.getArtist() + "\n地址:" +
                music.getUri() + "\n专辑图片:" +
                music.getCoverBig() + "\n感谢您的使用！";
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder);
        activity.startActivity(Intent.createChooser(textIntent, "分享"));
//        final Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//分享的类型
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, music.getTitle());//分享标题
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, music.getArtist());//要分享的内容摘要
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, music.getUri());//内容地址
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, music.getCoverBig());//分享的图片URL
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "音乐湖");//应用名称
//        MusicApp.getInstance().getTencent().shareToQQ(activity, params, new IUiListener() {
//            @Override
//            public void onComplete(Object o) {
//                ToastUtils.show("分享成功");
//            }
//
//            @Override
//            public void onError(UiError uiError) {
//                ToastUtils.show("分享失败");
//            }
//
//            @Override
//            public void onCancel() {
//                ToastUtils.show("取消分享");
//            }
//        });
    }
}
