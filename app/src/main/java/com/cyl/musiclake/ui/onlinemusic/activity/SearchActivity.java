package com.cyl.musiclake.ui.onlinemusic.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.onlinemusic.SearchAdapter;
import com.cyl.musiclake.ui.onlinemusic.contract.SearchContract;
import com.cyl.musiclake.ui.onlinemusic.presenter.SearchPresenter;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchContract.View {

    private static final String TAG = "SearchActivity";
    //搜索信息
    private String queryString;
    private SearchAdapter mAdapter;
    private MaterialDialog mProgressDialog;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Music> searchResults = new ArrayList<>();

    SearchPresenter mPresenter = new SearchPresenter();

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter.attachView(this);
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

    @SuppressWarnings({"unchecked", "varargs"})
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Music music = searchResults.get(position);
            Log.e(TAG, music.toString());
            mPresenter.play(music);
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
    }
}
