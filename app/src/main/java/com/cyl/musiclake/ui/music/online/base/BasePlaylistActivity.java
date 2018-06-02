package com.cyl.musiclake.ui.music.online.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.api.AddPlaylistUtils;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.ui.music.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.download.DownloadDialog;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.DialogUtils;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.SizeUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public abstract class BasePlaylistActivity extends BaseActivity<PlaylistPresenter> implements PlaylistContract.View {

    private static final String TAG = "BaiduMusicListActivity";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private View mViewHeader;
    private ImageView mIvBackground;
    private ImageView mIvCover;
    private TextView mTvTitle;
    private TextView mTvDate;
    private TextView mTvDesc;

    private int action = 0; //0 播放，1 下载
    private Playlist mPlaylist;
    private SongAdapter mAdapter;
    private List<Music> musicList = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_online_playlist;
    }

    public abstract String getToolBarTitle();

    @Override
    protected void initView() {
        setToolbarTitle(getToolBarTitle());
        initHeaderView();
    }


    @Override
    protected void initData() {
        mAdapter = new SongAdapter(musicList);
        mAdapter.setEnableLoadMore(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
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
                PlayManager.setPlayList(musicList);
                PlayManager.play(position);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = mPlaylist.getMusicList().get(position);
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance(music)
                                .show(getSupportFragmentManager(), "");
                        break;
                    case R.id.popup_add_playlist:
                        AddPlaylistUtils.getPlaylist(this, music);
                        break;
                    case R.id.popup_song_download:
                        action = 1;
                        mPresenter.getMusicInfo(music);
                        break;
                }
                return false;
            });
            popupMenu.inflate(R.menu.popup_song_online);
            popupMenu.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    private void initHeaderView() {
        mViewHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_header, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this, 150));
        mViewHeader.setLayoutParams(params);

        mIvCover = (ImageView) mViewHeader.findViewById(R.id.iv_cover);
        mTvTitle = (TextView) mViewHeader.findViewById(R.id.tv_title);
        mTvDate = (TextView) mViewHeader.findViewById(R.id.tv_update_date);
        mTvDesc = (TextView) mViewHeader.findViewById(R.id.tv_comment);
    }

    @Override
    public void showLoading() {
        DialogUtils.showProgressDialog(this, null, null);
    }

    @Override
    public void hideLoading() {
        DialogUtils.dismissDialog();
    }

    @Override
    public void showErrorInfo(String msg) {
        ToastUtils.show(this, msg);
        mAdapter.loadMoreFail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHeaderInfo() {
        CoverLoader.loadImageView(this, mPlaylist.getCoverUrl(), mIvCover);
        mTvTitle.setText(mPlaylist.getName());
        mTvDate.setText(getString(R.string.recent_update, FormatUtil.distime(mPlaylist.getDate())));
        mTvDesc.setText(mPlaylist.getDes());
        mAdapter.setHeaderView(mViewHeader, 0);
    }

    @Override
    public void showPlayList(Playlist playlist) {
        mPlaylist = playlist;
        musicList = playlist.getMusicList();
        mAdapter.setNewData(playlist.getMusicList());
        showHeaderInfo();
    }

    public void showMusicInfo(Music music) {
        if (action == 0) {
            PlayManager.playOnline(music);
        } else if (action == 1) {
            DownloadDialog.newInstance(music)
                    .show(getSupportFragmentManager(), getLocalClassName());
        }
    }
}