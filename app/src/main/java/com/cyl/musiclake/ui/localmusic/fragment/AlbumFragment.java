package com.cyl.musiclake.ui.localmusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.data.model.Album;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.NavigateUtil;
import com.cyl.musiclake.ui.localmusic.adapter.AlbumAdapter;
import com.cyl.musiclake.ui.localmusic.contract.AlbumsContract;
import com.cyl.musiclake.ui.localmusic.presenter.AlbumPresenter;
import com.cyl.musiclake.ui.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class AlbumFragment extends BaseFragment implements AlbumsContract.View {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;
    private AlbumAdapter mAdapter;
    private List<Album> albumList = new ArrayList<>();
    private AlbumPresenter mPresenter;


    public static AlbumFragment newInstance() {

        Bundle args = new Bundle();
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        mPresenter.loadAlbums("all");
    }


    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initViews() {
        mPresenter = new AlbumPresenter(getContext());
        mPresenter.attachView(this);

        mAdapter = new AlbumAdapter(albumList);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Album album = (Album) adapter.getItem(position);
            NavigateUtil.navigateToAlbum(getActivity(),
                    album.getId(),
                    album.getName(),
                    new Pair<View, String>(view.findViewById(R.id.album), Constants.TRANSTITION_ALBUM));
        });
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
    }

    @Override
    public void showAlbums(List<Album> albumList) {
        mAdapter.setNewData(albumList);
    }

    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }
}
