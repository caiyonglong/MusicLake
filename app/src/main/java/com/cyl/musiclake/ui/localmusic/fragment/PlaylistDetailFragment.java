package com.cyl.musiclake.ui.localmusic.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.model.Playlist;
import com.cyl.musiclake.data.source.PlaylistLoader;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.ui.localmusic.adapter.SongAdapter;
import com.cyl.musiclake.ui.localmusic.contract.PlaylistDetailContract;
import com.cyl.musiclake.ui.localmusic.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.localmusic.presenter.PlaylistDetailPresenter;
import com.cyl.musiclake.ui.zone.EditActivity;
import com.cyl.musiclake.utils.Extras;
import com.cyl.musiclake.utils.ConvertUtils;
import com.cyl.musiclake.utils.FormatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailFragment extends BaseFragment implements PlaylistDetailContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.album_art)
    ImageView album_art;

    private SongAdapter mAdapter;
    private List<Music> musicList = new ArrayList<>();
    private Playlist mPlaylist;
    private PlaylistDetailPresenter mPresenter;

    public static PlaylistDetailFragment newInstance(Playlist playlist) {

        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Extras.PLAYLIST, playlist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.playlist_detail;
    }

    @Override
    public void initViews() {
        mPlaylist = (Playlist) getArguments().getSerializable(Extras.PLAYLIST);

        mToolbar.setTitle(mPlaylist != null ? mPlaylist.getName() : "");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setHasOptionsMenu(true);
    }

    @Override
    protected void initDatas() {
        mPresenter = new PlaylistDetailPresenter(getContext());
        mPresenter.attachView(this);

        mAdapter = new SongAdapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

        mPresenter.loadPlaylistSongs(mPlaylist.getId());
        mPresenter.loadPlaylistArt(mPlaylist.getId());
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.setPlayList(musicList);
                PlayManager.play(position);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        PlayManager.setPlayList(musicList);
                        PlayManager.play(position);
                        break;
                    case R.id.popup_song_detail:
                        getMusicInfo(musicList.get(position));
                        break;
                    case R.id.popup_song_goto_album:
                        Log.e("album", musicList.get(position).toString() + "");
                        NavigateUtil.navigateToAlbum(getActivity(),
                                musicList.get(position).getAlbumId(),
                                musicList.get(position).getAlbum(), null);
                        break;
                    case R.id.popup_song_goto_artist:
                        NavigateUtil.navigateToAlbum(getActivity(),
                                musicList.get(position).getArtistId(),
                                musicList.get(position).getArtist(), null);
                        break;
                    case R.id.popup_song_addto_queue:
                        AddPlaylistDialog.newInstance(musicList.get(position)).show(getChildFragmentManager(), "ADD_PLAYLIST");
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_song);
            popupMenu.show();
        });
    }


    private void getMusicInfo(Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(ConvertUtils.getTitle(music.getTitle()));
        StringBuilder sb = new StringBuilder();
        sb.append("艺术家：")
                .append(music.getArtist())
                .append("\n\n")
                .append("专辑：")
                .append(music.getAlbum())
                .append("\n\n")
                .append("播放时长：")
                .append(FormatUtil.formatTime(music.getDuration()))
                .append("\n\n")
                .append("文件名称：")
                .append(music.getFileName())
                .append("\n\n")
                .append("文件大小：")
                .append(FormatUtil.formatSize(music.getFileSize()))
                .append("\n\n")
                .append("文件路径：")
                .append(new File(music.getUri()).getParent());
        dialog.setMessage(sb.toString());
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("是否删除这个歌单？")
                        .onPositive((dialog, which) -> {
                            PlaylistLoader.deletePlaylist(getActivity(), mPlaylist.getId());
                            RxBus.getInstance().post(new Playlist());
                            onBackPress();
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .show();
                break;
            case R.id.action_share:
                Intent intent3 = new Intent(getActivity(), EditActivity.class);
                StringBuilder content = new StringBuilder();
                if (musicList.size() > 0) {
                    content = new StringBuilder("分享歌单\n");
                }
                for (int i = 0; i < musicList.size(); i++) {
                    content.append(musicList.get(i).getTitle()).append("---").append(musicList.get(i).getArtist());
                    content.append("\n");
                }
                intent3.putExtra("content", content.toString());
                startActivity(intent3);
                break;
        }
        return true;
    }

    private void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_playlist_detail, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showPlaylistSongs(List<Music> songList) {
        musicList.clear();
        musicList.addAll(songList);
        mAdapter.setNewData(songList);
    }

    @Override
    public void showPlaylistArt(Drawable playlistArt) {
        album_art.setImageDrawable(playlistArt);
    }

    @Override
    public void showPlaylistArt(Bitmap bitmap) {

    }

    @Override
    public void showEmptyView() {

    }
}
