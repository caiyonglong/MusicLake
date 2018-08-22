package com.cyl.musiclake.ui.settings;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cyl.musiclake.BuildConfig;
import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.utils.Tools;
import com.cyl.musiclake.view.FlipperView;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_URL;

/**
 * Created by lw on 2018/2/12.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.flipperView)
    FlipperView flipperView;
    @BindView(R.id.tv_about_version)
    TextView mVersion;
    @BindView(R.id.cardEmailView)
    View cardEmailView;
    @BindView(R.id.logoFab)
    FloatingActionButton mLogoFab;
    @BindView(R.id.aboutContainerView)
    View mView;
    ObjectAnimator animator;

    @OnClick(R.id.cardGithubView)
    void introduce() {
        Tools.INSTANCE.openBrowser(this, ABOUT_MUSIC_LAKE_URL);
    }

    @OnClick(R.id.logoFab)
    void toFlipper() {
        flipperView.setOnClick();
    }

    @OnClick(R.id.cardEmailView)
    void toFeedback() {
        Tools.INSTANCE.feeback(this);
    }

    @OnClick(R.id.shareFab)
    void toShare() {
        Tools.INSTANCE.share(this);
    }

    @OnClick(R.id.cardUpdateView)
    void toUpdate() {
        Beta.checkUpgrade();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        mView.startAnimation(animation1);

        animator = ObjectAnimator.ofFloat(mLogoFab, "scaleX", 1.1f, 0.9f);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setDuration(800);
        animator.addUpdateListener(animation -> {
            float x = (float) animation.getAnimatedValue();
            mLogoFab.setScaleY(x);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                flipperView.setOnClick();
            }
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
        mVersion.setText(String.format("版本号v%s", BuildConfig.VERSION_NAME));
    }

    @Override
    protected void initInjector() {

    }

}
