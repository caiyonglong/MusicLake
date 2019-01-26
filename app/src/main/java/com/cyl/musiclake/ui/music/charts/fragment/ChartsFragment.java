package com.cyl.musiclake.ui.music.charts.fragment;

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
        updateTabLayout(mTabLayout);
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

    public void updateTabLayout(TabLayout tab) {
        tab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Class<?> tabLayout = tab.getClass();
                Field tabStrip = null;
                try {
                    tabStrip = tabLayout.getDeclaredField("mTabStrip");
                    tabStrip.setAccessible(true);
                    LinearLayout ll_tab = null;
                    ll_tab = (LinearLayout) tabStrip.get(tab);
                    int maxLen = 0;
                    int maxTextSize = 0;
                    int tabCount = ll_tab.getChildCount();
                    for (int i = 0; i < tabCount; i++) {
                        View child = ll_tab.getChildAt(i);
                        child.setPadding(0, 0, 0, 0);
                        if (child instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) child;
                            for (int j = 0; j < ll_tab.getChildCount(); j++) {
                                if (viewGroup.getChildAt(j) instanceof TextView) {
                                    TextView tabTextView = (TextView) viewGroup.getChildAt(j);
                                    int length = tabTextView.getText().length();
                                    maxTextSize = (int) tabTextView.getTextSize() > maxTextSize ? (int) tabTextView.getTextSize() : maxTextSize;
                                    maxLen = length > maxLen ? length : maxLen;
                                }
                            }

                        }

                        int margin = (tab.getWidth() / tabCount - (maxTextSize + DisplayUtils.dp2px(2)) * maxLen) / 2 - DisplayUtils.dp2px(2);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        child.setLayoutParams(params);
                        child.invalidate();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}