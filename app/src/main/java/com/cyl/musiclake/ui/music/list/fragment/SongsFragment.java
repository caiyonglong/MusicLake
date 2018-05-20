package com.cyl.musiclake.ui.music.list.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.cyl.musiclake.utils.LogUtil;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.data.SongLoader;
import com.cyl.musiclake.ui.music.list.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.list.contract.SongsContract;
import com.cyl.musiclake.ui.music.list.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.music.list.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.list.presenter.SongsPresenter;
import com.cyl.musiclake.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongsFragment extends BaseLazyFragment<SongsPresenter> implements SongsContract.View {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private SongAdapter mAdapter;
    private List<Music> musicList = new ArrayList<>();

    public static SongsFragment newInstance() {
        Bundle args = new Bundle();
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mAdapter = new SongAdapter(musicList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void listener() {
        initPullRefresh();

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                mPresenter.playMusic(musicList, position);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getItem(position);
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        mPresenter.playMusic(musicList, position);
                        break;
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance((Music) adapter.getItem(position))
                                .show(getChildFragmentManager(),getTag());
                        break;
                    case R.id.popup_song_goto_album:
                        LogUtil.e("album", music.toString() + "");
                        NavigationHelper.navigateToAlbum(getActivity(),
                                music.getAlbumId(),
                                music.getAlbum(), null);
                        break;
                    case R.id.popup_song_goto_artist:
                        NavigationHelper.navigateToArtist(getActivity(),
                                music.getArtistId(),
                                music.getArtist(), null);
                        break;
                    case R.id.popup_song_addto_queue:
                        AddPlaylistDialog.newInstance(music).show(getChildFragmentManager(), "ADD_PLAYLIST");
                        break;
                    case R.id.popup_song_delete:
                        new MaterialDialog.Builder(getContext())
                                .title("警告")
                                .content("是否删除这首歌曲？")
                                .onPositive((dialog, which) -> {
                                    FileUtils.delFile(musicList.get(position).getUri());
                                    SongLoader.removeSong(getActivity(), musicList.get(position));
                                    musicList.remove(position);
                                    mAdapter.setNewData(musicList);
                                })
                                .positiveText("确定")
                                .negativeText("取消")
                                .show();
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_song);
            popupMenu.show();
        });
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.loadSongs(Extras.SONG_LOCAL);
        });
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onLazyLoad() {
        mPresenter.loadSongs(Extras.SONG_LOCAL);
    }

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSongs(List<Music> songList) {
        musicList.clear();
        musicList.addAll(songList);
        mAdapter.setNewData(songList);
    }

    @Override
    public void setEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }
}
