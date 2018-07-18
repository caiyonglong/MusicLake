package com.cyl.musiclake.ui.music.playqueue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.player.playqueue.PlayQueueManager;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class PlayQueueDialog extends BottomSheetDialogFragment implements PlayQueueContract.View {

    TextView tvPlayMode;
    ImageView ivPlayMode;
    ImageView clearAll;
    RecyclerView recyclerView;
    private PlayQueuePresenter mPresenter;
    private List<Music> musicList = new ArrayList<>();
    private QueueAdapter mAdapter;


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
        params.height = MusicApp.getInstance().screenSize.y / 7 * 4;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mBehavior.setPeekHeight(params.height);
        //默认全屏展开
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PlayQueuePresenter();
        mPresenter.attachView(this);
        mAdapter = new QueueAdapter(musicList);
    }

    private BottomSheetBehavior mBehavior;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_playqueue, null);
        recyclerView = view.findViewById(R.id.recycler_view_songs);
        tvPlayMode = view.findViewById(R.id.tv_play_mode);
        ivPlayMode = view.findViewById(R.id.iv_play_mode);
        clearAll = view.findViewById(R.id.clear_all);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(PlayManager.getCurrentPosition());
        initListener();
        dialog.setContentView(view);
        mPresenter.loadSongs();
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

    }


    private void initListener() {
        tvPlayMode.setOnClickListener(view -> {
            UIUtils.INSTANCE.updatePlayMode(ivPlayMode, true);
            tvPlayMode.setText(PlayQueueManager.INSTANCE.getPlayMode());
        });
        ivPlayMode.setOnClickListener(view -> {
            UIUtils.INSTANCE.updatePlayMode((ImageView) view, true);
            tvPlayMode.setText(PlayQueueManager.INSTANCE.getPlayMode());
        });
        clearAll.setOnClickListener(v -> {
            mPresenter.clearQueue();
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_love && view.getId() != R.id.iv_more) {
                PlayManager.play(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_more:
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void dismiss() {
        getDialog().dismiss();
    }


    public void updatePlayMode() {
        UIUtils.INSTANCE.updatePlayMode(ivPlayMode, false);
        tvPlayMode.setText(PlayQueueManager.INSTANCE.getPlayMode());
    }

    @OnClick(R.id.clear_all)
    public void onClearAllClick() {
        new MaterialDialog.Builder(getActivity())
                .title("清空播放队列?")
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
    public void onDetach() {
        super.onDetach();
        mPresenter.detachView();
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
