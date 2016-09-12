package com.cyl.music_hnust.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.OnlineMusicActivity;
import com.cyl.music_hnust.adapter.OnlineAdapter;
import com.cyl.music_hnust.download.NetworkUtil;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.OnlinePlaylist;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineFragment extends BaseFragment implements OnlineAdapter.OnItemClickListener {

    XRecyclerView mRecyclerView;
    //适配器
    private OnlineAdapter mAdapter;
    //排行榜集合
    private List<OnlinePlaylist> mPlaylists = new ArrayList<>();
    private TextView tv_empty;

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview_online;
    }

    @Override
    public void initViews() {
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        mRecyclerView = (XRecyclerView) rootView.findViewById(R.id.xrecyclerview);



    }
    @Override
    protected void initDatas() {
        init();

        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //适配器
        mAdapter = new OnlineAdapter(getActivity(), mPlaylists);


        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                init();
                mAdapter.notifyDataSetChanged();
                //refresh data here
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                mRecyclerView.loadMoreComplete();
            }
        });

    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 初始化列表,当无数据时显示提示
     */
    private void init() {
        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            tv_empty.setText("暂无音乐!");
            tv_empty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        }
        if (mPlaylists.isEmpty()) {
            String[] titles=getResources().getStringArray(R.array.online_music_list_title);
            String[] types=getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < titles.length; i++) {
                OnlinePlaylist info = new OnlinePlaylist();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                mPlaylists.add(info);
            }
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_my, menu);
    }

    /**
     * 菜单点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
//        ToastUtil.show(getContext(),"===="+position);
        OnlinePlaylist songListInfo = mPlaylists.get(position);
        Intent intent = new Intent(getActivity(), OnlineMusicActivity.class);
        intent.putExtra("online_list_type", songListInfo);
        startActivity(intent);
    }
}