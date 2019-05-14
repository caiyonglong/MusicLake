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

    var mLyricText: LyricTextView
    var mTitle: TextView
    var mSizeSeekBar: SeekBar
    var mColorSeekBar: ColorSeekBar
    private val mLockButton: MaterialIconView
    private val mPreButton: MaterialIconView
    private val mNextButton: MaterialIconView
    private val mPlayButton: MaterialIconView
    private val mSettingsButton: MaterialIconView
    private val mCloseButton: ImageButton
    private val mMusicButton: ImageButton
    private val mSettingLinearLayout: LinearLayout
    private val mRelLyricView: LinearLayout
    private val mLinLyricView: LinearLayout
    private val mFrameBackground: FrameLayout
    private val mRootView: View?
    private var mIsLock: Boolean = false
    private val mNotify: UnLockNotify


    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mNotify = UnLockNotify()

        mRootView = LayoutInflater.from(context).inflate(R.layout.float_lyric_view, this)
        val view = findViewById<FrameLayout>(R.id.small_window_layout)
        viewWidth = view.layoutParams.width
        viewHeight = view.layoutParams.height
        mMovement = true
        isHiddenSettings = true

        mTitle = findViewById(R.id.music_title)
        mSizeSeekBar = findViewById(R.id.sb_size)
        mColorSeekBar = findViewById(R.id.sb_color)
        mLyricText = findViewById(R.id.lyric)
        mCloseButton = findViewById(R.id.btn_close)
        mMusicButton = findViewById(R.id.music_app)
        mLockButton = findViewById(R.id.btn_lock)
        mPreButton = findViewById(R.id.btn_previous)
        mPlayButton = findViewById(R.id.btn_play)
        mNextButton = findViewById(R.id.btn_next)
        mSettingsButton = findViewById(R.id.btn_settings)
        mSettingLinearLayout = findViewById(R.id.ll_settings)
        mRelLyricView = findViewById(R.id.rl_layout)
        mLinLyricView = findViewById(R.id.ll_layout)
        mFrameBackground = findViewById(R.id.small_bg)

        mCloseButton.setOnClickListener(this)
        mMusicButton.setOnClickListener(this)
        mLockButton.setOnClickListener(this)
        mPreButton.setOnClickListener(this)
        mPlayButton.setOnClickListener(this)
        mNextButton.setOnClickListener(this)
        mSettingsButton.setOnClickListener(this)

        mFontSize = SPUtils.getFontSize().toFloat()
        mLyricText.setFontSizeScale(mFontSize)
        mSizeSeekBar.progress = mFontSize.toInt()

        if (mIsLock) {
            toggleLyricView()
        }

        mFontColor = SPUtils.getFontColor()
        mLyricText.setFontColorScale(mFontColor)
        mColorSeekBar.colorBarPosition = mFontColor

        setPlayStatus(MusicPlayerService.getInstance().isPlaying)

        mSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                LogUtil.e("TEST", "$progress---$fromUser")
                mLyricText.setFontSizeScale(progress.toFloat())
                SPUtils.saveFontSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        mColorSeekBar.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            mLyricText.setFontColorScale(color)
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
                    toggleLyricView()
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
     * toggle背景
     */
    private fun toggleLyricView() {
        if (mRootView != null) {
            if (mRelLyricView.visibility == View.INVISIBLE) {
                mRelLyricView.visibility = View.VISIBLE
                mLinLyricView.visibility = View.VISIBLE
                mFrameBackground.visibility = View.VISIBLE
            } else {
                if (!isHiddenSettings) {
                    isHiddenSettings = true
                    updateSettingStatus(isHiddenSettings)
                }
                mLinLyricView.visibility = View.INVISIBLE
                mRelLyricView.visibility = View.INVISIBLE
                mFrameBackground.visibility = View.INVISIBLE
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
                    mLockButton.setIcon(MaterialDrawableBuilder.IconValue.LOCK_OPEN)
                } else {
                    saveLock(true, true)
                    mLockButton.setIcon(MaterialDrawableBuilder.IconValue.LOCK)
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
            mPlayButton.setIcon(MaterialDrawableBuilder.IconValue.PAUSE)
        } else {
            mPlayButton.setIcon(MaterialDrawableBuilder.IconValue.PLAY)
        }
    }

    fun updateSettingStatus(isHidden: Boolean) {
        if (isHidden) {
            mSettingLinearLayout.visibility = View.GONE
        } else {
            mSettingLinearLayout.visibility = View.VISIBLE
        }
    }


    fun saveLock(lock: Boolean, toast: Boolean) {
        mIsLock = lock
        SPUtils.putAnyCommit(SPUtils.SP_KEY_FLOAT_LYRIC_LOCK, mIsLock)
        if (toast) {
            ToastUtils.show(MusicApp.getAppContext(), if (!mIsLock) R.string.float_unlock else R.string.float_lock)
        }
        val params = layoutParams as WindowManager.LayoutParams
        if (params != null) {
            if (lock) {
                //锁定后点击通知栏解锁
                mNotify.notifyToUnlock()
                params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                mMovement = true
                mNotify.cancel()
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }
            toggleLyricView()
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