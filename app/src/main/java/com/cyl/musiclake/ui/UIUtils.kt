package com.cyl.musiclake.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.design.widget.TabLayout
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.RxBus
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.event.PlaylistEvent
import com.cyl.musiclake.player.playqueue.PlayQueueManager
import com.cyl.musiclake.utils.ToastUtils
import com.cyl.musiclake.view.custom.DisplayUtils
import java.lang.reflect.Field

object UIUtils {
    /**
     * 改变播放模式
     */
    fun updatePlayMode(imageView: ImageView, isChange: Boolean = false) {
        try {
            var playMode = PlayQueueManager.getPlayModeId()
            if (isChange) playMode = PlayQueueManager.updatePlayMode()
            when (playMode) {
                PlayQueueManager.PLAY_MODE_LOOP -> {
                    imageView.setImageResource(R.drawable.ic_repeat)
                    if (isChange) ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.play_mode_loop))
                }
                PlayQueueManager.PLAY_MODE_REPEAT -> {
                    imageView.setImageResource(R.drawable.ic_repeat_one)
                    if (isChange) ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.play_mode_repeat))
                }
                PlayQueueManager.PLAY_MODE_RANDOM -> {
                    imageView.setImageResource(R.drawable.ic_shuffle)
                    if (isChange) ToastUtils.show(MusicApp.getAppContext().resources.getString(R.string.play_mode_random))
                }
            }
        } catch (e: Throwable) {

        }
    }

    /**
     * 收藏歌曲
     */
    fun collectMusic(imageView: ImageView, music: Music?) {
        music?.let {
            imageView.setImageResource(if (!it.isLove) R.drawable.item_favorite_love else R.drawable.item_favorite)
        }
        ValueAnimator.ofFloat(1f, 1.3f, 0.8f, 1f).apply {
            duration = 600
            addUpdateListener {
                imageView.scaleX = it.animatedValue as Float
                imageView.scaleY = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    music?.let {
                        it.isLove = SongLoader.updateFavoriteSong(it)
                        RxBus.getInstance().post(PlaylistEvent(Constants.PLAYLIST_LOVE_ID))
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }.start()

    }


    /**
     * 反射设置tabLayout指示器宽度
     */
    fun updateTabLayout(tab: TabLayout) {
        tab.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tab.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val tabLayout = tab.javaClass
                var tabStrip: Field? = null
                try {
                    tabStrip = tabLayout.getDeclaredField("mTabStrip")
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                }

                tabStrip!!.isAccessible = true
                var ll_tab: LinearLayout? = null
                try {
                    ll_tab = tabStrip.get(tab) as LinearLayout
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                var maxLen = 0
                var maxTextSize = 0
                val tabCount = ll_tab!!.childCount
                for (i in 0 until tabCount) {
                    val child = ll_tab.getChildAt(i)
                    child.setPadding(0, 0, 0, 0)
                    if (child is ViewGroup) {
                        for (j in 0 until ll_tab.childCount) {
                            if (child.getChildAt(j) is TextView) {
                                val tabTextView = child.getChildAt(j) as TextView
                                val length = tabTextView.text.length
                                maxTextSize = if (tabTextView.textSize.toInt() > maxTextSize) tabTextView.textSize.toInt() else maxTextSize
                                maxLen = if (length > maxLen) length else maxLen
                            }
                        }

                    }
                    val margin = (tab.width / tabCount - (maxTextSize + DisplayUtils.dp2px(8f)) * maxLen) / 2 - DisplayUtils.dp2px(8f)
                    val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    params.leftMargin = margin
                    params.rightMargin = margin
                    child.layoutParams = params
                    child.invalidate()
                }
            }
        })
    }
}