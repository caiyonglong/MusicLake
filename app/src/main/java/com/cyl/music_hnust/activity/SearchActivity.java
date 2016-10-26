package com.cyl.music_hnust.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.cyl.music_hnust.Json.JsonCallback;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.MusicRecyclerViewAdapter;
import com.cyl.music_hnust.adapter.SearchAdapter;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.SearchMusic;
import com.cyl.music_hnust.service.PlaySearchedMusic;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：yonglong on 2016/9/15 12:32
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, MusicRecyclerViewAdapter.OnItemClickListener {
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private InputMethodManager mImm;
    private String queryString;

    private SearchAdapter mAdapter;

    private PlayService mPlayService;
    private ProgressDialog mProgressDialog;
    private int mOffset = 10;

    XRecyclerView mRecyclerView;

    private List<SearchMusic.SongList> searchResults = new ArrayList<>();

    @Override
    protected void listener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (XRecyclerView) findViewById(R.id.xrecyclerview);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
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
                    queryString = searchView.getQuery().toString().toString();
                    if (queryString.length() > 0) {
                        mOffset = 10;
                        searchResults.clear();
                        getMusic(mOffset);
                    } else {
                        ToastUtils.show(getApplicationContext(), "搜素內容不能為空");
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

    private void getMusic(final int offset) {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_SEARCH_MUSIC)
                .addParams(Constants.PARAM_QUERY, queryString)
                .addParams(Constants.PARAM_OFFSET, String.valueOf(offset))
                .build()
                .execute(new JsonCallback<SearchMusic>(SearchMusic.class) {
                    @Override
                    public void onResponse(SearchMusic response) {
                        if (offset == 0 && response == null) {
                            return;
                        } else if (offset == 0) {
//                            initHeader();
                        }
                        if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
                            mRecyclerView.loadMoreComplete();
                            return;
                        }
                        mOffset += Constants.MUSIC_LIST_SIZE;

                        Log.e("ddd11111111111", searchResults.size() + "" + "=====");
                        searchResults.addAll(response.getSong_list());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mRecyclerView.loadMoreComplete();
                        if (e instanceof RuntimeException) {
                            // 歌曲全部加载完成
                            mRecyclerView.setLoadingMoreEnabled(false);
                            return;
                        }
                        if (offset == 0) {
                        } else {
//                            ToastUtils.show(R.string.load_fail);
                        }
                    }
                });
    }

    private void init() {
        bindService();

        mAdapter = new SearchAdapter(this, searchResults);
        mAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                mOffset=10;
                getMusic(mOffset);
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                getMusic(mOffset);
                mRecyclerView.loadMoreComplete();
            }
        });
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    private void play(SearchMusic.SongList songList) {
        new PlaySearchedMusic(this,songList) {
            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(Music music) {
                mProgressDialog.cancel();
                Log.e("***********", music.toString());
                mPlayService.playMusic(music);
//                ToastUtils.show(getApplicationContext(),getString(R.string.now_play, music.getTitle()));
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
                ToastUtils.show(getApplicationContext(), R.string.unable_to_play);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
        play(searchResults.get(position));
    }
}
