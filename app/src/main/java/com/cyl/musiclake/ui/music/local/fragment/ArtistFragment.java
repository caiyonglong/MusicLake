package com.cyl.musiclake.ui.music.local.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigateUtil;
import com.cyl.musiclake.ui.music.local.adapter.ArtistAdapter;
import com.cyl.musiclake.ui.music.local.contract.ArtistContract;
import com.cyl.musiclake.ui.music.local.presenter.ArtistPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class ArtistFragment extends BaseFragment implements ArtistContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;

    private ArtistAdapter mAdapter;
    private List<Artist> artists = new ArrayList<>();
    private ArtistPresenter mPresenter;

    public static ArtistFragment newInstance() {
        Bundle args = new Bundle();
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void loadData() {
        mPresenter.loadArtists("all");
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initViews() {
        mPresenter = new ArtistPresenter(getContext());
        mPresenter.attachView(this);

        mAdapter = new ArtistAdapter(artists);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);

    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Artist artist = (Artist) adapter.getItem(position);
            NavigateUtil.navigateToArtist(getActivity(),
                    artist.getId() + "",
                    artist.getName(),
                    new Pair<View, String>(view.findViewById(R.id.album), Constants.TRANSTITION_ALBUM));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showArtists(List<Artist> artists) {
        mAdapter.setNewData(artists);
    }

    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }
}
