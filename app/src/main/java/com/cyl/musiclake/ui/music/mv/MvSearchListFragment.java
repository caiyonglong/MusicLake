package com.cyl.musiclake.ui.music.mv;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cyl.musicapi.netease.MvInfoDetail;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.base.BaseLazyFragment;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class MvSearchListFragment extends BaseLazyFragment<MvListPresenter> implements MvListContract.View {

    private static final String TAG = "MvSearchListFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    @BindView(R.id.tv_search)
    TextView searchTv;

    private String searchInfo;
    private Boolean isReset = false;

    @OnClick(R.id.tv_search)
    void search() {
        searchInfo = searchEditText.getText().toString();
        if (TextUtils.isEmpty(searchInfo)) {
            ToastUtils.show("请输入搜索内容");
        } else {
            mOffset = 0;
            if (mPresenter != null) {
                isReset = true;
                mPresenter.searchMv(searchInfo, mOffset);
            }
        }
    }

    private int mOffset = 0;

    //适配器
    private TopMvListAdapter mAdapter;
    private List<MvInfoDetail> mvList = new ArrayList<>();

    public static MvSearchListFragment newInstance() {
        Bundle args = new Bundle();
        MvSearchListFragment fragment = new MvSearchListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mv_search;
    }

    @Override
    public void initViews() {
        //适配器
        mAdapter = new TopMvListAdapter(mvList);
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            //成功获取更多数据
            if (mPresenter != null) {
                mPresenter.searchMv(searchInfo, mvList.size());
            }
        }, 1000));

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
        if (mvList.size() == 0) {
            showEmptyState();
        }
    }

    @Override
    protected void retryLoading() {
        super.retryLoading();
        mvList.clear();
        if (mPresenter != null) {
            mOffset = 0;
            isReset = true;
            mPresenter.searchMv(searchInfo, mOffset);
        }
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mFragmentComponent.getActivity(), VideoDetailActivity.class);
            intent.putExtra(Extras.MV_TITLE, mvList.get(position).getName());
            intent.putExtra(Extras.VIDEO_VID, String.valueOf(mvList.get(position).getId()));
            startActivity(intent);
        });
    }

    @Override
    public void onLazyLoad() {
        mvList.clear();
    }


    @Override
    public void showMvList(List<MvInfoDetail> mvList) {
        if (isReset) {
            isReset = false;
            this.mvList.clear();
        }
        this.mvList.addAll(mvList);
        mAdapter.setNewData(this.mvList);
    }

}