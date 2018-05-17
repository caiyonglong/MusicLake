package com.cyl.musiclake.ui.music.list.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musicapi.callback.musicApi.MusicApiServiceImpl;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ALL")
public class AddPlaylistDialog extends DialogFragment {

    private static String TAG_CREATE = "create_playlist";
    private static boolean result = false;

    public static AddPlaylistDialog newInstance(Music song) {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", song);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Music music = getArguments().getParcelable("music");
        if (music.getType() == Music.Type.LOCAL) {
            return showInfoDialog("增加到歌单", "暂不支持添加本地歌曲到在线歌单");
        }
        return null;
    }

    public void showSelectDialog(List<Playlist> playlists) {
        final Music music = getArguments().getParcelable("music");
        new MaterialDialog.Builder(getActivity())
                .title("增加到歌单")
                .items(playlists)
                .itemsCallback((dialog, itemView, which, text) -> {
                    collectMusic(playlists.get(which).getId(), music);
                }).build().show();
    }

    public MaterialDialog showInfoDialog(String title, String msg) {
        if (title.isEmpty()) {
            title = "提示";
        }
        if (msg.isEmpty()) {
            msg = "加载中";
        }
        return new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(msg)
                .positiveText(R.string.sure)
                .onPositive(null)
                .build();
    }

    public void collectMusic(String pid, Music music) {
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