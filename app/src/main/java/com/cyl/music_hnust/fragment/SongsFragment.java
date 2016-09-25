package com.cyl.music_hnust.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.activity.SearchActivity;
import com.cyl.music_hnust.adapter.LocalMusicAdapter;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SongsFragment extends BaseFragment implements LocalMusicAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    TextView tv_empty;

    private static LocalMusicAdapter mAdapter;
    private static List<Music> musicInfos = new ArrayList<>();

    /**
     * 新建一个线程更新UI
     */
    final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {

        }
    };

    /**
     * 设置监听事件
     */
    @Override
    protected void listener() {
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LocalMusicAdapter((AppCompatActivity) getActivity(),musicInfos);
        reloadAdapter();
        mRecyclerView.setAdapter(mAdapter);

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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_my, menu);
    }

    /**
     * 菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //recyclerview item点击事件
    @Override
    public void onItemClick(View view, int position) {
        MainActivity.mPlayService.setMyMusicList(musicInfos);
        MainActivity.mPlayService.playMusic(position);
    }

    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                musicInfos = MusicUtils.getAllSongs(getActivity());
                mAdapter.setMusicInfos(musicInfos);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (musicInfos.size() == 0) {
                    tv_empty.setText("请稍后，本地音乐加载中...");
                    tv_empty.setVisibility(View.VISIBLE);
                }else {
                    tv_empty.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
