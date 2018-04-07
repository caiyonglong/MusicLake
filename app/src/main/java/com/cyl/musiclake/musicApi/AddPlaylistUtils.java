package com.cyl.musiclake.musicApi;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by master on 2018/4/8.
 * 封装添加到在线歌单功能
 */

public class AddPlaylistUtils {

    @SuppressLint("StaticFieldLeak")
    private static AppCompatActivity myActivity;

    public static void getPlaylist(AppCompatActivity activity, Music music) {
        myActivity = activity;
        MusicApiServiceImpl.getPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Playlist>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<Playlist> playlists) {
                        showSelectDialog(playlists, music);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static void showSelectDialog(List<Playlist> playlists, Music music) {
        if (myActivity != null)
            new MaterialDialog.Builder(myActivity)
                    .title("增加到歌单")
                    .items(playlists)
                    .itemsCallback((dialog, itemView, which, text) -> {
                        collectMusic(playlists.get(which).getId(), music);
                    }).build().show();
    }

    private static void collectMusic(String pid, Music music) {
        MusicApiServiceImpl.collectMusic(pid, music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(String status) {
                        LogUtil.e("add", status);
                        if (status.equals("true")) {
                            ToastUtils.show("收藏成功");
                            RxBus.getInstance().post(new PlaylistEvent());
                        } else if (status.equals("false")) {
                            ToastUtils.show("歌曲已存在");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtils.show("网络异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
