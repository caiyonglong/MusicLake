package com.cyl.musiclake.ui.music.local.fragment;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.local.contract.FolderSongsContract;
import com.cyl.musiclake.ui.music.local.presenter.FolderSongPresenter;
import com.cyl.musiclake.view.ItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FolderSongsFragment extends BaseFragment<FolderSongPresenter> implements FolderSongsContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private SongAdapter mAdapter;
    private String path;
    private List<Music> musicList = new ArrayList<>();

    public static FolderSongsFragment newInstance(String path) {

        Bundle args = new Bundle();
        args.putString(Extras.FOLDER_PATH, path);

        FolderSongsFragment fragment = new FolderSongsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }


    @Override
    protected void loadData() {
        showLoading();
        if (mPresenter != null) {
            mPresenter.loadSongs(path);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void initViews() {
        mAdapter = new SongAdapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ItemDecoration(mFragmentComponent.getActivity(), ItemDecoration.VERTICAL_LIST));
        mAdapter.bindToRecyclerView(mRecyclerView);
        setHasOptionsMenu(true);
    }

    @Override
    protected String getToolBarTitle() {
        if (getArguments() != null) {
            path = getArguments().getString(Extras.FOLDER_PATH);
        }
        return path;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.play(position, musicList, Constants.PLAYLIST_DOWNLOAD_ID + path);
                mAdapter.notifyDataSetChanged();
                NavigationHelper.INSTANCE.navigateToPlaying(mFragmentComponent.getActivity(),null);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            BottomDialogFragment.Companion.newInstance(musicList.get(position)).show((AppCompatActivity) mFragmentComponent.getActivity());
        });
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void showSongs(List<Music> musicList) {
        this.musicList = musicList;
        mAdapter.setNewData(musicList);
        hideLoading();
    }

}
