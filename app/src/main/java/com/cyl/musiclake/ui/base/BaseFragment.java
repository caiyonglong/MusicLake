package com.cyl.musiclake.ui.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.di.component.DaggerFragmentComponent;
import com.cyl.musiclake.di.component.FragmentComponent;
import com.cyl.musiclake.di.module.FragmentModule;
import com.cyl.musiclake.utils.LogUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.cyl.musiclake.utils.AnimationUtils.animateView;

/**
 * 作者：YongLong on 2016/8/8 16:58
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends RxFragment implements BaseContract.BaseView {
    @Nullable
    @Inject
    protected T mPresenter;
    protected FragmentComponent mFragmentComponent;
    public View rootView;
    private Unbinder unbinder;
    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @Nullable
    @BindView(R.id.empty_state_view)
    public View emptyStateView;
    @Nullable
    @BindView(R.id.error_panel)
    public View errorPanelRoot;
    @Nullable
    @BindView(R.id.error_button_retry)
    public Button errorButtonRetry;
    @Nullable
    @BindView(R.id.tv_empty)
    public TextView emptyTextView;
    @Nullable
    @BindView(R.id.error_message_view)
    public TextView errorTextView;
    @Nullable
    @BindView(R.id.loading_progress_bar)
    public ProgressBar loadingProgressBar;

    @Nullable
    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentComponent();
        initInjector();
        attachView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = super.onCreateView(inflater, container, savedInstanceState)
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }


    private void initToolBar() {
        if (getActivity() != null && mToolbar != null) {
            mToolbar.setTitle(getToolBarTitle());
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
//            mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#FFFFFF"));
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                LogUtil.d("下拉刷新");
                new Handler().postDelayed(() -> {
                    if (mSwipeRefreshLayout != null)
                        mSwipeRefreshLayout.setRefreshing(false);
                    loadData();
                }, 2000);
            });
        }
    }


    protected void listener() {

    }

    protected void retryLoading() {
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
        detachView();
    }

    public abstract int getLayoutId();

    protected void initViews() {
    }

    protected void initInjector() {

    }

    protected void loadData() {

    }

    protected String getToolBarTitle() {
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initToolBar();
        initViews();
        loadData();
        listener();
    }


    /**
     * 初始化FragmentComponent
     */
    private void initFragmentComponent() {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(MusicApp.getInstance().getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    /**
     * 贴上view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(true);
        if (emptyStateView != null) animateView(emptyStateView, false, 150);
        if (loadingProgressBar != null) animateView(loadingProgressBar, true, 400);
        if (errorPanelRoot != null) animateView(errorPanelRoot, false, 150);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
        if (emptyStateView != null) animateView(emptyStateView, false, 150);
        if (loadingProgressBar != null) animateView(loadingProgressBar, false, 0);
        if (errorPanelRoot != null) animateView(errorPanelRoot, false, 150);
    }

    @Override
    public void showEmptyState() {
        if (emptyStateView != null) animateView(emptyStateView, true, 200);
        if (loadingProgressBar != null) animateView(loadingProgressBar, false, 0);
        if (errorPanelRoot != null) animateView(errorPanelRoot, false, 150);
    }

    @Override
    public void showError(String message, boolean showRetryButton) {
        hideLoading();
        if (errorTextView != null)
            errorTextView.setText(message);
        if (errorButtonRetry != null) {
            errorButtonRetry.setOnClickListener(v -> retryLoading());
            if (showRetryButton) animateView(errorButtonRetry, true, 600);
            else animateView(errorButtonRetry, false, 0);
        }
        if (errorPanelRoot != null) animateView(errorPanelRoot, true, 300);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }
}
