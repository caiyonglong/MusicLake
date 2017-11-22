package com.cyl.musiclake.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.dataloaders.MusicLoader;
import com.cyl.musiclake.mvp.model.music.Artist;
import com.cyl.musiclake.ui.adapter.ArtistAdapter;
import com.cyl.musiclake.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class ArtistFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.loading)
    LinearLayout loading;
    private ArtistAdapter mAdapter;
    private List<Artist> artists = new ArrayList<>();


    public static ArtistFragment newInstance() {

        Bundle args = new Bundle();
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        new loadArtistTask().execute();
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
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadArtistTask().execute();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private class loadArtistTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
                artists = MusicLoader.getAllArtists(getActivity());
                mAdapter = new ArtistAdapter(getActivity(), artists);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            loading.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            tv_empty.setText("请稍后，努力加载中...");
        }
    }
}
