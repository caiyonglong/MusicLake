package com.cyl.musiclake.ui.settings;

import android.animation.Animator;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cyl.musiclake.BuildConfig;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.bean.SocketOnlineEvent;
import com.cyl.musiclake.utils.Tools;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_ISSUES;
import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_PC;
import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_URL;

/**
 * Created by lw on 2018/2/12.
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_about_version)
    TextView mVersion;
    @BindView(R.id.cardEmailView)
    View cardEmailView;
    @BindView(R.id.shareFab)
    FloatingActionButton shareFab;
    @BindView(R.id.realTimeUserTv)
    TextView mRealTimeUserTv;
    @BindView(R.id.aboutContainerView)
    View mView;

    @OnClick(R.id.cardGithubView)
    void introduce() {
        Tools.INSTANCE.openBrowser(this, ABOUT_MUSIC_LAKE_URL);
    }

    @OnClick(R.id.onlineUserView)
    void toFlipper() {
    }

    @OnClick(R.id.cardEmailView)
    void toFeedback() {
        Tools.INSTANCE.openBrowser(this, ABOUT_MUSIC_LAKE_ISSUES);
    }

    @OnClick(R.id.cardPCView)
    void toPc() {
        Tools.INSTANCE.openBrowser(this, ABOUT_MUSIC_LAKE_PC);
    }

    @OnClick(R.id.shareFab)
    void toShare() {
        revealRed();
    }

    @OnClick(R.id.cardUpdateView)
    void toUpdate() {
        Beta.checkUpgrade();
    }

    @OnClick(R.id.email_feedback)
    void toEmailFeedback() {
        Tools.INSTANCE.feeback(this);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        mView.startAnimation(animation1);
    }

    @Override
    protected void initData() {
        mVersion.setText(getString(R.string.about_version,BuildConfig.VERSION_NAME));
        mRealTimeUserTv.setText(String.valueOf(MusicApp.socketManager.getRealUsersNum()));
    }

    @Override
    protected void initInjector() {

    }

    private void revealRed() {
        // 保存最开始的状态的参数
        final ViewGroup.LayoutParams saveParams = shareFab.getLayoutParams();
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition
                .changebounds_with_arcmotion);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }


            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealColor(mView.findViewById(R.id.ll_layout), R.color.colorAccent);
                // 动画结束之后，将 红圈再设回以前的参数
                shareFab.setLayoutParams(saveParams);
                Tools.INSTANCE.share(AboutActivity.this);
            }


            @Override
            public void onTransitionCancel(Transition transition) {

            }


            @Override
            public void onTransitionPause(Transition transition) {

            }


            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        // 保存 每个 View 当前的可见状态(Visibility)。
        TransitionManager.beginDelayedTransition(mView.findViewById(R.id.ll_layout), transition);
    }

    private void animateRevealColor(ViewGroup viewRoot, @ColorRes int color) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy);
    }


    private void animateRevealColorFromCoordinates(ViewGroup viewRoot, @ColorRes int color, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color));
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeEvent(SocketOnlineEvent event) {
        mRealTimeUserTv.setText(String.valueOf(event.getNum()));
    }

}
