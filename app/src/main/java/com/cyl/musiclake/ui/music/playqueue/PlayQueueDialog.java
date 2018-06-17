package com.cyl.musiclake.ui.music.playqueue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.ColorUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cyl.musiclake.player.MusicPlayerService.PLAY_MODE_LOOP;
import static com.cyl.musiclake.player.MusicPlayerService.PLAY_MODE_RANDOM;
import static com.cyl.musiclake.player.MusicPlayerService.PLAY_MODE_REPEAT;

public class PlayQueueDialog extends DialogFragment implements PlayQueueContract.View {

    @BindView(R.id.tv_play_mode)
    TextView tvPlayMode;
    @BindView(R.id.iv_play_mode)
    ImageView ivPlayMode;
    @BindView(R.id.clear_all)
    ImageView clearAll;
    @BindView(R.id.recycler_view_songs)
    RecyclerView recyclerView;
    @BindView(R.id.sheet)
    LinearLayout root;

    private PlayQueuePresenter mPresenter;
    private List<Music> musicList = new ArrayList<>();
    private PlayQueueAdapter mAdapter;
    private Palette.Swatch mSwatch;
    private String[] mPlayMode = new String[]{"顺序播放", "随机播放", "单曲循环"};
    private int playModeId = 0;

    @OnClick(R.id.iv_play_mode)
    public void onPlayModeClick() {
        PlayManager.refresh();
        updatePlayMode();
        ToastUtils.show(mPlayMode[playModeId]);
    }

    public static PlayQueueDialog newInstance() {
        Bundle args = new Bundle();
        PlayQueueDialog fragment = new PlayQueueDialog();
        fragment.setArguments(args);
        return fragment;
    }

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
        mPresenter = new PlayQueuePresenter();
        mPresenter.attachView(this);
        mAdapter = new PlayQueueAdapter(null);
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
        mAdapter.bindToRecyclerView(recyclerView);

        recyclerView.scrollToPosition(PlayManager.getCurrentPosition());

        initListener();
        mPresenter.loadSongs();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_love && view.getId() != R.id.iv_clear) {
                PlayManager.play(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_clear:
                    PlayManager.removeFromQueue(position);
                    musicList = PlayManager.getPlayList();
                    if (musicList.size() == 0)
                        dismiss();
                    else
                        mAdapter.setNewData(musicList);
                    break;
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void dismiss() {
        getDialog().dismiss();
    }


    public void updatePlayMode() {
        playModeId = SPUtils.getPlayMode();
        switch (playModeId) {
            case PLAY_MODE_LOOP:
                ivPlayMode.setImageResource(R.drawable.ic_repeat);
                tvPlayMode.setText(getResources().getString(R.string.play_mode,
                        mPlayMode[playModeId], musicList.size()));
                break;
            case PLAY_MODE_RANDOM:
                ivPlayMode.setImageResource(R.drawable.ic_shuffle);
                tvPlayMode.setText(getResources().getString(R.string.play_mode,
                        mPlayMode[playModeId], musicList.size()));
                break;
            case PLAY_MODE_REPEAT:
                ivPlayMode.setImageResource(R.drawable.ic_repeat_one);
                tvPlayMode.setText(getResources().getString(R.string.play_mode,
                        mPlayMode[playModeId], musicList.size()));
                break;
        }
    }

    @OnClick(R.id.clear_all)
    public void onClearAllClick() {
        new MaterialDialog.Builder(getActivity())
                .title("清除所有?")
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    mPresenter.clearQueue();
                    mPresenter.loadSongs();
                    dismiss();
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message, boolean showRetryButton) {

    }

    @Override
    public void showEmptyState() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }

    @Override
    public void showSongs(List<Music> songs) {
        musicList = songs;
        updatePlayMode();
        mAdapter.setNewData(songs);
    }

    @Override
    public void showEmptyView() {
        mAdapter.setNewData(null);
        mAdapter.setEmptyView(R.layout.view_queue_empty);
    }
}
