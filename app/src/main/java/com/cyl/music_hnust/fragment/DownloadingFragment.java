package com.cyl.music_hnust.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.DownloadManageAdapter;
import com.cyl.music_hnust.download.db.SqliteDao;
import com.cyl.music_hnust.download.model.FileState;
import com.cyl.music_hnust.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonglong on 2016/11/14.
 */
public class DownloadingFragment extends BaseFragment {

    //status 1:正在下载 0 0：已下载
    int status;
    RecyclerView mRecyclerView;
    LinearLayout loading;
    LinearLayoutManager mLayoutManager;
    DownloadManageAdapter mAdapter;

    private SqliteDao dao;
    /**
     * 存放要显示列表的数据
     */
    public static List<FileState> data = new ArrayList<>();

    public static DownloadingFragment newInstance(int status) {

        Bundle args = new Bundle();
        args.putInt("status", status);
        DownloadingFragment fragment = new DownloadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        status = getArguments().getInt("status");
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new DownloadManageAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        loading = (LinearLayout) rootView.findViewById(R.id.loading);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dao = new SqliteDao(getActivity());
        if (status==1) {
            data = dao.getFileStates();
        }else {
            data = dao.getFileStated();
        }
        mAdapter.setList(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (status==1) {
            data = dao.getFileStates();
        }else {
            data = dao.getFileStated();
        }
        mAdapter.setList(data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.updateFileState(data);
        getActivity().unregisterReceiver(mAdapter.receiver);
    }

}
