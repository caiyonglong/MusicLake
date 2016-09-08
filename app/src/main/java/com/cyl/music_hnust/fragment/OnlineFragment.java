package com.cyl.music_hnust.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.OnlineMusicActivity;
import com.cyl.music_hnust.adapter.OnlineAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.OnlinePlaylist;
import com.cyl.music_hnust.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class OnlineFragment extends BaseFragment implements OnlineAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    //适配器
    private OnlineAdapter mAdapter;
    //排行榜集合
    private List<OnlinePlaylist> mPlaylists = new ArrayList<>();
    private TextView tv_empty;

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
    @Override
    protected void initDatas() {
//        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
//            tv_empty.setVisibility(View.VISIBLE);
//            tv_empty.setText("网络连接异常\\(^o^)/~");
//            return;
//        }
//        mPlaylists = getmPlayService().mSongLists;
        if (mPlaylists.isEmpty()) {
            String[] titles=getResources().getStringArray(R.array.online_music_list_title);
            String[] types=getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < titles.length; i++) {
                OnlinePlaylist info = new OnlinePlaylist();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                mPlaylists.add(info);
            }
        }
        //适配器
        mAdapter = new OnlineAdapter(getActivity(),recyclerView, mPlaylists);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.item_divider_white));

    }

    @Override
    protected void listener() {

    }


    @Override
    public void onItemClick(View view, int position) {
//        ToastUtil.show(getContext(),"===="+position);
        OnlinePlaylist songListInfo = mPlaylists.get(position);
        Intent intent = new Intent(getActivity(), OnlineMusicActivity.class);
        intent.putExtra("online_list_type", songListInfo);
        startActivity(intent);
    }
}