package com.cyl.music_hnust.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.Json.JsonCallback;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.OnlineMusicActivity;
import com.cyl.music_hnust.adapter.OnlineAdapter;
import com.cyl.music_hnust.download.NetworkUtil;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.OnlinePlaylists;
import com.cyl.music_hnust.model.OnlinePlaylists.Billboard;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineFragment extends BaseFragment implements OnlineAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    //适配器
    private OnlineAdapter mAdapter;
    //排行榜集合
    private List<Billboard> mBillboards = new ArrayList<>();
    private TextView tv_empty;

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview_online;
    }

    @Override
    public void initViews() {
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

    }
    @Override
    protected void initDatas() {

        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //适配器
        mAdapter = new OnlineAdapter(getActivity(), mBillboards);
        mRecyclerView.setAdapter(mAdapter);
        init();

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
            if (mBillboards.isEmpty()) {
                OkHttpUtils.get().url(Constants.BASE_MUSIC_URL)
                        .build()
                        .execute(new JsonCallback<OnlinePlaylists>(OnlinePlaylists.class) {
                            @Override
                            public void onResponse(OnlinePlaylists response) {
                                if (response == null || response.getContent() == null) {
                                    return;
                                }
                                mBillboards= response.getContent();
                                mAdapter.setmBillboards(mBillboards);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                            }
                        });
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
        Billboard billboard = mBillboards.get(position);
        Intent intent = new Intent(getActivity(), OnlineMusicActivity.class);
        intent.putExtra(Extras.BILLBOARD_TITLE, billboard.getName());
        intent.putExtra(Extras.BILLBOARD_TYPE, billboard.getType());
        startActivity(intent);
    }
}