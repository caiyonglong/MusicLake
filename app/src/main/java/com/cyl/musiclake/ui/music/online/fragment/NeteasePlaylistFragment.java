package com.cyl.musiclake.ui.music.online.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.ui.music.online.activity.NeteaseMusicListActivity;
import com.cyl.musiclake.ui.music.online.adapter.TopListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class NeteasePlaylistFragment extends BaseFragment {

    private static final String TAG = "BaiduPlaylistFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.loading)
    LinearLayout loading;
    //适配器
    private TopListAdapter mAdapter;
    List<String> list = new ArrayList<>();

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
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        list.add("云音乐新歌榜");
        list.add("云音乐热歌榜");
        list.add("网易原创歌曲榜");
        list.add("云音乐飙升榜");
        list.add("云音乐电音榜");
        list.add("UK排行榜周榜");
        list.add("美国Billboard周榜 ");
        list.add("KTV嗨榜 ");
        list.add("iTunes榜 ");
        list.add("Hit FM Top榜 ");
        list.add("日本Oricon周榜 ");
        list.add("韩国Melon排行榜周榜 ");
        list.add("韩国Mnet排行榜周榜 ");
        list.add("韩国Melon原声周榜 ");
        list.add("中国TOP排行榜(港台榜) ");
        list.add("中国TOP排行榜(内地榜)");
        list.add("香港电台中文歌曲龙虎榜 ");
        list.add("华语金曲榜");
        list.add("中国嘻哈榜");
        list.add("法国 NRJ EuroHot 30 周榜");
        list.add("台湾Hito排行榜 ");
        list.add("Beatport全球电子舞曲榜");
        list.add("云音乐ACG音乐榜");
        list.add("云音乐嘻哈榜");
        //适配器
        mAdapter = new TopListAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), NeteaseMusicListActivity.class);
            intent.putExtra("id", position);
            intent.putExtra("title", list.get(position));
            startActivity(intent);
        });
    }

}