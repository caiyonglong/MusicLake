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
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.utils.SystemUtils;
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
            initPlayQueue();
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
                        initPlayQueue();
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
    private void initPlayQueue() {
        getCoverImageUrl();

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
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(heardCoverIv, "colorFilter",
//                getResources().getColor(R.color.app_green),
//                getResources().getColor(R.color.app_yellow),
//                getResources().getColor(R.color.app_red),
//                getResources().getColor(R.color.app_green_dark),
//                getResources().getColor(R.color.app_blue));
//        objectAnimator.setEvaluator(new ArgbEvaluator());
//
//        objectAnimator.setDuration(2000);
//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(heardCoverIv, "y", 0f, heardCoverIv.getTop());
//
//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(heardCoverIv, "scaleX", 0f, 1f);
//        objectAnimator2.addUpdateListener(valueAnimator -> {
//            float value = (float) valueAnimator.getAnimatedValue();
//            heardCoverIv.setScaleY(value);
//            heardCoverIv.setRotation(value);
//        });
//        objectAnimator2.setDuration(2000);
//        objectAnimator1.setDuration(2000);
//
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(objectAnimator)
//                .with(objectAnimator2)
//                .with(objectAnimator1);
//        animatorSet.start();

    }

}
