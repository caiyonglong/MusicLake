package com.cyl.musiclake.ui.music.discover;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.bean.Artist;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.common.NavigationHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 邮箱：643872807@qq.com
 * 版本：4.1.3
 */
public class AllListFragment extends BaseFragment {

    private static final String TAG = "ChartsDetailFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    //适配器
    private ArtistListAdapter mArtistAdapter;
    private PlaylistAdapter mPlaylistAdapter;

    private String type;
    private List<Artist> artistList = new ArrayList<>();
    private List<Playlist> playlistList = new ArrayList<>();

    @Override
    protected String getToolBarTitle() {
        type = getArguments().getString(Extras.PLAYLIST_TYPE, Constants.NETEASE_ARITIST_LIST);
        artistList = getArguments().getParcelableArrayList(Extras.ARTIST);
        playlistList = getArguments().getParcelableArrayList(Extras.PLAYLIST);
        if (type.equals(Constants.NETEASE_ARITIST_LIST))
            return getString(R.string.hot_artist);
        else
            return getString(R.string.radio);
    }

    public static AllListFragment newInstance(String type, List<Artist> artists,List<Playlist> playlists) {
        Bundle args = new Bundle();
        args.putString(Extras.PLAYLIST_TYPE, type);
        args.putParcelableArrayList(Extras.ARTIST, (ArrayList<? extends Parcelable>) artists);
        args.putParcelableArrayList(Extras.PLAYLIST, (ArrayList<? extends Parcelable>) playlists);
        AllListFragment fragment = new AllListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void initViews() {
        type = getArguments().getString(Extras.PLAYLIST_TYPE, Constants.NETEASE_ARITIST_LIST);
        artistList = getArguments().getParcelableArrayList(Extras.ARTIST);
        playlistList = getArguments().getParcelableArrayList(Extras.PLAYLIST);
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void loadData() {
        if (type.equals(Constants.NETEASE_ARITIST_LIST)) {
            //适配器
            mArtistAdapter = new ArtistListAdapter(artistList);
            mRecyclerView.setAdapter(mArtistAdapter);
            mArtistAdapter.bindToRecyclerView(mRecyclerView);

            mArtistAdapter.setOnItemClickListener((adapter, view, position) -> {
                Artist artist = (Artist) adapter.getData().get(position);
                NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), artist, new Pair<View, String>(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)));
            });
        } else if (type.equals(Constants.BAIDU_RADIO_LIST)) {
            //适配器
            mPlaylistAdapter = new PlaylistAdapter(playlistList);
            mRecyclerView.setAdapter(mArtistAdapter);
            mPlaylistAdapter.bindToRecyclerView(mRecyclerView);

            mPlaylistAdapter.setOnItemClickListener((adapter, view, position) -> {
                NavigationHelper.INSTANCE.navigateToPlaylist(mFragmentComponent.getActivity(), playlistList.get(position), null);
            });
        }
    }

    @Override
    protected void listener() {
    }
}