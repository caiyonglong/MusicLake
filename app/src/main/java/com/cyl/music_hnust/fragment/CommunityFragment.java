package com.cyl.music_hnust.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.EditActivity;
import com.cyl.music_hnust.adapter.CommunityAdapter;
import com.cyl.music_hnust.callback.SecretCallback;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.community.Secret;
import com.cyl.music_hnust.model.community.SecretInfo;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Monkey on 2015/6/29.
 */
public class CommunityFragment extends BaseFragment implements XRecyclerView.LoadingListener {


    XRecyclerView mRecyclerView;
    FloatingActionButton fab;
    TextView tv_empty;

    CommunityAdapter MyAdapter;
    private int pagenum = 1;
    private int flag = 1;
    private String user_id = "";

    private static List<Secret> mdatas = new ArrayList<>();

    public static CommunityFragment newInstance(int flag) {

        Bundle args = new Bundle();
        args.putInt(Extras.COMMUNITY_FLAG, flag);
        CommunityFragment fragment = new CommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFab();
            }
        });
    }

    @Override
    protected void initDatas() {
        MyAdapter = new CommunityAdapter(getContext(), mdatas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(MyAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean is = Math.abs(dy) > 4;

                if (is) {
                    if (dy > 0) {
                        fab.hide();
                    } else {
                        fab.show();
                    }
                }
            }
        });

        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        if (UserStatus.getstatus(getContext())) {
            user_id = UserStatus.getUserInfo(getContext()).getUser_id();
        } else {
            user_id = "";
        }
        getSecret(pagenum);
    }

    @Override
    public int getLayoutId() {
        return R.layout.acitvity_community;
    }

    @Override
    public void initViews() {
        mRecyclerView = (XRecyclerView) rootView.findViewById(R.id.community_RecyclerView);
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        flag = (int) getArguments().get(Extras.COMMUNITY_FLAG);
        if (flag != 1) {
            fab.setVisibility(View.GONE);
        }
        pagenum = 1;
    }

    public void setFab() {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        startActivity(intent);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        Log.e("111====1", String.valueOf(pagenum));
        mdatas.clear();
        pagenum = 1;
        getSecret(pagenum);
        mRecyclerView.refreshComplete();
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        Log.e("111======1", String.valueOf(pagenum));
        getSecret(pagenum);
        mRecyclerView.loadMoreComplete();
    }

    private void getSecret(int PageNum) {
        Map<String, String> params = new HashMap<>();
        if (flag == 1) {
            params.put(Constants.FUNC, Constants.GET_SECRET_LIST);
        } else {
            params.put(Constants.FUNC, Constants.GET_MYSECRET_LIST);
        }
        params.put(Constants.PAGENUM, String.valueOf(PageNum));
        params.put(Constants.USER_ID, user_id);

        OkHttpUtils.post().url(Constants.DEFAULT_URL)
                .params(params)
                .build()
                .execute(new SecretCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        tv_empty.setText(R.string.error_connection);
                        tv_empty.setVisibility(View.VISIBLE);
                        ToastUtils.show(getActivity(), R.string.error_connection);
                    }

                    @Override
                    public void onResponse(SecretInfo response) {
                        if (response.getStatus() == 1) {
                            if (response.getData() == null || response.getData().size() == 0) {
                                mRecyclerView.loadMoreComplete();
                                return;
                            }
                            pagenum = pagenum + 1;
                            mdatas.addAll(response.getData());
                            if (response.getData().size() <= 0) {
                                tv_empty.setText("暂无动态");
                                tv_empty.setVisibility(View.VISIBLE);
                            } else {
                                tv_empty.setVisibility(View.GONE);
                            }
                            MyAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        mdatas.clear();
        pagenum = 1;
        getSecret(pagenum);
    }
}

