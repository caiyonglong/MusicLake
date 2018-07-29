package com.cyl.musiclake.ui.settings;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.ui.main.WebActivity;
import com.cyl.musiclake.view.FlipperView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_URL;

/**
 * Created by lw on 2018/2/12.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.flipperView)
    FlipperView flipperView;
    @BindView(R.id.version)
    TextView mVersion;
    @BindView(R.id.logoFab)
    FloatingActionButton mLogoFab;
    ObjectAnimator animator;

    @OnClick(R.id.introduceTv)
    void introduce() {
        WebActivity.start(this, "关于软件", ABOUT_MUSIC_LAKE_URL);
    }

    @OnClick(R.id.logoFab)
    void toFlipper() {
        flipperView.setOnClick();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        animator = ObjectAnimator.ofFloat(mLogoFab, "scaleX", 1f, 1.2f, 1f);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> {
            float x = (float) animation.getAnimatedValue();
            mLogoFab.setScaleY(x);
        });
        animator.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        animator.cancel();
    }

    @Override
    protected void initData() {
        try {
            String version = MusicApp.getAppContext()
                    .getPackageManager()
                    .getPackageInfo(MusicApp.getAppContext().getPackageName(), 0).versionName;
            mVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initInjector() {

    }

}
