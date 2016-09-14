package com.cyl.music_hnust.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.model.LocalPlaylist;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.MusicUtils;

import java.util.List;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class AddPlaylistDialog extends DialogFragment {

//    public static AddPlaylistDialog newInstance(Music music) {
//        List<Music> musicList = new ArrayList<>();
//        musicList.add(music);
//        return newInstance(musicList);
//    }

    public static AddPlaylistDialog newInstance(Music music) {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("music", music);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<LocalPlaylist> playlists = MusicUtils.scanPlaylist(getActivity());
        CharSequence[] chars = new CharSequence[playlists.size() + 1];
        chars[0] = "新建歌单";

        for (int i = 0; i < playlists.size(); i++) {
            chars[i + 1] = playlists.get(i).getName();
        }
        return new MaterialDialog.Builder(getActivity()).title("增加到歌单").items(chars).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                Music music = (Music) getArguments().getSerializable("music");
                if (which == 0) {
//                    CreatePlaylistDialog.newInstance(songs).show(getActivity().getSupportFragmentManager(), "CREATE_PLAYLIST");
                    return;
                }

                MusicUtils.addToPlaylist(getActivity(),playlists.get(which-1).getId(),music);
                dialog.dismiss();

            }
        }).build();
    }
}