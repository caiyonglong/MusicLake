package com.cyl.music_hnust.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.music.Music;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.ui.adapter.PlayQueueAdapter;
import com.cyl.music_hnust.utils.ColorUtil;
import com.cyl.music_hnust.utils.Preferences;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hefuyi on 2016/12/27.
 */

public class PlayQueueDialog extends DialogFragment {

    @Bind(R.id.tv_play_mode)
    TextView tvPlayMode;
    @Bind(R.id.iv_play_mode)
    ImageView ivPlayMode;
    @Bind(R.id.clear_all)
    ImageView clearAll;
    @Bind(R.id.recycler_view_songs)
    RecyclerView recyclerView;
    @Bind(R.id.bottomsheet)
    LinearLayout root;

    private PlayQueueAdapter mAdapter;
    private int play_mode = 0;
    private Palette.Swatch mSwatch;


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PlayQueueAdapter((AppCompatActivity) getActivity(), PlayManager.getPlayList());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_playqueue, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (mSwatch != null) {
            root.setBackgroundColor(mSwatch.getRgb());
            mAdapter.setPaletteSwatch(mSwatch);
            int blackWhiteColor = ColorUtil.getBlackWhiteColor(mSwatch.getRgb());
            tvPlayMode.setTextColor(blackWhiteColor);
            ivPlayMode.setColorFilter(blackWhiteColor);
            clearAll.setColorFilter(blackWhiteColor);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        updatePlayMode();

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if (mAdapter.getItemCount() == 0) {
                    dismiss();
                }
            }
        });

    }

    public void setPaletteSwatch(Palette.Swatch swatch) {
        if (swatch == null) {
            return;
        }
        mSwatch = swatch;
        if (root != null) {
            root.setBackgroundColor(mSwatch.getRgb());
            int blackWhiteColor = ColorUtil.getBlackWhiteColor(mSwatch.getRgb());
            tvPlayMode.setTextColor(blackWhiteColor);
            ivPlayMode.setColorFilter(blackWhiteColor);
            clearAll.setColorFilter(blackWhiteColor);
            mAdapter.setPaletteSwatch(mSwatch);
        }
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void showSongs(List<Music> songs) {
        mAdapter.setSongList(songs);
    }

    public void dismiss() {
        getDialog().dismiss();
    }

    @OnClick(R.id.iv_play_mode)
    public void onPlayModeClick() {
        play_mode = Preferences.getPlayMode();
        play_mode = (play_mode + 1) % 3;
        Preferences.savePlayMode(play_mode);
        updatePlayMode();
    }


    public void updatePlayMode() {
        play_mode = Preferences.getPlayMode();
        switch (play_mode) {
            case 0:
                ivPlayMode.setImageResource(R.drawable.ic_repeat_white_24dp);
                tvPlayMode.setText("列表循环");
                break;
            case 1:
                ivPlayMode.setImageResource(R.drawable.ic_shuffle_white_24dp);
                tvPlayMode.setText("随机播放");
                break;
            case 2:
                ivPlayMode.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                tvPlayMode.setText("单曲循环");
                break;
        }

    }


    @OnClick(R.id.clear_all)
    public void onClearAllClick() {
        new MaterialDialog.Builder(getActivity())
                .title("清除所有?")
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PlayManager.clearQueue();
                        dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
