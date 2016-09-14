package com.cyl.music_hnust.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.model.Music;

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class CreatePlaylistDialog extends DialogFragment {
    public static CreatePlaylistDialog newInstance() {
        return newInstance((Music) null);
    }

    public static CreatePlaylistDialog newInstance(Music music) {
        long[] songs;
        if (music == null) {
            songs = new long[0];
        } else {
            songs = new long[1];
            songs[0] = music.getId();
        }
        return newInstance(songs);
    }

    public static CreatePlaylistDialog newInstance(long[] songList) {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putLongArray("songs", songList);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity()).positiveText("Create").negativeText("Cancel").input("Enter playlist name", "", false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                long[] songs = getArguments().getLongArray("songs");
//                long playistId = MusicPlayer.createPlaylist(getActivity(), input.toString());
//
//                if (playistId != -1) {
//                    if (songs != null && songs.length != 0)
//                        MusicPlayer.addToPlaylist(getActivity(), songs, playistId);
//                    else
//                        Toast.makeText(getActivity(), "Created playlist", Toast.LENGTH_SHORT).show();
//                    if (getParentFragment() instanceof PlaylistFragment) {
//                        ((PlaylistFragment) getParentFragment()).updatePlaylists(playistId);
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Unable to create playlist", Toast.LENGTH_SHORT).show();
//                }

            }
        }).build();
    }
}
