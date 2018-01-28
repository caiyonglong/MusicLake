package com.cyl.musiclake.ui.localmusic.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.ui.localmusic.adapter.PlaylistAdapter;
import com.cyl.musiclake.ui.localmusic.contract.MyMusicContract;
import com.cyl.musiclake.ui.localmusic.dialog.CreatePlaylistDialog;
import com.cyl.musiclake.ui.localmusic.presenter.MyMusicPresenter;
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
    @BindView(R.id.iv_recently)
    ItemView mRecently;
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

        mAdapter = new PlaylistAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Pair<View, String> tranitionViews = new Pair<View, String>(view.findViewById(R.id.iv_album), "transition_album_art" + position);
                    NavigateUtil.navigateToPlaylist(getActivity(), (Playlist) adapter.getItem(position), tranitionViews);
                }
        );
    }

    @Override
    protected void initDatas() {
        mPresenter.loadSongs();
        mPresenter.loadPlaylist();
        mLocal.setOnClickListener(v -> NavigateUtil.navigateToLocalMusic(getActivity(), null));
        mRecently.setOnClickListener(v -> {
            NavigateUtil.navigateRecentlyMusic(getActivity());
        });
        mLove.setOnClickListener(v -> {
            NavigateUtil.navigateToLoveMusic(getActivity(), null);
        });
        mDownload.setOnClickListener(v -> NavigateUtil.navigateToDownload(getActivity(), null));

        RxBus.getInstance().register(Playlist.class)
                .subscribe(playlist -> {
                    if (mPresenter != null) {
                        mPresenter.loadSongs();
                        mPresenter.loadPlaylist();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.subscribe();
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
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_playlist_empty);
    }

    @Override
    public void showPlaylist(List<Playlist> playlists) {
        mAdapter.setNewData(playlists);
    }

    @Override
    public void showHistory(List<Music> musicList) {
        mRecently.setSongsNum(musicList.size());
    }

    @Override
    public void showLoveList(List<Music> musicList) {
        mLove.setSongsNum(musicList.size());
    }

    @Override
    public void showDownloadList(List<Music> musicList) {
        mDownload.setSongsNum(musicList.size());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unsubscribe();
    }
}
