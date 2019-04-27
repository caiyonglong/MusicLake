package com.cyl.musiclake.ui.main;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.Tools;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.wel_container)
    ConstraintLayout container;
    @BindView(R.id.iv_header_cover)
    ImageView heardCoverIv;
    RxPermissions rxPermissions;

    //需要检查的权限
    private final String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //获取电话状态
            Manifest.permission.READ_PHONE_STATE,
    };

    @Override
    protected void listener() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        rxPermissions = new RxPermissions(this);
        if (SystemUtils.isMarshmallow()) {
            checkPermissionAndThenLoad();
        } else {
            initWelcome();
        }
    }

    @Override
    protected void initInjector() {

    }

    /**
     * 检查权限
     */
    @SuppressLint("CheckResult")
    private void checkPermissionAndThenLoad() {
        rxPermissions.request(mPermissionList)
                .subscribe(granted -> {
                    if (granted) {
                        initWelcome();
                    } else {
                        Snackbar.make(container, getResources().getString(R.string.permission_hint),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getResources().getString(R.string.sure), view -> checkPermissionAndThenLoad()).show();
                    }
                });
    }

    /**
     * 检查服务是否运行
     */
    private void initWelcome() {
        boolean isFirst = SPUtils.getAnyByKey(SPUtils.SP_KEY_FIRST_COMING, true);
        if (isFirst) {
            getCoverImageUrl();
            SPUtils.putAnyCommit(SPUtils.SP_KEY_FIRST_COMING, false);
        } else {
            mHandler.postDelayed(WelcomeActivity.this::startMainActivity, 1000);
        }
    }

    /**
     * 欢迎界面跳转到主界面
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void getCoverImageUrl() {
        mHandler.postDelayed(WelcomeActivity.this::startMainActivity, 3000);
    }

}
