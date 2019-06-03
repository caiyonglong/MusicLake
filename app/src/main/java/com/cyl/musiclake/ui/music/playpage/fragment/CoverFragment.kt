package com.cyl.musiclake.ui.music.playpage.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.ui.music.dialog.QualitySelectDialog
import com.cyl.musiclake.ui.music.search.SearchActivity
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.frag_player_coverview.*
import org.jetbrains.anko.support.v4.startActivity

class CoverFragment : BaseFragment<BasePresenter<BaseContract.BaseView>>() {

    val TAG = "CoverFragment"
    val coverView by lazy { rootView?.findViewById<ImageView>(R.id.civ_cover) }

    //当前专辑图片
    var currentBitmap: Bitmap? = null

    //是否初始化，第一次进入界面不播放切换动画
    var isInitAnimator = false

    //搜索信息
    var searchInfo: String? = null

    //旋转属性动画
    private var coverAnimator: ObjectAnimator? = null
    private var objectAnimator1: ObjectAnimator? = null
    private var objectAnimator3: ObjectAnimator? = null
    private var objectAnimator2: ObjectAnimator? = null
    private var animatorSet: AnimatorSet? = null

    override fun getLayoutId(): Int {
        return R.layout.frag_player_coverview
    }

    override fun initInjector() {
    }

    override fun initViews() {
        super.initViews()
        //音效点击
        tv_sound_effect.setOnClickListener {
            activity?.let { it1 -> NavigationHelper.navigateToSoundEffect(it1) }
        }

        //音质点击
        tv_quality.setOnClickListener {
            QualitySelectDialog.newInstance(PlayManager.getPlayingMusic()).apply {
                changeSuccessListener = {
                    tv_quality.text = it
                }
                isDownload = false
            }.show(activity as AppCompatActivity)
        }

        //点击歌曲类型，直接去搜索
        tv_source.setOnClickListener {
            searchInfo?.let {
                startActivity<SearchActivity>(Extras.SEARCH_INFO to it)
            }
        }
    }

    /**
     * 更新歌曲類型
     * 更新音乐品质
     */
    fun updateMusicType(playingMusic: Music) {
        if (context == null) return
        LogUtil.d(TAG, "CoverFragment = ${playingMusic.type}")
        searchInfo = playingMusic.title + "-" + playingMusic.artist
        val value: String? = when (playingMusic?.type) {
            Constants.QQ -> {
                getString(R.string.res_qq)
            }
            Constants.BAIDU -> {
                getString(R.string.res_baidu)
            }
            Constants.NETEASE -> {
                getString(R.string.res_wangyi)
            }
            Constants.XIAMI -> {
                getString(R.string.res_xiami)
            }
            else -> {
                getString(R.string.res_local)
            }
        }
        val quality = when (playingMusic.quality) {
            128000 -> getString(R.string.sound_quality_standard)
            192000 -> getString(R.string.sound_quality_high)
            320000 -> getString(R.string.sound_quality_hq_high)
            999000 -> getString(R.string.sound_quality_sq_high)
            else -> getString(R.string.sound_quality_standard)
        }
        tv_quality?.text = quality
        value?.let {
            tv_source?.text = value
        }
    }

    /**
     * 设置Bitmap
     */
    fun setImageBitmap(bm: Bitmap?) {
        civ_cover?.setImageBitmap(bm)
        LogUtil.d(TAG, "civ_cover 设置Bitmap")
        if (currentBitmap == null) {
            LogUtil.d(TAG, "civ_cover2 设置Bitmap")
            civ_cover_2?.setImageBitmap(bm)
        }
        currentBitmap = bm
    }

    /**
     * 初始化旋转动画
     */
    fun initAlbumPic() {
        if (civ_cover == null) return

        coverAnimator = ObjectAnimator.ofFloat(civ_cover, "rotation", 0F, 359F).apply {
            duration = (20 * 1000).toLong()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
            addUpdateListener {
                //同时更新civ_cover_2
                civ_cover_2?.rotation = it.animatedValue as Float
            }
        }

        //缩放动画
        objectAnimator1 = ObjectAnimator.ofFloat(civ_cover_2, "scaleX", 1f, 0.7f).apply {
            duration = 500L
            interpolator = AccelerateInterpolator()
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                civ_cover_2?.scaleY = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    LogUtil.d("objectAnimator", "objectAnimator1 动画结束")
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    //开始时，初始化状态
                    coverView?.alpha = 0f
                    civ_cover_2?.translationY = 0f
                    civ_cover_2?.visibility = View.VISIBLE
                    LogUtil.d("objectAnimator", "objectAnimator1 动画开始")
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
        }

        //coverView 由透明变成不透明
        objectAnimator3 = ObjectAnimator.ofFloat(coverView, "alpha", 0f, 1F).apply {
            duration = 300L
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    LogUtil.d("objectAnimator", "objectAnimator2 onAnimationEnd")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    coverView?.alpha = 1f
                }

                override fun onAnimationStart(animation: Animator?) {
                    LogUtil.d("objectAnimator", "objectAnimator2 动画开始")
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
        }

        //civ_cover_2 上移动画
        objectAnimator2 = ObjectAnimator.ofFloat(civ_cover_2, "translationY", 0f, -1000f).apply {
            duration = 300L
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    civ_cover_2?.translationY = 0f
                    civ_cover_2?.setImageBitmap(currentBitmap)
                    civ_cover_2?.visibility = View.GONE
                    LogUtil.d("objectAnimator", "objectAnimator2 onAnimationEnd")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    coverView?.alpha = 1f
                }

                override fun onAnimationStart(animation: Animator?) {
                    LogUtil.d("objectAnimator", "objectAnimator2 动画开始")
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
        }

        animatorSet = AnimatorSet()
        animatorSet?.play(objectAnimator2)?.with(objectAnimator3)?.after(objectAnimator1)
    }

    /**
     * 切换歌曲，开始旋转动画
     */
    fun startRotateAnimation() {
        coverAnimator?.cancel()
        coverAnimator?.start()

        if (isInitAnimator) {
            animatorSet?.cancel()
            animatorSet?.start()
        } else {
            //第一次进入不播放切换动画
            isInitAnimator = true
            civ_cover_2?.visibility = View.GONE
        }
    }

    /**
     * 停止旋转
     */
    fun stopRotateAnimation() {
        coverAnimator?.pause()
    }

    /**
     * 继续旋转
     */
    fun resumeRotateAnimation() {
        coverAnimator?.isStarted?.let {
            if (it) coverAnimator?.resume() else coverAnimator?.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if (coverAnimator != null && coverAnimator?.isPaused!! && PlayManager.isPlaying()) {
            coverAnimator?.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coverAnimator?.cancel()
        coverAnimator = null
    }

    override fun onStop() {
        super.onStop()
        coverAnimator?.pause()
        animatorSet?.pause()
    }

}