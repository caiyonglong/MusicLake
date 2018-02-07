package com.cyl.musiclake.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：YongLong on 2016/8/8 16:58
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public abstract class BaseFragment extends RxFragment {
    public View rootView;
    private Unbinder mUnbinder;
    private int count;//记录开启进度条的情况 只能开一个
    //当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    private boolean isFragmentVisible;
    //是否是第一次开启网络加载
    public boolean isViewCreated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initViews();
        listener();
        if (isLazyLoad()) {
            isViewCreated = true;
            lazyLoad();
        } else {
            loadData();
        }

    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }


    protected void listener() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public abstract int getLayoutId();

    public abstract void initViews();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        if (isVisibleToUser) {
            isFragmentVisible = true;
            lazyLoad();
        } else {
            isFragmentVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isFragmentVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isFragmentVisible = false;
            Log.e(getClass().getName(), "isVisible :可见加载数据");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    /**
     * 开启加载进度条
     */
    public void startProgressDialog() {
        count++;
        if (count == 1) {
        }
    }

    /**
     * 开启加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
    }

    /**
     * 停止加载进度条
     */
    public void stopProgressDialog() {
        count--;
        if (count == 0) {
        }
    }

    protected void loadData() {

    }

}
