package com.cyl.musiclake.ui.music.charts.fragment;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseFragment;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.main.PageAdapter;
import com.cyl.musiclake.utils.DisplayUtils;

import java.lang.reflect.Field;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class ChartsFragment extends BaseFragment<BasePresenter> {

    private static final String TAG = "BaiduPlaylistFragment";
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewpager;

    public static ChartsFragment newInstance() {
        Bundle args = new Bundle();
        ChartsFragment fragment = new ChartsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mv;
    }

    @Override
    public void initViews() {
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
//        adapter.addFragment(ChartsDetailFragment.Companion.newInstance(Constants.BAIDU), getString(R.string.res_baidu));
//        adapter.addFragment(ChartsDetailFragment.Companion.newInstance(Constants.QQ), getString(R.string.res_qq));
//        adapter.addFragment(ChartsDetailFragment.Companion.newInstance(Constants.NETEASE), getString(R.string.res_wangyi));
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setCurrentItem(0);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void listener() {

    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

}