package com.cyl.music_hnust.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.ui.fragment.PlaylistFragment;

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class CreatePlaylistDialog extends DialogFragment {
    public static CreatePlaylistDialog newInstance() {
        return newInstance((Music) null);
    }

//    public static CreatePlaylistDialog newInstance(Music music) {
//        long[] songs;
//        if (music == null) {
//            songs = new long[0];
//        } else {
//            songs = new long[1];
//            songs[0] = music.getId();
//        }
//        return newInstance(songs);
//    }

    public static CreatePlaylistDialog newInstance(Music music) {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", music);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(
                getActivity())
                .positiveText("新建")
                .negativeText("取消")
                .input("输入歌单名", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Music music = getArguments().getParcelable("music");
                        long playistId = PlaylistLoader.createPlaylist(getActivity(), input.toString());
//
                        if (playistId != -1) {
                            if (music != null)
                                PlaylistLoader.addToPlaylist(getActivity(), String.valueOf(playistId), music);
                            else
                                Toast.makeText(getActivity(), "创建歌单成功", Toast.LENGTH_SHORT).show();
                            if (getParentFragment() instanceof PlaylistFragment) {
//                        get.updatePlaylists();
                            }
                        } else {
                            Toast.makeText(getActivity(), "创建歌单失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).build();
    }
}
