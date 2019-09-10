package com.cyl.musiclake.ui.settings

import android.graphics.BitmapFactory
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import com.cyl.musiclake.BuildConfig
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.SocketOnlineEvent
import com.cyl.musiclake.common.Constants.*
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.utils.ImageUtils
import com.cyl.musiclake.utils.ToastUtils
import com.cyl.musiclake.utils.Tools
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.card_about_1.*
import kotlinx.android.synthetic.main.card_about_2.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by lw on 2018/2/12.
 */
class AboutActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    override fun getLayoutResID(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show)
        aboutContainerView?.startAnimation(animation1)
    }

    override fun initData() {
        tv_about_version?.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)
//        realTimeUserTv?.text = MusicApp.socketManager.realUsersNum.toString()
    }

    override fun initInjector() {

    }

    /**
     * 打开PC端的GitHub主页
     */
    fun toGithubIssue(view: View) {
        Tools.openBrowser(this, ABOUT_MUSIC_LAKE_ISSUES)
    }


    /**
     * 打开PC端的GitHub主页
     */
    fun toGithubOfPc(view: View) {
        Tools.openBrowser(this, ABOUT_MUSIC_LAKE_PC)
    }

    /**
     * 打开GitHub主页
     */
    fun toGithubPage(view: View) {
        Tools.openBrowser(this, ABOUT_MUSIC_LAKE_URL)
    }

    /**
     * 检查更新
     */
    fun onCheckUpdate(view: View) {
        Beta.checkUpgrade()
    }

    /**
     * 打开GitHub主页
     */
    fun toEmailFeedBack(view: View) {
        Tools.feeback(this)
    }

    /**
     * 打开GitHub主页
     */
    fun shareApp(view: View) {
        revealRed()
    }


    /**
     * 打开GitHub主页
     */
    fun donateWeChat(view: View) {
        doAsync {
            val success = ImageUtils.saveImageToGallery(context, BitmapFactory.decodeResource(resources, R.drawable.donate_wechat), "donate_wechat")
            uiThread {
                if (success) {
                    ToastUtils.show("二维码保存成功，扫码支持一下哦")
                }
            }
        }
    }


    /**
     * 打开GitHub主页
     */
    fun donateAliPay(view: View) {
        doAsync {
            val success = ImageUtils.saveImageToGallery(context, BitmapFactory.decodeResource(resources, R.drawable.donate_alipay), "donate_alipay")
            uiThread {
                if (success) {
                    ToastUtils.show("二维码保存成功，扫码支持一下哦")
                }
            }
        }
    }

    /**
     * 扩散动画
     */
    private fun revealRed() {
        // 保存最开始的状态的参数
        val saveParams = shareFab!!.layoutParams
        val transition = TransitionInflater.from(this).inflateTransition(R.transition
                .changebounds_with_arcmotion)
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

            }


            override fun onTransitionEnd(transition: Transition) {
                animateRevealColor(aboutContainerView.findViewById(R.id.ll_layout), R.color.colorAccent)
                // 动画结束之后，将 红圈再设回以前的参数
                shareFab!!.layoutParams = saveParams
                Tools.share(this@AboutActivity)
            }


            override fun onTransitionCancel(transition: Transition) {

            }


            override fun onTransitionPause(transition: Transition) {

            }


            override fun onTransitionResume(transition: Transition) {

            }
        })
        // 保存 每个 View 当前的可见状态(Visibility)。
        TransitionManager.beginDelayedTransition(aboutContainerView.findViewById(R.id.ll_layout), transition)
    }

    private fun animateRevealColor(viewRoot: ViewGroup, @ColorRes color: Int) {
        val cx = (viewRoot.left + viewRoot.right) / 2
        val cy = (viewRoot.top + viewRoot.bottom) / 2
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy)
    }


    private fun animateRevealColorFromCoordinates(viewRoot: ViewGroup, @ColorRes color: Int, x: Int, y: Int) {
        val finalRadius = Math.hypot(viewRoot.width.toDouble(), viewRoot.height.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0f, finalRadius)
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color))
        anim.duration = 1000
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.start()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRealTimeEvent(event: SocketOnlineEvent) {
        realTimeUserTv!!.text = event.num.toString()
    }

}
