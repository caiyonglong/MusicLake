package com.cyl.musiclake.ui.localmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.data.source.PlayHistoryLoader;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.localmusic.adapter.SongAdapter;
import com.cyl.musiclake.ui.localmusic.contract.RecentlyContract;
import com.cyl.musiclake.ui.localmusic.dialog.AddPlaylistDialog;
import com.cyl.musiclake.ui.localmusic.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.localmusic.presenter.RecentlyPresenter;
import com.cyl.musiclake.ui.zone.EditActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class RecentlyFragment extends BaseFragment implements RecentlyContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private SongAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();
    private RecentlyPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    public static RecentlyFragment newInstance() {
        Bundle args = new Bundle();
        RecentlyFragment fragment = new RecentlyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initViews() {
        mToolbar.setTitle("播放历史");

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        mAdapter = new SongAdapter(musicInfos);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initDatas() {
        mPresenter = new RecentlyPresenter(getContext());
        mPresenter.attachView(this);
        mPresenter.loadSongs();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSongs(List<Music> songs) {
        musicInfos = songs;
        mAdapter.setNewData(songs);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                List<Music> musicList = adapter.getData();
                PlayManager.setPlayList(musicList);
                PlayManager.play(position);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        PlayManager.setPlayList((List<Music>) adapter.getData());
                        PlayManager.play(position);
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
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("是否清空播放历史？")
                        .onPositive((dialog, which) -> {
                            PlayHistoryLoader.clearPlayHistory(getActivity());
                            mAdapter.notifyDataSetChanged();
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .show();
                break;
            case R.id.action_share:
                Intent intent3 = new Intent(getActivity(), EditActivity.class);
                StringBuilder content = new StringBuilder();

                if (musicInfos.size() > 0) {
                    content = new StringBuilder("分享歌单\n");
                }
                for (int i = 0; i < musicInfos.size(); i++) {
                    content.append(musicInfos.get(i).getTitle()).append("---").append(musicInfos.get(i).getArtist());
                    content.append("\n");
                }
                intent3.putExtra("content", content.toString());
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist_detail, menu);
    }
}
