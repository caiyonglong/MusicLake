package com.cyl.musiclake.ui.music.playlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.api.AddPlaylistUtils;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.dialog.PopupUtilsKt;
import com.cyl.musiclake.ui.music.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.ui.zone.EditActivity;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.view.ItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistDetailFragment extends BaseFragment<PlaylistDetailPresenter> implements PlaylistDetailContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.album_art)
    ImageView album_art;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @OnClick(R.id.fab)
    void onPlayAll() {
        PlayManager.play(0, musicList, mPlaylist.getPid());
    }

    private SongAdapter mAdapter;
    private List<Music> musicList = new ArrayList<>();
    private Playlist mPlaylist;

    public static PlaylistDetailFragment newInstance(Playlist playlist, boolean useTransition, String transitionName) {
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Extras.PLAYLIST, playlist);
        args.putBoolean(Extras.TRANSITION, useTransition);
        if (useTransition) {
            args.putString(Extras.TRANSITIONNAME, transitionName);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_playlist_detail;
    }

    @Override
    public void initViews() {
        mPlaylist = (Playlist) getArguments().getSerializable(Extras.PLAYLIST);
        mToolbar.setTitle(mPlaylist != null ? mPlaylist.getName() : "");
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getArguments().getBoolean(Extras.TRANSITION)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                album_art.setTransitionName(getArguments().getString("transition_name"));
            }
        }
        mAdapter = new SongAdapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new ItemDecoration(mFragmentComponent.getActivity(), ItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        showLoading();
        mPresenter.loadPlaylistSongs(mPlaylist);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                PlayManager.play(position, musicList, mPlaylist.getPid());
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        PlayManager.play(position, musicList, mPlaylist.getPid());
                        break;
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance((Music) adapter.getItem(position))
                                .show(getChildFragmentManager(), getTag());
                        break;
                    case R.id.popup_song_addto_queue:
                        AddPlaylistUtils.INSTANCE.getPlaylist((AppCompatActivity) getActivity(), musicList.get(position));
                        break;
                    case R.id.popup_song_delete:
                        new MaterialDialog.Builder(getContext())
                                .title("提示")
                                .content("是否移除这首歌曲？")
                                .onPositive((dialog, which) -> {
                                    if (mPresenter != null) {
                                        mPresenter.disCollectMusic(mPlaylist.getPid(), position, musicList.get(position));
                                    }
                                })
                                .positiveText("确定")
                                .negativeText("取消")
                                .show();
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_playlist);
            popupMenu.show();
        });
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                LogUtil.e("action_delete_playlist");
                PopupUtilsKt.deletePlaylist(mFragmentComponent.getActivity(), mPlaylist, isHistory -> {
                    if (isHistory) {
                        musicList.clear();
                        PlayHistoryLoader.INSTANCE.clearPlayHistory();
                        mAdapter.notifyDataSetChanged();
                        showEmptyState();
                        RxBus.getInstance().post(new PlaylistEvent(Constants.PLAYLIST_HISTORY_ID));
                    } else if (mPresenter != null) {
                        mPresenter.deletePlaylist(mPlaylist);
                    }
                    return null;
                }, () -> null);
                break;
            case R.id.action_rename_playlist:
                LogUtil.e("action_rename_playlist");
                new MaterialDialog.Builder(getActivity())
                        .title("重命名歌单")
                        .positiveText("确定")
                        .negativeText("取消")
                        .inputRangeRes(2, 10, R.color.red)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("输入歌单名", mPlaylist.getName(), false, (dialog, input) -> LogUtil.e("=====", input.toString()))
                        .onPositive((dialog, which) -> {
                            String title = dialog.getInputEditText().getText().toString();
                            mPresenter.renamePlaylist(mPlaylist, title);
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
//                break;
//            case R.id.action_add:
//                List<String> titles = new ArrayList<>();
//                List<Music> addMusicList = SongLoader.getSongsForDB(getContext());
//                for (Music music : addMusicList) {
//                    titles.add(music.getTitle() + "-" + music.getArtist());
//                }
//                new MaterialDialog.Builder(getActivity())
//                        .title("新增歌曲")
//                        .iconRes(R.drawable.ic_playlist_add)
//                        .content("快速添加歌曲，更加方便地添加所需要的歌曲到当前目录")
//                        .positiveText("确定")
//                        .items(titles)
//                        .itemsCallbackMultiChoice(null, (dialog, which, text) -> false)
//                        .onPositive((dialog, which) -> {
//                            dialog.dismiss();
//                            int sum = dialog.getSelectedIndices().length, num = 0;
//                            for (int i = 0; i < sum; i++) {
//                                int index = dialog.getSelectedIndices()[i];
//                                boolean success = PlaylistLoader.addToPlaylist(getContext(), mPlaylist.getId(), addMusicList.get(index).getId());
//                                if (success) {
//                                    num++;
//                                }
//                            }
//                            mPresenter.loadPlaylistSongs(mPlaylist.getId());
//                            mPresenter.loadPlaylistArt(mPlaylist.getId());
//                            ToastUtils.show(getContext(), num + "首添加成功，" + (sum - num) + "首已存在此歌单添加失败");
//                            RxBus.getInstance().post(new Playlist());
//                        }).show();
                break;
        }
        return super.

                onOptionsItemSelected(item);

    }

    private void onBackPress() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist_detail, menu);
        if (mPlaylist.getPid() != null && mPlaylist.getPid().equals(Constants.PLAYLIST_HISTORY_ID)) {
            menu.removeItem(R.id.action_rename_playlist);
        } else if (mPlaylist.getPid() != null && mPlaylist.getPid().equals(Constants.PLAYLIST_LOVE_ID)) {
            menu.removeItem(R.id.action_rename_playlist);
            menu.removeItem(R.id.action_delete_playlist);
        }
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
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void showPlaylistSongs(List<Music> songList) {
        hideLoading();
        musicList.addAll(songList);
        mAdapter.setNewData(musicList);
        if (musicList.size() >= 1) {
            CoverLoader.loadImageView(getContext(), musicList.get(0).getCoverUri(), album_art);
        }
        if (musicList.size() == 0) {
            showEmptyState();
        }
    }

    @Override
    public void changePlayStatus(Boolean isPlaying) {

    }

    @Override
    public void removeMusic(int position) {
        musicList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void success(int type) {
        onBackPress();
    }
}
