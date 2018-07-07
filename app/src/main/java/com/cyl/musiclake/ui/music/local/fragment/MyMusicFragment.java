package com.cyl.musiclake.ui.music.local.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.data.PlaylistLoader;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.dialog.CreatePlaylistDialog;
import com.cyl.musiclake.ui.music.local.contract.MyMusicContract;
import com.cyl.musiclake.ui.music.local.presenter.MyMusicPresenter;
import com.cyl.musiclake.ui.music.playlist.PlaylistAdapter;
import com.cyl.musiclake.ui.music.playlist.PlaylistManagerActivity;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.view.LocalItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Monkey on 2015/6/29.
 */
public class MyMusicFragment extends BaseFragment<MyMusicPresenter> implements MyMusicContract.View {
    @BindView(R.id.playlistRcv)
    RecyclerView mPlaylistRcv;

    @BindView(R.id.localView)
    LocalItemView localView;

    @BindView(R.id.historyView)
    LocalItemView historyView;

    @BindView(R.id.favoriteView)
    LocalItemView favoriteView;

    @BindView(R.id.downloadView)
    LocalItemView downloadView;

    @OnClick(R.id.playlistAddIv)
    void addPlaylist() {
        if (UserStatus.getstatus(mFragmentComponent.getActivity())) {
            CreatePlaylistDialog dialog = CreatePlaylistDialog.newInstance();
            dialog.show(getChildFragmentManager(), TAG_CREATE);
        } else {
            ToastUtils.show("请登录");
        }
    }

    @OnClick(R.id.playlistManagerIv)
    void playlistManager() {
        Intent intent = new Intent(getActivity(), PlaylistManagerActivity.class);
        startActivity(intent);
    }

    private static final String TAG_CREATE = "create_playlist";
    private List<Playlist> playlists =new ArrayList<>();
    private PlaylistAdapter mAdapter;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(false);

        mPlaylistRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlaylistRcv.setNestedScrollingEnabled(false);

        mAdapter = new PlaylistAdapter(playlists);
        mPlaylistRcv.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mPlaylistRcv);

    }


    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//                    Pair<View, String> transitionViews = new Pair<View, String>(view.findViewById(R.id.iv_album), "transition_album_art" + position);
                    NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), playlists.get(position), null);
                }
        );

    }

    @Override
    protected void loadData() {
        mPresenter.loadSongs();
        showLoading();
        mPresenter.loadPlaylist();
    }


    @Override
    public void showSongs(List<Music> songList) {
        localView.setSongsNum(songList.size(), 0);
        localView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, songList, Constants.PLAYLIST_LOCAL_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showEmptyView() {
        showEmptyState();
    }

    @Override
    public void showPlaylist(List<Playlist> playlists) {
        this.playlists = playlists;
        hideLoading();
        mAdapter.setNewData(playlists);
    }

    @Override
    public void showHistory(List<Music> musicList) {
        historyView.setSongsNum(musicList.size(), 1);
        historyView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_HISTORY_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showLoveList(List<Music> musicList) {
        favoriteView.setSongsNum(musicList.size(), 2);
        favoriteView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_LOVE_ID);
            } else {
                toFragment(position);
            }
        });
    }

    @Override
    public void showDownloadList(List<Music> musicList) {
        downloadView.setSongsNum(musicList.size(), 3);
        downloadView.setOnItemClickListener((view, position) -> {
            if (view.getId() == R.id.iv_play) {
                PlayManager.play(0, musicList, Constants.PLAYLIST_DOWNLOAD_ID);
            } else {
                toFragment(position);
            }
        });
    }

    private void toFragment(int position) {
        if (position == 0) {
            NavigationHelper.INSTANCE.navigateToLocalMusic(mFragmentComponent.getActivity(), null);
        } else if (position == 1) {
            NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), PlaylistLoader.INSTANCE.getHistoryPlaylist(), null);
        } else if (position == 2) {
            NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), PlaylistLoader.INSTANCE.getFavoritePlaylist(), null);
        } else if (position == 3) {
            NavigationHelper.INSTANCE.navigateToDownload(mFragmentComponent.getActivity(), null);
        }
    }

    @Override
    public void showError(String message, boolean showRetryButton) {
        super.showError(message, showRetryButton);
    }

}
