package com.cyl.musiclake.ui.music.playlist.history;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.UIUtilsKt;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.music.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class RecentlyFragment extends BaseFragment<RecentlyPresenter> implements RecentlyContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    public static RecentlyFragment newInstance() {
        Bundle args = new Bundle();
        RecentlyFragment fragment = new RecentlyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getString(R.string.item_history));

        setHasOptionsMenu(true);
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new SongAdapter(musicInfos);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        mPresenter.loadSongs();
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
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.play(position, musicInfos, Constants.PLAYLIST_HISTORY_ID);
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        PlayManager.play(position, musicInfos, Constants.PLAYLIST_HISTORY_ID);
                        break;
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance((Music) adapter.getItem(position))
                                .show(getChildFragmentManager(), getTag());
                        break;
                    case R.id.popup_song_addto_queue:
                        AddPlaylistDialog.Companion.newInstance(musicInfos.get(position))
                                .show(getChildFragmentManager(), "ADD_PLAYLIST");
                        break;

                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_album);
            popupMenu.show();
        });
    }


    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                if (getActivity() != null) {
                    UIUtilsKt.showInfoDialog((AppCompatActivity) getActivity(), getString(R.string.tips), getString(R.string.clear_history_playlist_tips), () -> {
                        PlayHistoryLoader.INSTANCE.clearPlayHistory();
                        musicInfos.clear();
                        mAdapter.notifyDataSetChanged();
                        showEmptyView();
                        return null;
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
    }
}
