package com.cyl.musiclake.ui.widget.lyric

import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.player.UnLockNotify
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import com.rtugeek.android.colorseekbar.ColorSeekBar
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import net.steamcrafted.materialiconlib.MaterialIconView

/**
 * 桌面歌词View
 */
class FloatLyricView(context: Context) : FrameLayout(context), View.OnClickListener {

    /**
     * 记录小悬浮窗的宽度
     */
    var viewWidth: Int = 0

    /**
     * 记录小悬浮窗的高度
     */
    var viewHeight: Int = 0

    /**
     * 用于更新小悬浮窗的位置
     */
    private val windowManager: WindowManager

    /**
     * 小悬浮窗的参数
     */
    private var mParams: WindowManager.LayoutParams? = null

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private var xInScreen: Float = 0.toFloat()

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private var yInScreen: Float = 0.toFloat()

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private var xDownInScreen: Float = 0.toFloat()

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private var yDownInScreen: Float = 0.toFloat()

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private var xInView: Float = 0.toFloat()

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private var yInView: Float = 0.toFloat()
    private val mFontSize: Float
    private val mFontColor: Int
    private var mMovement: Boolean = false
    private var isHiddenSettings: Boolean = false


    private val mRootView by lazy { LayoutInflater.from(context).inflate(R.layout.float_lyric_view, this) }
    private val mPreButton by lazy { mRootView?.findViewById<MaterialIconView>(R.id.btn_previous) }
    private val mPlayButton by lazy { mRootView?.findViewById<MaterialIconView>(R.id.btn_play) }
    private val mNextButton by lazy { mRootView?.findViewById<MaterialIconView>(R.id.btn_next) }
    private val mLockButton by lazy { mRootView?.findViewById<MaterialIconView>(R.id.btn_lock) }
    private val mSettingsButton by lazy { mRootView?.findViewById<MaterialIconView>(R.id.btn_settings) }
    private val mFrameBackground by lazy { mRootView?.findViewById<View>(R.id.small_bg) }
    private val mSettingLinearLayout by lazy { mRootView?.findViewById<View>(R.id.ll_settings) }
    private val mRelLyricView by lazy { mRootView?.findViewById<View>(R.id.rl_layout) }
    private val mLinLyricView by lazy { mRootView?.findViewById<View>(R.id.ll_layout) }

    private val mMusicButton by lazy { mRootView?.findViewById<ImageButton>(R.id.music_app) }
    private val mCloseButton by lazy { mRootView?.findViewById<ImageButton>(R.id.btn_close) }
    val mLyricText by lazy { mRootView?.findViewById<LyricTextView>(R.id.lyric) }
    val mTitle by lazy { mRootView?.findViewById<TextView>(R.id.music_title) }
    private val mSizeSeekBar by lazy { mRootView?.findViewById<SeekBar>(R.id.sb_size) }
    private val mColorSeekBar by lazy { mRootView?.findViewById<ColorSeekBar>(R.id.sb_color) }
    private val view by lazy { mRootView?.findViewById<FrameLayout>(R.id.small_window_layout) }

    private var mIsLock: Boolean = false
    private val mNotify: UnLockNotify

    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mNotify = UnLockNotify()

        viewWidth = view?.layoutParams?.width ?: 0
        viewHeight = view?.layoutParams?.height ?: 0
        mMovement = true
        isHiddenSettings = true


        mCloseButton?.setOnClickListener(this)
        mMusicButton?.setOnClickListener(this)
        mLockButton?.setOnClickListener(this)
        mPreButton?.setOnClickListener(this)
        mPlayButton?.setOnClickListener(this)
        mNextButton?.setOnClickListener(this)
        mSettingsButton?.setOnClickListener(this)

        mFontSize = SPUtils.getFontSize().toFloat()
        mLyricText?.setFontSizeScale(mFontSize)
        mSizeSeekBar?.progress = mFontSize.toInt()

        mIsLock = SPUtils.getAnyByKey(SPUtils.SP_KEY_FLOAT_LYRIC_LOCK, false)

        saveLock(mIsLock, false)

        mFontColor = SPUtils.getFontColor()
        mLyricText?.setFontColorScale(mFontColor)
        mColorSeekBar?.colorBarPosition = mFontColor

        setPlayStatus(MusicPlayerService.getInstance().isPlaying)

        mSizeSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                LogUtil.e("TEST", "$progress---$fromUser")
                mLyricText?.setFontSizeScale(progress.toFloat())
                SPUtils.saveFontSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        mColorSeekBar?.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            mLyricText?.setFontColorScale(color)
            SPUtils.saveFontColor(color)
        }

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.x
                yInView = event.y
                xDownInScreen = event.rawX
                yDownInScreen = event.rawY - getStatusBarHeight()
                xInScreen = event.rawX
                yInScreen = event.rawY - getStatusBarHeight()
            }
            MotionEvent.ACTION_MOVE -> {
                xInScreen = event.rawX
                yInScreen = event.rawY - getStatusBarHeight()
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition()
            }
            MotionEvent.ACTION_UP ->
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen && mMovement) {
                    showLyricBackground()
                }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    fun setParams(params: WindowManager.LayoutParams) {
        mParams = params
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private fun updateViewPosition() {
        if (!mMovement) return
        mParams!!.x = (xInScreen - xInView).toInt()
        mParams!!.y = (yInScreen - yInView).toInt()
        windowManager.updateViewLayout(this, mParams)
    }

    /**
     * 显示隐藏背景
     */
    private fun showLyricBackground() {
        LogUtil.d("FloatLyricView", "桌面歌词状态：mIsLock:$mIsLock")
        if (mRootView != null) {
            if (!mIsLock) {
                mRelLyricView?.visibility = View.VISIBLE
                mLinLyricView?.visibility = View.VISIBLE
                mFrameBackground?.visibility = View.VISIBLE
            } else {
                if (!isHiddenSettings) {
                    isHiddenSettings = true
                    updateSettingStatus(isHiddenSettings)
                }
                mLinLyricView?.visibility = View.INVISIBLE
                mRelLyricView?.visibility = View.INVISIBLE
                mFrameBackground?.visibility = View.INVISIBLE
            }
        }
    }


    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private fun getStatusBarHeight(): Int {
        if (statusBarHeight == 0) {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val o = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = field.get(o) as Int
                statusBarHeight = resources.getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return statusBarHeight
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.music_app -> {
                val intent = NavigationHelper.getNowPlayingIntent(context)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            R.id.btn_close -> MusicPlayerService.getInstance().showDesktopLyric(false)
            R.id.btn_lock -> {
                mMovement = !mMovement
                if (mMovement) {
                    mLockButton?.setIcon(MaterialDrawableBuilder.IconValue.LOCK_OPEN)
                } else {
                    saveLock(true, true)
                }
            }
            R.id.btn_previous -> MusicPlayerService.getInstance().prev()
            R.id.btn_play -> {
                MusicPlayerService.getInstance().playPause()
                setPlayStatus(MusicPlayerService.getInstance().isPlaying)
            }
            R.id.btn_next -> MusicPlayerService.getInstance().next(false)
            R.id.btn_settings -> {
                isHiddenSettings = !isHiddenSettings
                updateSettingStatus(isHiddenSettings)
            }
        }
    }

    fun setPlayStatus(isPlaying: Boolean) {
        if (isPlaying) {
            mPlayButton?.setIcon(MaterialDrawableBuilder.IconValue.PAUSE)
        } else {
            mPlayButton?.setIcon(MaterialDrawableBuilder.IconValue.PLAY)
        }
    }

    fun updateSettingStatus(isHidden: Boolean) {
        if (isHidden) {
            mSettingLinearLayout?.visibility = View.GONE
        } else {
            mSettingLinearLayout?.visibility = View.VISIBLE
        }
    }

    /**
     * 更新歌词锁定状态
     * @param lock 锁定
     * @param toast 是否弹出提示
     */
    fun saveLock(lock: Boolean, toast: Boolean) {
        mIsLock = lock
        LogUtil.d("FloatLyricView", "桌面歌词状态：mIsLock:$mIsLock$lock")
        SPUtils.putAnyCommit(SPUtils.SP_KEY_FLOAT_LYRIC_LOCK, mIsLock)
        if (toast) {
            ToastUtils.show(MusicApp.getAppContext(), if (!mIsLock) R.string.float_unlock else R.string.float_lock)
        }
        if (layoutParams != null) {
            val params = layoutParams as WindowManager.LayoutParams
            if (lock) {
                //锁定后点击通知栏解锁
                mNotify.notifyToUnlock()
                params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                mLockButton?.setIcon(MaterialDrawableBuilder.IconValue.LOCK)
            } else {
                mMovement = true
                mNotify.cancel()
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                mLockButton?.setIcon(MaterialDrawableBuilder.IconValue.LOCK_OPEN)
            }
            showLyricBackground()
            windowManager.updateViewLayout(this, params)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mNotify.cancel()
    }

    companion object {

        /**
         * 记录系统状态栏的高度
         */
        private var statusBarHeight: Int = 0
    }

}