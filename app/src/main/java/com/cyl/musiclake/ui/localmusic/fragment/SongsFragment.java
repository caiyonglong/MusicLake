package com.cyl.musiclake.ui.localmusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.SongLoader;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.ui.localmusic.adapter.SongAdapter;
import com.cyl.musiclake.ui.localmusic.contract.SongsContract;
import com.cyl.musiclake.ui.localmusic.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.localmusic.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.localmusic.presenter.SongsPresenter;
import com.cyl.musiclake.ui.common.Extras;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongsFragment extends BaseFragment implements SongsContract.View {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;
    private SongAdapter mAdapter;
    private List<Music> musicList = new ArrayList<>();

    private SongsPresenter mPresenter;

    public static SongsFragment newInstance(String flag) {
        Bundle args = new Bundle();
        args.putString(Extras.SONG_CATEGORY, flag);
        SongsFragment fragment = new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatas() {
        mPresenter.loadSongs(getArguments().getString(Extras.SONG_CATEGORY));
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview_songs;
    }

    @Override
    public void initViews() {
        mPresenter = new SongsPresenter(getActivity());
        mPresenter.attachView(this);

        mAdapter = new SongAdapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void listener() {
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
                                    SongLoader.removeSong(getActivity(), musicList.get(position));
                                    mAdapter.notifyItemChanged(position);
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
