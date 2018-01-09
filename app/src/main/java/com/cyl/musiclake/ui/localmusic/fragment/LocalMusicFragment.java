package com.cyl.musiclake.ui.localmusic.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.common.PageAdapter;
import com.cyl.musiclake.utils.ToastUtils;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by Monkey on 2015/6/29.
 */
public class LocalMusicFragment extends BaseFragment {
    @BindView(R.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static LocalMusicFragment newInstance() {
        Bundle args = new Bundle();
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
        mToolbar.setTitle("本地歌曲");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initDatas() {
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);

    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
        adapter.addFragment(SongsFragment.newInstance(), "歌曲");
        adapter.addFragment(AlbumFragment.newInstance(), "专辑");
        adapter.addFragment(ArtistFragment.newInstance(), "艺术家");
        viewPager.setAdapter(adapter);
    }
}
