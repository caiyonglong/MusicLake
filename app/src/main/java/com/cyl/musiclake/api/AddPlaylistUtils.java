package com.cyl.musiclake.api;

import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

/**
 * Created by master on 2018/4/8.
 * 封装添加到在线歌单功能
 */

public class AddPlaylistUtils {


    public static void getPlaylist(AppCompatActivity activity, Music music) {
        if (activity == null) return;
        if (music == null) {
            ToastUtils.show((MusicApp.getAppContext().getResources().getString(R.string.resource_error)));
            return;
        }
        if (music.getType() == Music.Type.LOCAL || music.getType() == Music.Type.BAIDU) {
            ToastUtils.show(MusicApp.getAppContext().getResources().getString(R.string.warning_add_playlist));
            return;
        }
        if (!UserStatus.getstatus(activity)) {
            ToastUtils.show(MusicApp.getAppContext().getResources().getString(R.string.prompt_login));
            return;
        }
        ApiManager.request(PlaylistApiServiceImpl.INSTANCE.getPlaylist(), new RequestCallBack<List<Playlist>>() {
            @Override
            public void success(List<Playlist> result) {
                showSelectDialog(activity, result, music);
            }

            @Override
            public void error(String msg) {
                ToastUtils.show(msg);
            }
        });
    }

    private static void showSelectDialog(AppCompatActivity activity, List<Playlist> playlists, Music music) {
        new MaterialDialog.Builder(activity)
                .title("增加到歌单")
                .items(playlists)
                .itemsCallback((dialog, itemView, which, text) -> {
                    collectMusic(playlists.get(which).getId(), music);
                }).build().show();
    }

    private static void collectMusic(String pid, Music music) {
        ApiManager.request(PlaylistApiServiceImpl.INSTANCE.collectMusic(pid, music), new RequestCallBack<String>() {
            @Override
            public void success(String result) {
                ToastUtils.show("收藏成功");
                RxBus.getInstance().post(new PlaylistEvent());
            }

            @Override
            public void error(String msg) {
                ToastUtils.show(msg);
            }
        });
    }

}
