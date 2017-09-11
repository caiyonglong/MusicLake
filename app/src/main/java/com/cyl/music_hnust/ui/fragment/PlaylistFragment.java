package com.cyl.music_hnust.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.music.Playlist;
import com.cyl.music_hnust.view.CreatePlaylistDialog;
import com.cyl.music_hnust.view.MultiViewPager;

import java.util.ArrayList;
import java.util.List;

import static com.cyl.music_hnust.R.id.playlistpager;

/**
 * 作者：yonglong on 2016/8/10 21:27
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistFragment extends BaseFragment {

    int playlistcount;
    FragmentStatePagerAdapter adapter;
    MultiViewPager pager;
    Toolbar toolbar;
    RecyclerView mRecyclerView;
    static Context context;
    private static List<Playlist> localplaylists = new ArrayList<>();

    //判断新增歌单是否存在
    static Boolean isNewPlaylist = false;


    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("我的歌单");

        final List<Playlist> playlists = PlaylistLoader.getPlaylists(getActivity(), true);
        playlistcount = playlists.size();
//        if (playlistcount == 0) {
//            tv_empty.setVisibility(View.VISIBLE);
//        } else {
//            tv_empty.setVisibility(View.GONE);
//        }

        pager = (MultiViewPager) rootView.findViewById(playlistpager);

        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return playlistcount + 1;
            }

            @Override
            public Fragment getItem(int position) {
                if (position==playlistcount){
                    return PlaylistPagerFragment.newInstance(playlistcount);
                }else {
                    return PlaylistPagerFragment.newInstance(position);
                }
            }
        };
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_playlist;
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        pager = (MultiViewPager) rootView.findViewById(playlistpager);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
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
            case R.id.action_new_playlist:
                CreatePlaylistDialog.newInstance().show(getChildFragmentManager(), "CREATE_PLAYLIST");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 异步刷新数据
     */
    public void updatePlaylists(final long id) {
        final List<Playlist> playlists = PlaylistLoader.getPlaylists(getActivity(), true);
        playlistcount = playlists.size();
        adapter.notifyDataSetChanged();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < playlists.size(); i++) {
                    long playlistid = Long.parseLong(playlists.get(i).getId());
                    if (playlistid == id) {
                        pager.setCurrentItem(i);
                        break;
                    }
                }
            }
        }, 200);

    }

    /**
     * 异步刷新数据
     */
    public void updatePlaylists() {
        final List<Playlist> playlists = PlaylistLoader.getPlaylists(getActivity(), true);
        playlistcount = playlists.size();
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return playlistcount + 1;
            }

            @Override
            public Fragment getItem(int position) {
                if (position==playlistcount){
                    return PlaylistPagerFragment.newInstance(playlistcount);
                }else {
                    return PlaylistPagerFragment.newInstance(position);
                }
            }

        };
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

    }

}
