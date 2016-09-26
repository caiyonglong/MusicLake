package com.cyl.music_hnust.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.PlaylistDetailActivity;
import com.cyl.music_hnust.adapter.PlaylistAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.LocalPlaylist;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.MusicUtils;
import com.cyl.music_hnust.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yonglong on 2016/8/10 21:27
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    TextView tv_empty;
    EditText et_playlist;
    private GridLayoutManager mLayoutManager;
    private PlaylistAdapter mAdapter;
    private List<LocalPlaylist> localplaylists = new ArrayList<>();
    List<List<Music>> playlist;
    private FloatingActionButton mFloatingActionButton;

    //判断新增歌单是否存在
    Boolean isNewPlaylist = false;


    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        localplaylists = MusicUtils.scanPlaylist(getContext());
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_recyclerview;
    }

    @Override
    public void initViews() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        mLayoutManager = new GridLayoutManager(getActivity(),2);

        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void initData() {
        mAdapter = new PlaylistAdapter(getActivity(),localplaylists,onRecyclerItemClick);
        mRecyclerView.setAdapter(mAdapter);

    }
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_playlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_playlist:
                toAddPlaylist();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 新增歌单
     * @return
     */
    private boolean toAddPlaylist(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.playlist_new, null);


        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("新增歌单");
        dialog.setView(layout);
        et_playlist = (EditText) layout.findViewById(R.id.et_playlist);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlist = et_playlist.getText().toString();
                if (playlist.length()<2){
                    ToastUtils.show(getContext(),"歌单名长度不少于3");
                }else {
                    LocalPlaylist localPlaylist = new LocalPlaylist();
                    localPlaylist.setName(playlist);
                    localPlaylist.setId(System.currentTimeMillis()+"hk");
                    isNewPlaylist =MusicUtils.newPlaylist(getContext(),localPlaylist);
                    if (isNewPlaylist){
                        ToastUtils.show(getContext(),"操作成功");

                        localplaylists = MusicUtils.scanPlaylist(getContext());
                        mAdapter.setLocalplaylists(localplaylists);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.show(getContext(),"操作失败，歌单已存在！");
                    }
                }
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return isNewPlaylist;
    }
    private PlaylistAdapter.OnRecyclerItemClick onRecyclerItemClick = new PlaylistAdapter.OnRecyclerItemClick() {
        @Override
        public void onItemClick(View view, LocalPlaylist localPlaylist) {
            Pair squareParticipant = new Pair<>(view, ViewCompat.getTransitionName(view));
            ActivityOptions transitionActivityOptions =
                    null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(),view,"share");
                Intent intent = new Intent(
                        getActivity(), PlaylistDetailActivity.class);
                intent.putExtra(Extras.PLAYLIST_ID,localPlaylist.getId());
                intent.putExtra(Extras.ALBUM,false);
                startActivity(intent, transitionActivityOptions.toBundle());
            }else {
                Intent intent = new Intent(
                        getActivity(), PlaylistDetailActivity.class);
                intent.putExtra(Extras.PLAYLIST_ID,localPlaylist.getId());
                intent.putExtra(Extras.ALBUM,false);
                startActivity(intent);
            }
        }
    };


}
