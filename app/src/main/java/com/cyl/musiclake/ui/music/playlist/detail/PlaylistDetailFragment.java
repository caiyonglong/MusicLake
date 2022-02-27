package com.cyl.musiclake.ui.music.playlist.detail;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.UIUtilsKt;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment;
import com.cyl.musiclake.ui.music.edit.PlaylistManagerUtils;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.ui.widget.ItemDecoration;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.LogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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
    private Artist mArtist;
    private Album mAlbum;
    private String title;

    public static PlaylistDetailFragment newInstance(Playlist playlist, boolean useTransition, String transitionName) {
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Extras.PLAYLIST, playlist);
        args.putBoolean(Extras.TRANSITION, useTransition);
        if (useTransition) {
            args.putString(Extras.TRANSITIONNAME, transitionName);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public static PlaylistDetailFragment newInstance(Artist artist) {
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Extras.ARTIST, artist);
        fragment.setArguments(args);
        return fragment;
    }

    public static PlaylistDetailFragment newInstance(Album album) {
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Extras.ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_playlist_detail;
    }

    @Override
    public void initViews() {
        rootView.setFitsSystemWindows(true);
        mPlaylist = getArguments() != null ? getArguments().getParcelable(Extras.PLAYLIST) : null;
        mArtist = getArguments() != null ? getArguments().getParcelable(Extras.ARTIST) : null;
        mAlbum = (Album) getArguments().getSerializable(Extras.ALBUM);

        if (mPlaylist != null) title = mPlaylist.getName();
        if (mArtist != null) title = mArtist.getName();
        if (mAlbum != null) title = mAlbum.getName();
        mToolbar.setTitle(title);
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
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        showLoading();
        if (mPlaylist != null && mPresenter != null)
            mPresenter.loadPlaylistSongs(mPlaylist);
        if (mArtist != null && mPresenter != null)
            mPresenter.loadArtistSongs(mArtist);
        if (mAlbum != null && mPresenter != null)
            mPresenter.loadAlbumSongs(mAlbum);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                if (mPlaylist != null) {
                    PlayManager.play(position, musicList, mPlaylist.getPid());
                } else if (mArtist != null) {
                    PlayManager.play(position, musicList, String.valueOf(mArtist.getArtistId()));
                } else if (mAlbum != null) {
                    PlayManager.play(position, musicList, String.valueOf(mAlbum.getAlbumId()));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = musicList.get(position);
            BottomDialogFragment.Companion.newInstance(music)
                    .show((AppCompatActivity) mFragmentComponent.getActivity());
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                LogUtil.e("action_delete_playlist");
                UIUtilsKt.deletePlaylist(mFragmentComponent.getActivity(), mPlaylist, () -> {
                    if (mPlaylist.getType().equals(Constants.PLAYLIST_HISTORY_ID)) {
                        musicList.clear();
                        PlayHistoryLoader.INSTANCE.clearPlayHistory();
                        mAdapter.notifyDataSetChanged();
                        showEmptyState();
                        EventBus.getDefault().post(new PlaylistEvent(Constants.PLAYLIST_HISTORY_ID, mPlaylist));
                    } else if (mPresenter != null) {
                        PlaylistManagerUtils.INSTANCE.deletePlaylist(mPlaylist, result -> {
                            onBackPress();
                            return null;
                        });
                    }
                    return null;
                }, () -> null);
                break;
            case R.id.action_rename_playlist:
                LogUtil.e("action_rename_playlist");
                if (getActivity() != null) {
                    UIUtilsKt.showPlaylistRenameDialog((AppCompatActivity) getActivity(), mPlaylist.getName(), new Function1<String, Unit>() {
                        @Override
                        public Unit invoke(String title) {
                            if (mPresenter != null)
                                mPresenter.renamePlaylist(mPlaylist, title);
                            return null;
                        }
                    });
                }
                break;
//            case R.id.action_share:
//                Intent intent3 = new Intent(getActivity(), EditActivity.class);
//                StringBuilder content = new StringBuilder();
//                if (musicList.size() > 0) {
//                    content = new StringBuilder("分享歌单\n");
//                }
//                for (int i = 0; i < musicList.size(); i++) {
//                    content.append(musicList.get(i).getTitle()).append("---").append(musicList.get(i).getArtist());
//                    content.append("\n");
//                }
//                intent3.putExtra("content", content.toString());
//                startActivity(intent3);
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
//                break;
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
        if (mPlaylist == null) {
            menu.removeItem(R.id.action_rename_playlist);
            menu.removeItem(R.id.action_delete_playlist);
        } else if (mPlaylist.getPid() != null && mPlaylist.getPid().equals(Constants.PLAYLIST_HISTORY_ID)) {
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
        if (mPlaylist != null && mPlaylist.getCoverUrl() != null) {
            CoverLoader.INSTANCE.loadBigImageView(getContext(), mPlaylist.getCoverUrl(), mPlaylist.getType(), album_art);
        } else if (musicList.size() >= 1) {
            CoverLoader.INSTANCE.loadBigImageView(getContext(), musicList.get(0).getCoverUri(), musicList.get(0).getType(), album_art);
        }
        if (musicList.size() == 0) {
            showEmptyState();
        }
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

    @Override
    public void showTitle(@NotNull String title) {

    }

    @Override
    public void showCover(@NotNull String cover) {

    }

    @Override
    public void showDescInfo(@NotNull String title) {

    }

    @Override
    public void showEmptyView(@NotNull String msg) {

    }

    @Override
    public void showErrorTips(@NotNull String msg, boolean hasTry) {

    }
}
