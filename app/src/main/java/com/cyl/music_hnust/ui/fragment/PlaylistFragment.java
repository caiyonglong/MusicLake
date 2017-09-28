package com.cyl.music_hnust.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Playlist;
import com.cyl.music_hnust.ui.adapter.MyViewPagerAdapter;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.utils.ToastUtils;
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
public class PlaylistFragment extends BaseFragment implements CreatePlaylistDialog.InputListener {


    private static final String TAG = "PlaylistFragment";
    private static String TAG_CREATE = "create_playlist";

    @Bind(R.id.mul_vp)
    MultiViewPager mMultiViewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private static List<Playlist> mPlaylists = new ArrayList<>();
    private static List<Fragment> fragments = new ArrayList<>();

    static MyViewPagerAdapter mAdapter;
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
                CreatePlaylistDialog dialog = CreatePlaylistDialog.newInstance();
                dialog.setInputListener(this);
                dialog.show(getChildFragmentManager(), TAG_CREATE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 异步刷新数据
     */
    public void updateView(final long id) {
        final List<Playlist> playlists = PlaylistLoader.getPlaylist(getActivity());
        mAdapter.notifyDataSetChanged();
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
        mPlaylists = PlaylistLoader.getPlaylist(getActivity());
        fragments.clear();
        for (int i = 0; i < mPlaylists.size(); i++) {
            fragments.add(PlaylistPagerFragment.newInstance(i));
        }
        mAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
        mMultiViewPager.setAdapter(mAdapter);
        mMultiViewPager.setOffscreenPageLimit(3);
        mMultiViewPager.setCurrentItem(mPlaylists.size() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onInputResult(String title) {
        Log.e(TAG, "1111111" + title);
        long mId = PlaylistLoader.createPlaylist(getActivity(), title);
        if (mId != -1) {
            updateView();
            ToastUtils.show(getActivity(), "创建歌单成功");
        } else {
            ToastUtils.show(getActivity(), "歌单已存在");
        }
    }

}
