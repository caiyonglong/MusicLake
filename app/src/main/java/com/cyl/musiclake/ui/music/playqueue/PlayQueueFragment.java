package com.cyl.musiclake.ui.music.playqueue;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.dragswipe.DragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.UIUtilsKt;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class PlayQueueFragment extends BaseFragment<PlayQueuePresenter> implements PlayQueueContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private PlayQueueAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.frag_play_queue;
    }

    public static PlayQueueFragment newInstance() {
        Bundle args = new Bundle();
        PlayQueueFragment fragment = new PlayQueueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getString(R.string.playlist_queue));

        setHasOptionsMenu(true);
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new PlayQueueAdapter(musicInfos);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback itemDragAndSwipeCallback = new DragAndSwipeCallback(mAdapter.getDraggableModule());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        // 开启拖拽
        mAdapter.getDraggableModule().setItemTouchHelper(itemTouchHelper);
        mAdapter.getDraggableModule().setOnItemDragListener(onItemDragListener);

        // 开启滑动删除
        mAdapter.getDraggableModule().setSwipeEnabled(true);
        mAdapter.getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);


    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        if (mPresenter != null) {
            mPresenter.loadSongs();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSongs(List<Music> songs) {
        musicInfos = songs;
        mAdapter.setNewData(songs);
        if (songs.size() == 0) {
            mAdapter.setEmptyView(R.layout.view_song_empty);
        }
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.play(position);
                mAdapter.notifyItemChanged(position);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = musicInfos.get(position);
            BottomDialogFragment.Companion.newInstance(music, Constants.PLAYLIST_QUEUE_ID)
                    .show((AppCompatActivity) mFragmentComponent.getActivity());
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                UIUtilsKt.showInfoDialog((AppCompatActivity) mFragmentComponent.getActivity(), getString(R.string.playlist_queue_clear), null, () -> {
                    if (mPresenter != null) {
                        mPresenter.clearQueue();
                        mPresenter.loadSongs();
                    }
                    return null;
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
    }


    OnItemDragListener onItemDragListener = new OnItemDragListener() {
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            BaseViewHolder holder = ((BaseViewHolder) viewHolder);

        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            BaseViewHolder holder = ((BaseViewHolder) source);

        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            BaseViewHolder holder = ((BaseViewHolder) viewHolder);
        }
    };

    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    };


}
