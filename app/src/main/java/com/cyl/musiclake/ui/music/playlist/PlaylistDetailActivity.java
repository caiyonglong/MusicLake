package com.cyl.musiclake.ui.music.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.data.db.Playlist;
import com.cyl.musiclake.db.Album;
import com.cyl.musiclake.db.Artist;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.OnlinePlaylistUtils;
import com.cyl.musiclake.ui.UIUtilsKt;
import com.cyl.musiclake.ui.music.dialog.PopupDialogFragment;
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
public class PlaylistDetailActivity extends BaseActivity<PlaylistDetailPresenter> implements PlaylistDetailContract.View {

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
    private String coverUrl;

    public static void newInstance(Context context, Playlist playlist) {
        Intent intent = new Intent(context, PlaylistDetailActivity.class);
        intent.putExtra(Extras.PLAYLIST, playlist);
        context.startActivity(intent);
    }

    public static void newInstance(Context context, Artist artist) {
        Intent intent = new Intent(context, PlaylistDetailActivity.class);
        intent.putExtra(Extras.ARTIST, artist);
        context.startActivity(intent);
    }

    public static void newInstance(Context context, Album album) {
        Intent intent = new Intent(context, PlaylistDetailActivity.class);
        intent.putExtra(Extras.ALBUM, album);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.frag_playlist_detail;
    }


    @Override
    protected void initData() {
        showLoading();
        if (mPlaylist != null && mPresenter != null)
            mPresenter.loadPlaylistSongs(mPlaylist);
        if (mArtist != null && mPresenter != null)
            mPresenter.loadArtistSongs(mArtist);
        if (mAlbum != null && mPresenter != null)
            mPresenter.loadAlbumSongs(mAlbum);
    }

    @Override
    protected String setToolbarTitle() {
        mPlaylist = (Playlist) getIntent().getParcelableExtra(Extras.PLAYLIST);
        mArtist = (Artist) getIntent().getSerializableExtra(Extras.ARTIST);
        mAlbum = (Album) getIntent().getSerializableExtra(Extras.ALBUM);

        if (mPlaylist != null) title = mPlaylist.getName();
        if (mArtist != null) title = mArtist.getName();
        if (mAlbum != null) title = mAlbum.getName();
        return title;
    }

    @Override
    protected void initView() {
        mAdapter = new SongAdapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDecoration(this, ItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getId() != R.id.iv_more) {
                if (mPlaylist != null) {
                    PlayManager.play(position, musicList, mPlaylist.getPid());
                } else if (mArtist != null) {
                    PlayManager.play(position, musicList, String.valueOf(mArtist.getId()));
                } else if (mAlbum != null) {
                    PlayManager.play(position, musicList, String.valueOf(mAlbum.getId()));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = musicList.get(position);
            PopupDialogFragment.Companion.newInstance(music)
                    .show(this);
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_playlist:
                LogUtil.e("action_delete_playlist");
                UIUtilsKt.deletePlaylist(PlaylistDetailActivity.this, mPlaylist, isHistory -> {
                    if (isHistory) {
                        musicList.clear();
                        PlayHistoryLoader.INSTANCE.clearPlayHistory();
                        mAdapter.notifyDataSetChanged();
                        showEmptyState();
                        RxBus.getInstance().post(new PlaylistEvent(Constants.PLAYLIST_HISTORY_ID));
                    } else if (mPresenter != null) {
                        OnlinePlaylistUtils.INSTANCE.deletePlaylist(mPlaylist, result -> {
                            onBackPress();
                            return null;
                        });
                    }
                    return null;
                }, () -> null);
                break;
            case R.id.action_rename_playlist:
                LogUtil.e("action_rename_playlist");
                new MaterialDialog.Builder(this)
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
                Intent intent3 = new Intent(this, EditActivity.class);
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
        return super.

                onOptionsItemSelected(item);

    }

    private void onBackPress() {
        this.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playlist_detail, menu);
        if (mPlaylist == null) {
            menu.removeItem(R.id.action_rename_playlist);
            menu.removeItem(R.id.action_delete_playlist);
        } else if (mPlaylist.getPid() != null && mPlaylist.getPid().equals(Constants.PLAYLIST_HISTORY_ID)) {
            menu.removeItem(R.id.action_rename_playlist);
        } else if (mPlaylist.getPid() != null && mPlaylist.getPid().equals(Constants.PLAYLIST_LOVE_ID)) {
            menu.removeItem(R.id.action_rename_playlist);
            menu.removeItem(R.id.action_delete_playlist);
        } else if (mPlaylist.getType() == 2) {
            menu.removeItem(R.id.action_rename_playlist);
            menu.removeItem(R.id.action_delete_playlist);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void showPlaylistSongs(List<Music> songList) {
        hideLoading();
        musicList.addAll(songList);
        mAdapter.setNewData(musicList);
        if (mPlaylist != null && mPlaylist.getCoverUrl() != null) {
            coverUrl = mPlaylist.getCoverUrl();
        } else if (mArtist != null && mArtist.getPicUrl() != null) {
            coverUrl = mArtist.getPicUrl();
        } else if (musicList.size() >= 1) {
            coverUrl = musicList.get(0).getCoverUri();
        }
        CoverLoader.loadImageView(getContext(), coverUrl, album_art);
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
