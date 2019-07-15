package com.cyl.musiclake.ui.music.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ALL")
public class ShowDetailDialog extends DialogFragment {

    private static boolean result = false;

    public static ShowDetailDialog newInstance(Music song) {
        ShowDetailDialog dialog = new ShowDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", song);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Music music = getArguments().getParcelable("music");
        StringBuilder sb = new StringBuilder();
        sb.append("歌名：")
                .append(music.getTitle())
                .append("\n\n")
                .append("歌手：")
                .append(music.getArtist())
                .append("\n\n")
                .append("专辑：")
                .append(music.getAlbum());

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.artist_detail)
                .setMessage(sb.toString())
                .create();
    }


}