package com.cyl.musiclake.ui.music.online.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musicapi.BaseApiImpl;
import com.cyl.musicapi.bean.ListItem;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.MusicUtils;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.ui.music.online.activity.NeteasePlaylistActivity;
import com.cyl.musiclake.ui.music.online.adapter.OnlineAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class NeteasePlaylistFragment extends BaseFragment {

    private static final String TAG = "NeteasePlaylistFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    //适配器
    private OnlineAdapter mAdapter;
    List<Playlist> neteaseLists = new ArrayList<>();

    private BaseApiImpl baseApi;

    private String[] charts = new String[]{"云音乐新歌榜"
            , "云音乐热歌榜"
            , "网易原创歌曲榜"
            , "云音乐飙升榜"
            , "云音乐电音榜"
            , "UK排行榜周榜"
            , "美国Billboard周榜 "
            , "KTV嗨榜 "
            , "iTunes榜 "
            , "Hit FM Top榜 "
            , "日本Oricon周榜 "
            , "韩国Melon排行榜周榜 "
            , "韩国Mnet排行榜周榜 "
            , "韩国Melon原声周榜 "
            , "中国TOP排行榜(港台榜) "
            , "中国TOP排行榜(内地榜)"
            , "香港电台中文歌曲龙虎榜 "
            , "华语金曲榜"
            , "中国嘻哈榜"
            , "法国 NRJ EuroHot 30 周榜"
            , "台湾Hito排行榜 "
            , "Beatport全球电子舞曲榜"};

    {
        for (int i = 0; i < charts.length; i++) {
            neteaseLists.add(new Playlist(String.valueOf(i), charts[i]));
        }
    }

    public static NeteasePlaylistFragment newInstance() {
        Bundle args = new Bundle();

        NeteasePlaylistFragment fragment = new NeteasePlaylistFragment();
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
        mAdapter = new OnlineAdapter(neteaseLists);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void loadData() {
        for (int i = 0; i < charts.length; i++) {
            int finalI = i;
            BaseApiImpl.Companion.getInstance(getActivity()).getTopList(String.valueOf(i), topList -> {
                Playlist playlist = new Playlist();
                playlist.setId(String.valueOf(finalI));
                playlist.setName(topList.getData().getName());
                playlist.setCount(topList.getData().getPlayCount());
                playlist.setCoverUrl(topList.getData().getCover());
                playlist.setDes(topList.getData().getDescription());
                List<Music> musicList = new ArrayList<>();
                if (topList.getData().getList().size() > 0) {
                    for (ListItem item : topList.getData().getList()) {
                        Music music = MusicUtils.INSTANCE.getMusic(item);
                        musicList.add(music);
                    }
                }
                playlist.setMusicList(musicList);
                neteaseLists.set(finalI, playlist);
                mAdapter.notifyItemChanged(finalI);
                return null;
            });


        }

    }

    @Override
    protected void listener() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), NeteasePlaylistActivity.class);
            intent.putExtra("title", neteaseLists.get(position).getName());
            intent.putExtra("id", neteaseLists.get(position).getId());
            startActivity(intent);
        });
    }

}