package com.cyl.music_hnust.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.OnlineMusicActivity;
import com.cyl.music_hnust.adapter.OnlineAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.OnlinePlaylistMusic;
import com.cyl.music_hnust.view.DividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineFragment extends BaseFragment implements OnlineAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    private OnlineAdapter mAdapter;
    private List<OnlinePlaylistMusic> onlinePlaylistMusics = new ArrayList<>();
    private FloatingActionButton mFloatingActionButton;

    private ProgressDialog mProgressDialog;
    private int mOffset = 0;

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
    @Override
    protected void initDatas() {
        String[] titles=getResources().getStringArray(R.array.online_music_list_title);
        String[] types=getResources().getStringArray(R.array.online_music_list_type);

        for(int i=0;i<titles.length;i++){
            OnlinePlaylistMusic onlinelist = new OnlinePlaylistMusic();
            onlinelist.setTitle(titles[i]);
            onlinelist.setType(types[i]);
            onlinePlaylistMusics.add(onlinelist);
        }
        mAdapter = new OnlineAdapter(getActivity(),recyclerView, onlinePlaylistMusics);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.item_divider_white));

    }

    @Override
    protected void listener() {

    }


    @Override
    public void onItemClick(View view, int position) {
        OnlinePlaylistMusic songListInfo = onlinePlaylistMusics.get(position);
        Intent intent = new Intent(getActivity(), OnlineMusicActivity.class);
        intent.putExtra("online_list_type", (Serializable) songListInfo);
        startActivity(intent);
    }
}