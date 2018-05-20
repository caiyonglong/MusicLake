package com.cyl.musiclake.ui.music.online.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.ui.music.online.activity.NeteasePlaylistActivity;
import com.cyl.musiclake.ui.music.online.adapter.TopListAdapter;
import com.cyl.musiclake.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
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
public class NeteasePlaylistFragment extends BaseFragment {

    private static final String TAG = "NeteasePlaylistFragment";
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
    //适配器
    private TopListAdapter mAdapter;
    List<NeteaseList> neteaseLists = new ArrayList<>();

    {
        neteaseLists.clear();
        neteaseLists.add(new NeteaseList("云音乐新歌榜"));
        neteaseLists.add(new NeteaseList("云音乐热歌榜"));
        neteaseLists.add(new NeteaseList("网易原创歌曲榜"));
        neteaseLists.add(new NeteaseList("云音乐飙升榜"));
        neteaseLists.add(new NeteaseList("云音乐电音榜"));
        neteaseLists.add(new NeteaseList("UK排行榜周榜"));
        neteaseLists.add(new NeteaseList("美国Billboard周榜 "));
        neteaseLists.add(new NeteaseList("KTV嗨榜 "));
        neteaseLists.add(new NeteaseList("iTunes榜 "));
        neteaseLists.add(new NeteaseList("Hit FM Top榜 "));
        neteaseLists.add(new NeteaseList("日本Oricon周榜 "));
        neteaseLists.add(new NeteaseList("韩国Melon排行榜周榜 "));
        neteaseLists.add(new NeteaseList("韩国Mnet排行榜周榜 "));
        neteaseLists.add(new NeteaseList("韩国Melon原声周榜 "));
        neteaseLists.add(new NeteaseList("中国TOP排行榜(港台榜) "));
        neteaseLists.add(new NeteaseList("中国TOP排行榜(内地榜)"));
        neteaseLists.add(new NeteaseList("香港电台中文歌曲龙虎榜 "));
        neteaseLists.add(new NeteaseList("华语金曲榜"));
        neteaseLists.add(new NeteaseList("中国嘻哈榜"));
        neteaseLists.add(new NeteaseList("法国 NRJ EuroHot 30 周榜"));
        neteaseLists.add(new NeteaseList("台湾Hito排行榜 "));
        neteaseLists.add(new NeteaseList("Beatport全球电子舞曲榜"));
    }

    public static NeteasePlaylistFragment newInstance() {
        Bundle args = new Bundle();

        NeteasePlaylistFragment fragment = new NeteasePlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    public void initViews() {
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //适配器
        mAdapter = new TopListAdapter(neteaseLists);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void loadData() {
        for (int i = 0; i < neteaseLists.size(); i++) {
            try {
                int finalI = i;
                Observable<Playlist> observable
                        = MusicApi.getTopList(i);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Playlist>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Playlist playlist) {
//                                neteaseLists.set(finalI, playlist);
                                LogUtil.e(TAG, playlist.toString());
                                mAdapter.notifyItemChanged(finalI);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e(TAG, e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }

    }

    @Override
    protected void listener() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), NeteasePlaylistActivity.class);
            intent.putExtra("title", neteaseLists.get(position).getName());
            intent.putExtra("id", position);
            intent.putExtra("netease", neteaseLists.get(position));
            startActivity(intent);
        });
    }

    private void updateLists() {
        mAdapter.setNewData(neteaseLists);
    }
}