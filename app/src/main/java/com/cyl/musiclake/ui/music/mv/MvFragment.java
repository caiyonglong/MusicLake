package com.cyl.musiclake.ui.music.mv;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
public class MvFragment extends BaseFragment<BasePresenter> {

    private static final String TAG = "ChartsFragment";
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewpager;


    public static MvFragment newInstance() {
        Bundle args = new Bundle();
        MvFragment fragment = new MvFragment();
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
        adapter.addFragment(MvListFragment.newInstance("personalized"), getString(R.string.personalized));
        adapter.addFragment(MvListFragment.newInstance("rank"), getString(R.string.charts));
        adapter.addFragment(MvListFragment.newInstance("recently"), getString(R.string.recent_update_title));
        adapter.addFragment(MvSearchListFragment.newInstance(), getString(R.string.search));
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(2);
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