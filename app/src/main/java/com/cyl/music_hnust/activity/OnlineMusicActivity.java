package com.cyl.music_hnust.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cyl.music_hnust.Json.JsonCallback;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.OnlineMusicAdapter;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.OnlineMusicInfo;
import com.cyl.music_hnust.model.OnlineMusicList;
import com.cyl.music_hnust.model.OnlinePlaylist;
import com.cyl.music_hnust.service.PlayOnlineMusic;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineMusicActivity extends BaseActivity {


    private OnlinePlaylist mListInfo;
    private OnlineMusicList mMusicList;

    private List<OnlineMusicInfo> mMusicLists = new ArrayList<>();
    private OnlineMusicAdapter mAdapter;
    XRecyclerView mRecyclerView;
    Toolbar toolbar;

    private View vHeader;
    private PlayService mPlayService;
    private ProgressDialog mProgressDialog;
    private int mOffset = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_online;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        mListInfo = (OnlinePlaylist) getIntent().getSerializableExtra("online_list_type");
        setTitle(mListInfo.getTitle());

        mRecyclerView = (XRecyclerView) findViewById(R.id.xrecyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener(new OnlineMusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                play(mMusicLists.get(position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void initDatas() {
        bindService();

        mAdapter = new OnlineMusicAdapter(this, mMusicLists);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
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
            onLoad();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void onLoad() {
        getMusic(mOffset);
    }
    private void getMusic(final int offset) {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST)
                .addParams(Constants.PARAM_TYPE, mListInfo.getType())
                .addParams(Constants.PARAM_SIZE, String.valueOf(Constants.MUSIC_LIST_SIZE))
                .addParams(Constants.PARAM_OFFSET, String.valueOf(offset))
                .build()
                .execute(new JsonCallback<OnlineMusicList>(OnlineMusicList.class) {
                    @Override
                    public void onResponse(OnlineMusicList response) {
                        mMusicList = response;
                        if (offset == 0 && response == null) {
//                            ViewUtils.changeViewState(lvOnlineMusic, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
                            return;
                        } else if (offset == 0) {
//                            initHeader();
//                            ViewUtils.changeViewState(lvOnlineMusic, llLoading, llLoadFail, LoadStateEnum.LOAD_SUCCESS);
                        }
                        if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
//                            lvOnlineMusic.setEnable(false);
                            mRecyclerView.loadMoreComplete();
                            return;
                        }
                        mOffset += Constants.MUSIC_LIST_SIZE;
                        mMusicLists.addAll(response.getSong_list());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
//                        lvOnlineMusic.onLoadComplete();
                        if (e instanceof RuntimeException) {
                            // 歌曲全部加载完成
//                            lvOnlineMusic.setEnable(false);
                            mRecyclerView.loadMoreComplete();
                            return;
                        }
                        if (offset == 0) {
//                            ViewUtils.changeViewState(lvOnlineMusic, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
                        } else {
//                            ToastUtils.show(R.string.load_fail);
                        }
                    }
                });
    }

    private void play(OnlineMusicInfo musicInfo) {
        new PlayOnlineMusic(this, musicInfo) {

            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onSuccess(Music music) {
                mProgressDialog.cancel();
                mPlayService.playMusic(music);
                ToastUtil.show(getApplicationContext(),getString(R.string.now_play, music.getTitle()));
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
                ToastUtil.show(getApplicationContext(),R.string.unable_to_play);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayServiceConnection);
        super.onDestroy();
    }
}
