package com.cyl.musiclake.ui.music.local.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.data.source.PlaylistLoader;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.List;

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

        final List<Playlist> playlists = PlaylistLoader.getPlaylist(getActivity());
        final CharSequence[] chars = new CharSequence[playlists.size() + 1];
        chars[0] = "新建歌单";

        for (int i = 0; i < playlists.size(); i++) {
            chars[i + 1] = playlists.get(i).getName();
        }
        return new MaterialDialog.Builder(getActivity())
                .title("增加到歌单")
                .items(chars)
                .itemsCallback((dialog, itemView, which, text) -> {
                    final Music music = getArguments().getParcelable("music");
                    if (which == 0) {
                        CreatePlaylistDialog createDialog = CreatePlaylistDialog.newInstance(music);
                        createDialog.show(getFragmentManager(), TAG_CREATE);
                    } else {
                        Log.d("addDialog", which + "----" + playlists.get(which - 1).getId() + "------" + music.getId());
                        result = PlaylistLoader.addToPlaylist(getActivity(), playlists.get(which - 1).getId(), music.getId());
                        if (result) {
                            RxBus.getInstance().post(new Playlist());
                            ToastUtils.show(getActivity(), "添加成功");
                        } else {
                            ToastUtils.show(getActivity(), "歌单中已有此音乐，请勿重复添加");
                        }
                        dialog.dismiss();
                    }
                }).build();
    }

}