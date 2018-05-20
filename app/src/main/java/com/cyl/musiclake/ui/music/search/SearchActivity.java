package com.cyl.musiclake.ui.music.search;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.cyl.musiclake.musicapi.playlist.AddPlaylistUtils;
import com.cyl.musiclake.utils.LogUtil;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.list.dialog.ShowDetailDialog;
import com.cyl.musiclake.ui.music.online.DownloadDialog;
import com.cyl.musiclake.ui.music.online.activity.ArtistInfoActivity;
import com.cyl.musiclake.ui.music.online.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchView.OnQueryTextListener, SearchContract.View {

    private static final String TAG = "SearchActivity";
    //搜索信息
    private String queryString;
    private SearchAdapter mAdapter;
    private MaterialDialog mProgressDialog;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Music> searchResults = new ArrayList<>();


    private int mCurrentCounter = 0;
    private int TOTAL_COUNTER = 0;
    private int limit = 10;
    private int mOffset = 1;


    @Override
    protected int getLayoutResID() {
        return R.layout.acitvity_search;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mAdapter = new SearchAdapter(searchResults);
        mAdapter.setEnableLoadMore(true);
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

        mProgressDialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .build();
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @SuppressWarnings({"unchecked", "varargs"})
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = searchResults.get(position);
            LogUtil.e(TAG, music.toString());
            mPresenter.getMusicInfo(0, music);
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = searchResults.get(position);
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_song_detail:
                        ShowDetailDialog.newInstance(music)
                                .show(getSupportFragmentManager(), TAG);
                        break;
                    case R.id.popup_song_goto_artist:
                        LogUtil.e(TAG, music.toString());
                        Intent intent = new Intent(this, ArtistInfoActivity.class);
                        intent.putExtra(Extras.TING_UID, music);
                        startActivity(intent);
                        break;
                    case R.id.popup_add_playlist:
                        AddPlaylistUtils.getPlaylist(this, music);
                        break;
                    case R.id.popup_song_download:
                        mPresenter.getMusicInfo(1, music);
                        break;
                }
                return true;
            });
            popupMenu.inflate(R.menu.popup_song_online);
            popupMenu.show();
        });
        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            TOTAL_COUNTER = limit * mOffset * 2;
            if (mCurrentCounter < TOTAL_COUNTER) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd();
            } else {
                mOffset++;
                //成功获取更多数据
                mPresenter.search(queryString, limit, mOffset);
            }
        }, 1000), mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.search_tips));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            mOffset = 1;
            searchResults.clear();
            queryString = query;
            mPresenter.search(query, limit, mOffset);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            mOffset = 1;
            searchResults.clear();
            queryString = newText;
            mPresenter.search(newText, limit, mOffset);
        }
        LogUtil.e(TAG, "time = start2");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showSearchResult(List<Music> list) {
        searchResults.addAll(list);
        mAdapter.setNewData(searchResults);
        mAdapter.loadMoreComplete();
        mCurrentCounter = mAdapter.getData().size();
        LogUtil.e("search", mCurrentCounter + "--" + TOTAL_COUNTER + "--" + mOffset);
    }

    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }

    @Override
    public void showMusicInfo(int type, Music music) {
        if (type == 0) {
            PlayManager.playOnline(music);
        } else if (type == 1) {
            DownloadDialog.newInstance(music)
                    .show(getSupportFragmentManager(), getLocalClassName());
        }
    }
}
