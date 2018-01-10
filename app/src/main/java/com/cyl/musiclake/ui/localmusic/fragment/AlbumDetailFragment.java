package com.cyl.musiclake.ui.localmusic.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.localmusic.adapter.AlbumMusicAdapter;
import com.cyl.musiclake.ui.localmusic.contract.AlbumDetailContract;
import com.cyl.musiclake.ui.localmusic.presenter.AlbumDetailPresenter;
import com.cyl.musiclake.utils.Extras;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 专辑
 */
public class AlbumDetailFragment extends BaseFragment implements AlbumDetailContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.album_art)
    ImageView album_art;


    long albumID;
    boolean isAlbum;
    String transitionName;
    String title;

    private AlbumMusicAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();
    private AlbumDetailPresenter mPresenter;

    public static AlbumDetailFragment newInstance(long id, boolean isAlbum, String title, String transitionName) {
        Bundle args = new Bundle();
        args.putLong(Extras.ALBUM_ID, id);
        args.putString(Extras.PLAYLIST_NAME, title);
        args.putBoolean("isAlbum", isAlbum);
        args.putString("transitionName", transitionName);
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initDatas() {

        isAlbum = getArguments().getBoolean("isAlbum");
        albumID = getArguments().getLong(Extras.ALBUM_ID);
        transitionName = getArguments().getString("transitionName");
        title = getArguments().getString(Extras.PLAYLIST_NAME);

        if (transitionName != null)
            album_art.setTransitionName(transitionName);
        if (title != null)
            collapsing_toolbar.setTitle(title);
        if (isAlbum) {
            mPresenter.loadAlbumSongs(albumID);
            mPresenter.loadAlbumArt(albumID);
        } else {
            mPresenter.loadArtistSongs(albumID);
            mPresenter.loadAlbumArt(albumID);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_album;
    }

    @Override
    public void initViews() {
        mPresenter = new AlbumDetailPresenter(getContext());
        mPresenter.attachView(this);

        if (((AppCompatActivity) getActivity()) != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new AlbumMusicAdapter((AppCompatActivity) getActivity(), musicInfos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showAlbumSongs(List<Music> songList) {
        mAdapter.setMusicInfos(songList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showArtistSongs(List<Music> songList) {
        mAdapter.setMusicInfos(songList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAlbumArt(Drawable albumArt) {

    }

    @Override
    public void showAlbumArt(Bitmap bitmap) {
        album_art.setImageBitmap(bitmap);
    }

}
