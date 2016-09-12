package com.cyl.music_hnust.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.Music;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：yonglong on 2016/8/8 21:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class TestFragment extends BaseFragment {

    public static TestFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.recycle)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PRELOAD_SIZE = 6;

    public static final String EXTRA_CID = "extra_cid";

    public static final String EXTRA_TYPE = "extra_type";

    private boolean mIsLoadMore = true;

    private int cid;

    private int pageNum = 20;

    private int page = 1;

    private LocalMusicAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private int type;

    private List<Music> musicInfos = new ArrayList<>();

    private boolean mIsRefreshing = false;


    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public int getLayoutId()
    {

        return R.layout.frag_music;
    }

    @Override
    public void initViews()
    {
       // initRecycleView();
    }

    private void initRecycleView()
    {

        getMusic();
//        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LocalMusicAdapter(getContext(), musicInfos);
        mRecyclerView.setAdapter(mAdapter);
    }



    private void getMusic()
    {
        musicInfos.clear();
        Music music = new Music();
        music.setTitle("方圆几里");
        music.setArtist("薛之谦-方圆几里");
        musicInfos.add(music);
        music.setTitle("方圆几里2");
        music.setArtist("薛之谦-方圆几里3");
        musicInfos.add(music);
//        MusicUtils.scanMusic(getContext(), musicInfos);
        Log.e("数量===", musicInfos.size() + "sss");
    }


}
