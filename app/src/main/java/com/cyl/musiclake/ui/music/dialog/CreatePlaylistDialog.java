package com.cyl.musiclake.ui.music.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.PlaylistApiServiceImpl;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.event.MyPlaylistEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.net.ApiManager;
import com.cyl.musiclake.net.RequestCallBack;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者：yonglong on 2016/9/14 15:56
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ALL")
public class CreatePlaylistDialog extends DialogFragment {

    private static final String TAG = "CreatePlaylistDialog";
    private static final String TAG_MUSIC = "music";

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
                .title(R.string.create_playlist)
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .inputRangeRes(2, 20, R.color.red)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.input_playlist, R.string.playlist_name, false, (dialog, input) -> LogUtil.e(TAG, input.toString()))
                .onPositive((dialog, which) -> {
                    String title = dialog.getInputEditText().getText().toString();
                    createPlaylist(title);
                    LogUtil.d(TAG, title);
                }).build();
    }

    private void createPlaylist(String name) {
        boolean mIsLogin = UserStatus.getLoginStatus();
        if (mIsLogin) {
            ApiManager.request(
                    PlaylistApiServiceImpl.INSTANCE.createPlaylist(name),
                    new RequestCallBack<Playlist>() {
                        @Override
                        public void success(Playlist result) {
                            ToastUtils.show(getString(R.string.create_playlist_success));
                            EventBus.getDefault().post(new MyPlaylistEvent(Constants.PLAYLIST_ADD, null));
                        }

                        @Override
                        public void error(String msg) {
                            ToastUtils.show(msg);
                        }
                    }
            );
        }
    }
}
