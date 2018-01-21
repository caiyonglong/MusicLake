package com.cyl.musiclake.ui.onlinemusic.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.onlinemusic.SearchAdapter;
import com.cyl.musiclake.ui.onlinemusic.contract.SearchContract;
import com.cyl.musiclake.ui.onlinemusic.presenter.SearchPresenter;
import com.cyl.musiclake.utils.ToastUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchContract.View {

    //搜索信息
    private String queryString;
    private SearchAdapter mAdapter;
    private MaterialDialog mProgressDialog;
    private int mOffset = 1;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private List<Music> searchResults = new ArrayList<>();

    SearchPresenter mPresenter = new SearchPresenter();

    private int mCurrentCounter = 0;
    private int TOTAL_COUNTER = 10;

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

        mAdapter = new SearchAdapter(searchResults);
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

    @SuppressWarnings({"unchecked", "varargs"})
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = (Music) adapter.getItem(position);
            Log.e("TAH", music.toString());
            PlayManager.playOnline(music);
            PlayManager.setPlayList(adapter.getData());
        });
        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            if (mCurrentCounter >= TOTAL_COUNTER) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd();
            } else {
                //成功获取更多数据
                mPresenter.search(queryString, 10, mOffset);
                mCurrentCounter = mAdapter.getData().size();
                TOTAL_COUNTER = 10 * mOffset;
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
        searchView.setSubmitButtonEnabled(true);
        try {
            Field field = searchView.getClass().getDeclaredField("mGoButton");
            field.setAccessible(true);
            ImageView mGoButton = (ImageView) field.get(searchView);
            mGoButton.setImageResource(R.drawable.ic_search_white_18dp);
            mGoButton.setOnClickListener(v -> {
                queryString = searchView.getQuery().toString();
                if (queryString.length() > 0) {
                    mOffset = 1;
                    searchResults.clear();
                    mProgressDialog.show();
                    mPresenter.search(queryString, 10, mOffset);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            mOffset = 1;
            searchResults.clear();
            mPresenter.search(query, 10, mOffset);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            mOffset = 1;
            searchResults.clear();
            mPresenter.search(newText, 10, mOffset);
        }
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
        mProgressDialog.cancel();
    }

    @Override
    public void showSearchResult(List<Music> list) {
        if (list.size() == 0) {
            mAdapter.setEmptyView(R.layout.view_song_empty);
        }
        if (mOffset == 1) {
            mAdapter.setNewData(list);
        } else {
            mAdapter.addData(list);
        }
        searchResults.addAll(list);
        mOffset++;
    }

    @Override
    public void showEmptyView() {

    }
}
