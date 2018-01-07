package com.cyl.musiclake.ui.music.fragment;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.ui.music.adapter.PlaylistAdapter;
import com.cyl.musiclake.ui.music.contract.MyMusicContract;
import com.cyl.musiclake.ui.music.dialog.CreatePlaylistDialog;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.model.Playlist;
import com.cyl.musiclake.ui.music.presenter.MyMusicPresenter;
import com.cyl.musiclake.view.ItemView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyMusicFragment extends BaseFragment implements CreatePlaylistDialog.mCallBack, MyMusicContract.View {
    @BindView(R.id.iv_local)
    ItemView mLocal;
    @BindView(R.id.iv_favorite)
    ItemView mLove;
    @BindView(R.id.iv_download)
    ItemView mDownload;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.scroll_view)
    NestedScrollView mNestedScrollView;

    @OnClick(R.id.iv_playlist_add)
    void addPlaylist() {
        CreatePlaylistDialog dialog = CreatePlaylistDialog.newInstance();
        dialog.setCallBack(this);
        dialog.show(getChildFragmentManager(), TAG_CREATE);
    }

    private static final String TAG_CREATE = "create_playlist";
    private List<Playlist> mData;
    private PlaylistAdapter mAdapter;
    private MyMusicPresenter mPresenter;

    public static MyMusicFragment newInstance() {
        Bundle args = new Bundle();
        MyMusicFragment fragment = new MyMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_local;
    }

    @Override
    public void initViews() {
        mPresenter = new MyMusicPresenter(getActivity());
        mPresenter.attachView(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapter = new PlaylistAdapter(getContext(), mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, playlist) -> {
            NavigateUtil.navigateToPlaylist(getActivity(), playlist.getId(), playlist.getName(), null);
        });
    }

    @Override
    protected void initDatas() {
        mPresenter.loadSongs();
        mPresenter.loadPlaylist();
        mLocal.setOnClickListener(v -> NavigateUtil.navigateToLocalMusic(getActivity(), null));
        mLove.setOnClickListener(v -> NavigateUtil.navigateToLocalMusic(getActivity(), null));
        mDownload.setOnClickListener(v -> NavigateUtil.navigateToDownload(getActivity(), null));
    }

    @Override
    public void updatePlaylistView() {
        mPresenter.loadPlaylist();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSongs(List<Music> songList) {
        mLocal.setSongsNum(songList.size());
    }

    @Override
    public void showPlaylist(List<Playlist> playlists) {
        mAdapter.setLocalplaylists(playlists);
        mAdapter.notifyDataSetChanged();
    }
}
