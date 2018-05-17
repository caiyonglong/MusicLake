package com.cyl.musiclake.ui.music.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musicapi.callback.TopListApiListener;
import com.cyl.musicapi.impl.TopListApiImpl;
import com.cyl.musiclake.ui.music.online.activity.NeteaseMusicListActivity;
import com.cyl.musiclake.ui.music.online.adapter.TopPlaylistAdapter;
import com.cyl.musiclake.ui.music.online.fragment.NeteasePlaylistFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class DiscoverFragment extends BaseLazyFragment implements TopListApiListener {

    private static final String TAG = "FoundFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.btn_baidu)
    void toBaidu() {
        new TopListApiImpl(getActivity(), this).getTopList("1");
    }

    @OnClick(R.id.btn_netease)
    void toNetease() {
        NavigationHelper.navigateFragment(getActivity(), NeteasePlaylistFragment.newInstance());
    }

    //适配器
    private TopPlaylistAdapter mAdapter;
    List<NeteaseList> neteaseLists = new ArrayList<>();

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();

        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_find;
    }

    @Override
    public void initViews() {
        //初始化列表
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //适配器
        mAdapter = new TopPlaylistAdapter(neteaseLists);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

    }

    @Override
    protected void loadData() {
        MusicApi.getTopPlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NeteaseList>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(List<NeteaseList> lists) {
                        neteaseLists = lists;
                        mAdapter.setNewData(lists);
                        if (lists.size() == 0) {
                            mAdapter.setEmptyView(R.layout.view_song_empty);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mAdapter.setEmptyView(R.layout.view_song_empty);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void listener() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), NeteaseMusicListActivity.class);
            intent.putExtra("netease", neteaseLists.get(position));
            startActivity(intent);
        });
    }

    private void updateLists() {
        mAdapter.setNewData(neteaseLists);
    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void getTopList(List<Music> musicList) {

    }
}