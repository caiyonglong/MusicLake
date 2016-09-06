package com.cyl.music_hnust.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.PlaylistDetail;
import com.cyl.music_hnust.adapter.PlaylistAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:27
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistFragment extends BaseFragment {

    RecyclerView recyclerView;
    TextView tv_empty;
    private GridLayoutManager mLayoutManager;
    private PlaylistAdapter mAdapter;
    private List<Music> musicInfos = new ArrayList<>();
    private FloatingActionButton mFloatingActionButton;


    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        mLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(mLayoutManager);
        initData();
    }

    private void initData() {

        mAdapter = new PlaylistAdapter(getActivity(),recyclerView,musicInfos,onRecyclerItemClick);
        recyclerView.setAdapter(mAdapter);
        if (musicInfos.size()==0){
            tv_empty.setVisibility(View.VISIBLE);
            tv_empty.setText("暂无歌单，请新建歌单！\n点击右上角菜单\\(^o^)/~");
        }else {
            tv_empty.setVisibility(View.GONE);
        }

    }
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_my, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private PlaylistAdapter.OnRecyclerItemClick onRecyclerItemClick = new PlaylistAdapter.OnRecyclerItemClick() {

        @Override
        public void onItemClick(View view) {
            Pair squareParticipant = new Pair<>(view, ViewCompat.getTransitionName(view));
            ActivityOptions transitionActivityOptions =
                    null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(),view,"share");
                Intent intent = new Intent(
                        getActivity(), PlaylistDetail.class);
                startActivity(intent, transitionActivityOptions.toBundle());
            }else {
                Intent intent = new Intent(
                        getActivity(), PlaylistDetail.class);
                startActivity(intent);
            }

        }
    };


}
