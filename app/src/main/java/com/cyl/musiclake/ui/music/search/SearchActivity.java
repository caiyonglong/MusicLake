package com.cyl.musiclake.ui.music.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.data.db.DaoLitepal;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.db.SearchHistoryBean;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment;
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    private static final String TAG = "SearchActivity";
    //搜索信息
    private String queryString;
    private SongAdapter mAdapter;
    private SearchHistoryAdapter historyAdapter;


    @BindView(R.id.recyclerView)
    RecyclerView resultListRcv;
    @BindView(R.id.suggestions_list)
    RecyclerView historyRcv;
    @BindView(R.id.history_panel)
    View historyPanel;
    @BindView(R.id.toolbar_search_edit_text)
    EditText searchEditText;
    @BindView(R.id.toolbar_search_container)
    View searchToolbarContainer;

    @OnClick(R.id.clearAllIv)
    void clearAll() {
        DaoLitepal.INSTANCE.clearAllSearch();
        historyAdapter.setNewData(null);
    }

    @OnClick(R.id.toolbar_search_clear)
    void clearQuery() {
        queryString = "";
        searchEditText.setText("");
    }

    private List<Music> searchResults = new ArrayList<>();
    private List<SearchHistoryBean> suggestions = new ArrayList<>();

    private int mCurrentCounter = 10;
    private int limit = 10;
    private int mOffset = 0;


    @Override
    protected int getLayoutResID() {
        return R.layout.acitvity_search;
    }

    @Override
    protected void initView() {
        showSearchOnStart();
    }

    private void showSearchOnStart() {
        searchEditText.setText(queryString);

        if (TextUtils.isEmpty(queryString) || TextUtils.isEmpty(searchEditText.getText())) {
            searchToolbarContainer.setTranslationX(100);
            searchToolbarContainer.setAlpha(0f);
            searchToolbarContainer.setVisibility(View.VISIBLE);
            searchToolbarContainer.animate().translationX(0).alpha(1f).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
        } else {
            searchToolbarContainer.setTranslationX(0);
            searchToolbarContainer.setAlpha(1f);
            searchToolbarContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        mAdapter = new SongAdapter(searchResults);
        mAdapter.setEnableLoadMore(true);
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultListRcv.setLayoutManager(layoutManager);
        resultListRcv.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(resultListRcv);
        mPresenter.getSearchHistory();
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
            PlayManager.playOnline(music);
            NavigationHelper.INSTANCE.navigateToPlaying(this);
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = searchEditText.getText().toString();
                if (newText.length() == 0) {
                    mPresenter.getSearchHistory();
                    updateHistoryPanel(true);
                }
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            LogUtil.d(TAG, "onEditorAction() called with: v = [" + v + "], actionId = [" + actionId + "], event = [" + event + "]");
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getAction() == EditorInfo.IME_ACTION_SEARCH)) {
                search(searchEditText.getText().toString());
                return true;
            }
            return false;
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Music music = searchResults.get(position);
            BottomDialogFragment.Companion.newInstance(music, Constants.OP_ONLINE).show(this);
        });
        mAdapter.setOnLoadMoreListener(() -> resultListRcv.postDelayed(() -> {
            if (mCurrentCounter == 0) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd();
            } else {
                mOffset++;
                //成功获取更多数据
                mPresenter.search(queryString, filter, limit, mOffset);
            }
        }, 1000), resultListRcv);


    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
//        restoreFilterChecked(menu, filterItemCheckedId);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.menu_filter_all:
//            case R.id.menu_filter_qq:
//            case R.id.menu_filter_xiami:
//            case R.id.menu_filter_netease:
//                changeFilter(item, getFilterFromMenuId(item.getItemId()));
//                break;
            case R.id.menu_search:
                queryString = searchEditText.getText().toString().trim();
                search(queryString);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    SearchEngine.Filter filter = SearchEngine.Filter.ANY;

    private void changeFilter(MenuItem item, SearchEngine.Filter filter) {
        this.filter = filter;
        this.filterItemCheckedId = item.getItemId();
        item.setChecked(true);

        if (!TextUtils.isEmpty(queryString)) {
            search(queryString);
        }
    }


