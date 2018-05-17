package com.cyl.musiclake.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.cyl.musiclake.utils.LogUtil;

/**
 * 作者：YongLong on 2016/8/8 16:58
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public abstract class BaseLazyFragment extends BaseFragment {
    private boolean isLazyLoaded;//懒加载过
    private boolean isPrepared;
    private int count;//记录开启进度条的情况 只能开一个

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        //只有Fragment onCreateView好了，
        //另外这里调用一次lazyLoad(）
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.d("TAG", getClass().getName() + " setUserVisibleHint() --> isVisibleToUser = " + isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoad();
        }
    }

    /**
     * 调用懒加载
     */

    private void lazyLoad() {
        // 用户可见Fragment && 没有加载过数据 && 视图已经准备完毕
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    @UiThread
    public abstract void onLazyLoad();

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

}
