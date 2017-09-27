package com.cyl.music_hnust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Playlist;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.view.CreatePlaylistDialog;
import com.cyl.music_hnust.view.MultiViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：yonglong on 2016/8/10 21:27
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class PlaylistFragment extends BaseFragment {

    @Bind(R.id.mul_vp)
    MultiViewPager mMultiViewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private List<Playlist> mPlaylists = new ArrayList<>();

    FragmentStatePagerAdapter adapter;
    //判断新增歌单是否存在
    static Boolean isNewPlaylist = false;


    @Override
    public int getLayoutId() {
        return R.layout.frag_playlist;
    }

    @Override
    public void initViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("我的歌单");
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {
        updateView();
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
            case R.id.action_new_playlist:
                CreatePlaylistDialog.newInstance().show(getChildFragmentManager(), "CREATE_PLAYLIST");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 异步刷新数据
     */
    public void updateView(final long id) {
        final List<Playlist> playlists = PlaylistLoader.getPlaylists(getActivity(), true);
        adapter.notifyDataSetChanged();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < playlists.size(); i++) {
                    long playlistid = Long.parseLong(playlists.get(i).getId());
                    if (playlistid == id) {
                        mMultiViewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        }, 200);

    }

    /**
     * 异步刷新数据
     */
    public void updateView() {
        mPlaylists = PlaylistLoader.getPlaylists(getActivity(), true);
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return mPlaylists.size() + 1;
            }

            @Override
            public Fragment getItem(int position) {
                if (position == mPlaylists.size()) {
                    return PlaylistPagerFragment.newInstance(mPlaylists.size());
                } else {
                    return PlaylistPagerFragment.newInstance(position);
                }
            }
        };
        mMultiViewPager.setAdapter(adapter);
        mMultiViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateView();
    }
}
