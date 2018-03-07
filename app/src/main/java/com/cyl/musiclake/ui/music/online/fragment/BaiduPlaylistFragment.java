package com.cyl.musiclake.ui.music.online.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.baidu.BaiduMusicList.Billboard;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.music.online.activity.BaiduMusicListActivity;
import com.cyl.musiclake.ui.music.online.adapter.OnlineAdapter;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;
import com.cyl.musiclake.ui.music.online.presenter.OnlinePlaylistPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class BaiduPlaylistFragment extends BaseFragment implements OnlinePlaylistContract.View {

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
    private OnlineAdapter mAdapter;
    //排行榜集合
    private OnlinePlaylistPresenter mPresenter;

    public static BaiduPlaylistFragment newInstance() {
        Bundle args = new Bundle();

        BaiduPlaylistFragment fragment = new BaiduPlaylistFragment();
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
        mAdapter = new OnlineAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

        mPresenter = new OnlinePlaylistPresenter(getContext());
        mPresenter.attachView(this);
    }

    @Override
    protected void loadData() {
//        初始化列表,当无数据时显示提示
        mPresenter.loadOnlinePlaylist();
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Billboard billboard = (Billboard) adapter.getItem(position);
            Intent intent = new Intent(getActivity(), BaiduMusicListActivity.class);
            intent.putExtra(Extras.BILLBOARD_TITLE, billboard.getName());
            intent.putExtra(Extras.BILLBOARD_DESC, billboard.getComment());
            intent.putExtra(Extras.BILLBOARD_ALBUM, billboard.getPic_s260());
            intent.putExtra(Extras.BILLBOARD_TYPE, billboard.getType());
            startActivity(intent);
        });
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showErrorInfo(String msg) {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }

    @Override
    public void showOnlineSongs(List<Billboard> mBillboards) {
        mAdapter.setNewData(mBillboards);
    }
}