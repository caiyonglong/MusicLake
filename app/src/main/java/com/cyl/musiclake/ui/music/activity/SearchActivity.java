package com.cyl.musiclake.ui.music.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.music.adapter.SearchAdapter;
import com.cyl.musiclake.ui.music.contract.SearchContract;
import com.cyl.musiclake.ui.music.model.Music;
import com.cyl.musiclake.ui.music.presenter.SearchPresenter;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchAdapter.OnItemClickListener, SearchContract.View {

    //搜索信息
    private String queryString;
    private SearchAdapter mAdapter;
    private MaterialDialog mProgressDialog;
    private int mOffset = 1;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.xrecyclerview)
    XRecyclerView mRecyclerView;

    private List<Music> searchResults = new ArrayList<>();

    SearchPresenter mPresenter = new SearchPresenter();

    @Override
    protected void listener() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.acitvity_search;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter.attachView(this);
    }

    @Override
    protected void initData() {

        mAdapter = new SearchAdapter(this, searchResults);
        mAdapter.setOnItemClickListener(this);

        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        mProgressDialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .build();

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                mOffset = 1;
                mPresenter.search(queryString, 10, mOffset);
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                mPresenter.search(queryString, 10, mOffset);
                mRecyclerView.loadMoreComplete();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.search_tips));
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        try {
            Field field = searchView.getClass().getDeclaredField("mGoButton");
            field.setAccessible(true);
            ImageView mGoButton = (ImageView) field.get(searchView);
            mGoButton.setImageResource(R.drawable.ic_search_white_18dp);
            mGoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queryString = searchView.getQuery().toString();
                    if (queryString.length() > 0) {
                        mOffset = 1;
                        searchResults.clear();
                        mPresenter.search(queryString, 10, mOffset);
                    } else {
                        ToastUtils.show(getApplicationContext(), "不能搜索空文本");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {

        switch (view.getId()) {
            case R.id.iv_more:
                setOnPopupMenuListener(view, position);
                break;
            default:
//                play(searchResults.get(position));
                break;
        }
    }

    private void setOnPopupMenuListener(View view, final int position) {
        PopupMenu mPopupmenu = new PopupMenu(this, view);
        mPopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
//                        play(searchResults.get(position));
                        break;
                    case R.id.popup_song_detail:
                        getMusicInfo(searchResults.get(position));
                        break;
                    case R.id.popup_song_goto_artist:
//                        Intent intent = new Intent(SearchActivity.this, ArtistInfoActivity.class);
//                        intent.putExtra(Extras.TING_UID, searchResults.get(position).getar());
//                        startActivity(intent);
                        break;
                    case R.id.popup_song_download:
                        break;

                }
                return false;
            }
        });
        mPopupmenu.inflate(R.menu.popup_song_online);
        mPopupmenu.show();
    }

    private void getMusicInfo(Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("歌曲信息");
        StringBuilder sb = new StringBuilder();
        sb.append("歌曲名：")
                .append(FileUtils.getTitle(music.getTitle()))
                .append("\n\n")
                .append("歌手：")
                .append(FileUtils.getArtist(music.getArtist()))
                .append("\n\n")
                .append("专辑：")
                .append(music.getAlbum())
                .append("\n\n")
                .append("专辑Id：")
                .append(music.getAlbumId());
        dialog.setMessage(sb.toString());
        dialog.show();
    }


    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.cancel();
    }

    @Override
    public void showSearchResult(List<Music> list) {
        searchResults.addAll(list);
        mAdapter.notifyDataSetChanged();
        mOffset++;
    }

    @Override
    public void showEmptyView() {

    }
}