//    private void restoreFilterChecked(Menu menu, int itemId) {
//        if (itemId != -1) {
//            MenuItem item = menu.findItem(itemId);
//            if (item == null) return;
//
//            item.setChecked(true);
//            filter = getFilterFromMenuId(itemId);
//        }
//    }


    int filterItemCheckedId = -1;

//    private SearchEngine.Filter getFilterFromMenuId(int itemId) {
//        switch (itemId) {
//            case R.id.menu_filter_qq:
//                return SearchEngine.Filter.QQ;
//            case R.id.menu_filter_xiami:
//                return SearchEngine.Filter.XIAMI;
//            case R.id.menu_filter_netease:
//                return SearchEngine.Filter.NETEASE;
//            case R.id.menu_filter_all:
//            default:
//                return SearchEngine.Filter.ANY;
//        }
//    }

    private void search(String query) {
        if (query != null && query.length() > 0) {
            mOffset = 0;
            searchResults.clear();
            queryString = query;
            updateHistoryPanel(false);
            mPresenter.saveQueryInfo(query);
            mPresenter.search(query, filter, limit, mOffset);
        }
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
    public void showSearchResult(List<Music> list) {
        searchResults.addAll(list);
        mAdapter.setNewData(searchResults);
        mAdapter.loadMoreComplete();
        mCurrentCounter = mAdapter.getData().size();
        LogUtil.e("search", mCurrentCounter + "--" + mCurrentCounter + "--" + mOffset);
    }

    @Override
    public void showSearchSuggestion(List<SearchHistoryBean> result) {
        suggestions = result;
        if (historyAdapter == null) {
            historyAdapter = new SearchHistoryAdapter(suggestions);
            historyRcv.setLayoutManager(new LinearLayoutManager(this));
            historyRcv.setAdapter(historyAdapter);
            historyAdapter.bindToRecyclerView(historyRcv);
            historyAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                return false;
            });
            historyAdapter.setOnItemClickListener((adapter, view, position) -> {
            });
            historyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.history_search) {
                    searchEditText.setText(suggestions.get(position).getTitle());
                    searchEditText.setSelection(suggestions.get(position).getTitle().length());
                } else if (view.getId() == R.id.deleteView) {
                    DaoLitepal.INSTANCE.deleteSearchInfo(suggestions.get(position).getTitle());
                    historyAdapter.remove(position);
                }
            });
        } else {
            suggestions = result;
            historyAdapter.setNewData(result);
        }
    }

    @Override
    public void showSearchHistory(List<SearchHistoryBean> result) {
        suggestions = result;
        if (historyAdapter == null) {
            historyAdapter = new SearchHistoryAdapter(suggestions);
            historyRcv.setLayoutManager(new LinearLayoutManager(this));
            historyRcv.setAdapter(historyAdapter);
            historyAdapter.bindToRecyclerView(historyRcv);
            historyAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                return false;
            });
            historyAdapter.setOnItemClickListener((adapter, view, position) -> {
            });
            historyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.history_search) {
                    searchEditText.setText(suggestions.get(position).getTitle());
                    searchEditText.setSelection(suggestions.get(position).getTitle().length());
                    search(suggestions.get(position).getTitle());
                } else if (view.getId() == R.id.deleteView) {
                    DaoLitepal.INSTANCE.deleteSearchInfo(suggestions.get(position).getTitle());
                    historyAdapter.remove(position);
                }
            });
        } else {
            suggestions = result;
            historyAdapter.setNewData(result);
        }
    }

    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }

    private void updateHistoryPanel(boolean isShow) {
        if (isShow) {
            resultListRcv.setVisibility(View.GONE);
            historyPanel.setVisibility(View.VISIBLE);
        } else {
            resultListRcv.setVisibility(View.VISIBLE);
            historyPanel.setVisibility(View.GONE);
        }
    }
}
