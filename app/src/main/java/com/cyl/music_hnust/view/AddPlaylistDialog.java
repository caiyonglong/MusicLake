package com.cyl.music_hnust.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.model.music.Playlist;
import com.cyl.music_hnust.utils.MusicUtils;

import java.util.List;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class AddPlaylistDialog extends DialogFragment {

//    public static AddPlaylistDialog newInstance(Music song) {
//        long[] songs = new long[1];
//        songs[0] = song.getId();
//        return newInstance(songs);
//    }


    public static AddPlaylistDialog newInstance(Music song) {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("music", song);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<Playlist> playlists = MusicUtils.scanPlaylist(getActivity());
        CharSequence[] chars = new CharSequence[playlists.size() + 1];
        chars[0] = "新建歌单";

        for (int i = 0; i < playlists.size(); i++) {
            chars[i + 1] = playlists.get(i).getName();
        }
        return new MaterialDialog.Builder(getActivity()).title("增加到歌单").items(chars).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                Music music = (Music) getArguments().getSerializable("music");
//                long[] songs = getArguments().getSerializable("songs");
                if (which == 0) {
                    CreatePlaylistDialog.newInstance(music).show(getActivity().getSupportFragmentManager(), "新建歌单");
                    return;
                }

                PlaylistLoader.addToPlaylist(getActivity(),playlists.get(which-1).getId(),music);
                dialog.dismiss();

            }
        }).build();
    }
}