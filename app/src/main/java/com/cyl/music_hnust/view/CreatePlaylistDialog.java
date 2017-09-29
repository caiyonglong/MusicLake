package com.cyl.music_hnust.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.R;

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class CreatePlaylistDialog extends DialogFragment {

    private static final String TAG = "CreatePlaylistDialog";
    private InputListener mCallback;

    public interface InputListener {
        void onInputResult(String title);
    }

    public void setInputListener(InputListener mCallback) {
        this.mCallback = mCallback;
    }


    public static CreatePlaylistDialog newInstance() {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                        mCallback.onInputResult(title);
                        Log.d(TAG, title);
                    }
                }).build();
    }
}
