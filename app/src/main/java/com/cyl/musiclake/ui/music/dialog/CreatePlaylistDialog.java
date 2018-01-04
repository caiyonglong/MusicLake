package com.cyl.musiclake.ui.music.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.music.model.data.PlaylistLoader;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.utils.ToastUtils;

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class CreatePlaylistDialog extends DialogFragment {

    private static final String TAG = "CreatePlaylistDialog";
    private static final String TAG_MUSIC = "music";
    private mCallBack callBack;

    public void setCallBack(mCallBack callBack) {
        this.callBack = callBack;
    }

    public interface mCallBack {
        void updatePlaylistView();
    }

    public static CreatePlaylistDialog newInstance() {
        return newInstance(null);
    }

    public static CreatePlaylistDialog newInstance(Music music) {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_MUSIC, music);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Music music = getArguments().getParcelable(TAG_MUSIC);
        return new MaterialDialog.Builder(getActivity())
                .title("新建歌单")
                .positiveText("确定")
                .negativeText("取消")
                .inputRangeRes(2, 10, R.color.red)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("输入歌单名", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.e(TAG, input.toString());
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String title = dialog.getInputEditText().getText().toString();
                        long pid = PlaylistLoader.createPlaylist(getActivity(), title);
                        if (pid != -1) {
                            if (music != null) {
                                PlaylistLoader.addToPlaylist(getActivity(), String.valueOf(pid), music.getId());
                                ToastUtils.show(getActivity(), "添加成功");
                            } else {
                                ToastUtils.show(getActivity(), "新建歌单 " + title);
                                if (callBack != null) {
                                    callBack.updatePlaylistView();
                                }
                            }
                        } else {
                            ToastUtils.show(getActivity(), "创建失败" + title);
                        }
                        Log.d(TAG, title);
                    }
                }).build();
    }
}
