package com.cyl.musiclake.ui.localmusic.fragment;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.Extras;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.ui.localmusic.adapter.SongAdapter;
import com.cyl.musiclake.ui.localmusic.contract.FolderSongsContract;
import com.cyl.musiclake.ui.localmusic.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.localmusic.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.localmusic.presenter.FolderSongPresenter;

import java.util.List;

import butterknife.BindView;

public class FolderSongsFragment extends BaseFragment implements FolderSongsContract.View {

    FolderSongPresenter mPresenter;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;

    private SongAdapter mAdapter;
    private String path;

    public static FolderSongsFragment newInstance(String path) {

        Bundle args = new Bundle();
        args.putString(Extras.FOLDER_PATH, path);

        FolderSongsFragment fragment = new FolderSongsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }


    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }


    @Override
    protected void initDatas() {

        mPresenter.loadSongs(path);

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_list_layout;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle("文件夹");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPresenter = new FolderSongPresenter(getActivity());
        mPresenter.attachView(this);
        if (getArguments() != null) {
            path = getArguments().getString(Extras.FOLDER_PATH);
        }
        mAdapter = new SongAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        setHasOptionsMenu(true);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.setPlayList(adapter.getData());
                PlayManager.play(position);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getItem(position);
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        PlayManager.setPlayList(adapter.getData());
                        PlayManager.play(position);
                        break;
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance((Music) adapter.getItem(position))
                                .show(getChildFragmentManager(), getTag());
                        break;
                    case R.id.popup_song_goto_album:
                        Log.e("album", music.toString() + "");
                        NavigateUtil.navigateToAlbum(getActivity(),
                                music.getAlbumId(),
                                music.getAlbum(), null);
                        break;
                    case R.id.popup_song_goto_artist:
                        NavigateUtil.navigateToArtist(getActivity(),
                                music.getArtistId(),
                                music.getArtist(), null);
                        break;
                    case R.id.popup_song_addto_queue:
                        AddPlaylistDialog.newInstance(music).show(getChildFragmentManager(), "ADD_PLAYLIST");
                        break;
                    case R.id.popup_song_delete:
                        new MaterialDialog.Builder(getContext())
                                .title("提示")
                                .content("是否移除这首歌曲？")
                                .onPositive((dialog, which) -> {
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

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        tv_empty.setText("请稍后，努力加载中...");
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showSongs(List<Music> musicList) {
        mAdapter.setNewData(musicList);
    }

}
