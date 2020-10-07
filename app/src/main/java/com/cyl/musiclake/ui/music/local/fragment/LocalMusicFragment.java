package com.cyl.musiclake.ui.music.local.fragment;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseLazyFragment;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.main.PageAdapter;

import butterknife.BindView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class LocalMusicFragment extends BaseLazyFragment {
    @BindView(R.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static LocalMusicFragment newInstance(String flag) {
        Bundle args = new Bundle();
        args.putString(Extras.SONG_DB, flag);
        LocalMusicFragment fragment = new LocalMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_music;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getResources().getString(R.string.local_music));
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initInjector() {

    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
        adapter.addFragment(SongsFragment.Companion.newInstance(), getString(R.string.local_title));
        adapter.addFragment(AlbumFragment.Companion.newInstance(), getString(R.string.album_title));
        adapter.addFragment(ArtistFragment.Companion.newInstance(), getString(R.string.artist_title));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onLazyLoad() {
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
    }
}
