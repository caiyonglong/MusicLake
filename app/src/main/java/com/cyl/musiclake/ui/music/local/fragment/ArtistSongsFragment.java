package com.cyl.musiclake.ui.music.local.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.local.contract.ArtistSongContract;
import com.cyl.musiclake.ui.music.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.music.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.local.presenter.ArtistSongsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 专辑
 */
public class ArtistSongsFragment extends BaseFragment<ArtistSongsPresenter> implements ArtistSongContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.album_art)
    ImageView album_art;

    long artistID;
    String transitionName;
    String title;

    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();

    @OnClick(R.id.fab)
    void onPlayAll() {
        PlayManager.play(0, musicInfos, String.valueOf(artistID));
    }

    public static ArtistSongsFragment newInstance(String id, String title, String transitionName) {
        Bundle args = new Bundle();
        args.putString(Extras.ARTIST_ID, id);
        args.putString(Extras.PLAYLIST_NAME, title);
        args.putString(Extras.TRANSITIONNAME, transitionName);

        ArtistSongsFragment fragment = new ArtistSongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {
        mPresenter.loadSongs(title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_playlist_detail;
    }

    @Override
    public void initViews() {
        artistID = getArguments().getLong(Extras.ARTIST_ID);
        transitionName = getArguments().getString(Extras.TRANSITIONNAME);
        title = getArguments().getString(Extras.PLAYLIST_NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            album_art.setTransitionName(transitionName);
            if (transitionName != null)
                album_art.setTransitionName(transitionName);
        }

        if (title != null)
            collapsing_toolbar.setTitle(title);
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAdapter = new SongAdapter(musicInfos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                List<Music> musicList = adapter.getData();
                PlayManager.play(position,musicList, String.valueOf(artistID));
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        List<Music> musicList = adapter.getData();
                        PlayManager.play(position,musicList, String.valueOf(artistID));
                        break;
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance((Music) adapter.getItem(position))
                                .show(getChildFragmentManager(), getTag());
                        break;
                    case R.id.popup_song_addto_queue:
                        AddPlaylistDialog.newInstance(musicInfos.get(position))
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
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }

    @Override
    public void showSongs(List<Music> songList) {
        musicInfos = songList;
        mAdapter.setNewData(songList);
        hideLoading();
    }

    @Override
    public void showAlbumArt(Bitmap bitmap) {
        album_art.setImageBitmap(bitmap);
    }
}
