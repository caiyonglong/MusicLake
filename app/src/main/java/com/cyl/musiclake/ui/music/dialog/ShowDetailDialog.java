package com.cyl.musiclake.ui.music.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.cyl.musiclake.R;
import com.music.lake.musiclib.bean.BaseMusicInfo;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ALL")
public class ShowDetailDialog extends DialogFragment {

    private static boolean result = false;

    public static ShowDetailDialog newInstance(BaseMusicInfo song) {
        ShowDetailDialog dialog = new ShowDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", song);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BaseMusicInfo baseMusicInfo = getArguments().getParcelable("music");
        StringBuilder sb = new StringBuilder();
        sb.append("歌名：")
                .append(baseMusicInfo.getTitle())
                .append("\n\n")
                .append("歌手：")
                .append(baseMusicInfo.getArtist())
                .append("\n\n")
                .append("专辑：")
                .append(baseMusicInfo.getAlbum());

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.artist_detail)
                .setMessage(sb.toString())
                .create();
    }


}