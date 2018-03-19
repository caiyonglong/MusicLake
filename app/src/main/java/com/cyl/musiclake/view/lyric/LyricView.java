package com.cyl.musiclake.view.lyric;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.cyl.musiclake.utils.LogUtil;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Author   : cyl
 * version  : 2018/01/17
 * function : 歌词解析显示
 */

public class LyricView extends View {

    private int mBtnColor = Color.parseColor("#0091EA");  // 按钮颜色
    private int mHintColor = Color.parseColor("#FFFFFF");  // 提示语颜色
    private int mDefaultColor = Color.parseColor("#FFFFFF");  // 默认字体颜色
    private int mIndicatorColor = Color.parseColor("#EFEFEF");  // 指示器颜色
    private int mHighLightColor = Color.parseColor("#ffb701");  // 当前播放位置的颜色
    private int mCurrentShowColor = Color.parseColor("#AAAAAA");  // 当前拖动位置的颜色

    private int mLineCount;  // 行数
    private float mLineHeight;  // 行高

    private float mScrollY = 0;  // 纵轴偏移量
    private float mVelocity = 0;  // 纵轴上的滑动速度
    private float mLineSpace = 0;  // 行间距（包含在行高中）
    private float mShaderWidth = 0;  // 渐变过渡的距离
    private int mCurrentShowLine = 0;  // 当前拖动位置对应的行数
    private int mCurrentPlayLine = 0;  // 当前播放位置对应的行数
    private int mMinStartUpSpeed = 1600;  // 最低滑行启动速度

    private boolean mUserTouch = false;  // 判断当前用户是否触摸
    private boolean mIndicatorShow = false;  // 判断当前滑动指示器是否显示
    private boolean mIsMoved = false;//判断用户触摸时是否发生move事件
    private boolean mPlayerClick = false; //判断当前用户是否点击指示器

    /***/
    private int mBtnWidth = 0;  // Btn 按钮的宽度
    private int mDefaultMargin = 12;
    private int maximumFlingVelocity;  // 最大纵向滑动速度
    private Rect mBtnBound, mTimerBound;
    private VelocityTracker mVelocityTracker;

    private LyricInfo mLyricInfo;
    private String mDefaultTime = "00:00";
    private String mDefaultHint = "暂无歌词";
    private Paint mTextPaint, mBtnPaint, mIndicatorPaint;

    private OnPlayerClickListener mClickListener;

    private final int MSG_PLAYER_SLIDE = 0x158;
    private final int MSG_PLAYER_HIDE = 0x157;

    private ValueAnimator mFlingAnimator;
    private boolean mPlayable = false;
    private boolean mSliding = false;
    private boolean mTouchable = true;

    public LyricView(Context context) {
        super(context);
        initMyView(context);
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView(context);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView(context);
    }

    private void initMyView(Context context) {
        maximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        initAllPaints();
        initAllBounds();
    }

    /**
     * 初始化需要的尺寸
     */
    private void initAllBounds() {
        setTextSize(15);
        setLineSpace(12);
        mBtnWidth = (int) (getRawSize(TypedValue.COMPLEX_UNIT_SP, 24));
        mTimerBound = new Rect();
        mIndicatorPaint.getTextBounds(mDefaultTime, 0, mDefaultTime.length(), mTimerBound);

        measureLineHeight();
    }

    /**
     * 初始化画笔
     */
    private void initAllPaints() {
        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setDither(true);
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP, 12));
        mIndicatorPaint.setTextAlign(Paint.Align.CENTER);

        mBtnPaint = new Paint();
        mBtnPaint.setDither(true);
        mBtnPaint.setAntiAlias(true);
        mBtnPaint.setColor(mBtnColor);
        mBtnPaint.setStrokeWidth(5.0f);
        mBtnPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mShaderWidth = getMeasuredHeight() * 0.3f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLyricInfo != null && mLyricInfo.song_lines != null && mLyricInfo.song_lines.size() > 0) {
            for (int i = 0, size = mLineCount; i < size; i++) {
                float x = getMeasuredWidth() * 0.5f;
                float y = getMeasuredHeight() * 0.5f + (i + 0.5f) * mLineHeight - 6 - mLineSpace * 0.5f - mScrollY;
                if (y + mLineHeight * 0.5f < 0) {
                    continue;
                }
                if (y - mLineHeight * 0.5f > getMeasuredHeight()) {
                    break;
                }
                if (i == mCurrentPlayLine - 1) {
                    mTextPaint.setColor(mHighLightColor);
                } else {
                    if (mIndicatorShow && i == mCurrentShowLine - 1) {
                        mTextPaint.setColor(mCurrentShowColor);
                    } else {
                        mTextPaint.setColor(mDefaultColor);
                    }
                }
                if (y > getMeasuredHeight() - mShaderWidth || y < mShaderWidth) {
                    if (y < mShaderWidth) {
                        mTextPaint.setAlpha(26 + (int) (23000.0f * y / mShaderWidth * 0.01f));
                    } else {
                        mTextPaint.setAlpha(26 + (int) (23000.0f * (getMeasuredHeight() - y) / mShaderWidth * 0.01f));
                    }
                } else {
                    mTextPaint.setAlpha(255);
                }
                canvas.drawText(mLyricInfo.song_lines.get(i).content, x, y, mTextPaint);
            }
        } else {
            mTextPaint.setColor(mHintColor);
            canvas.drawText(mDefaultHint, getMeasuredWidth() * 0.5f, (getMeasuredHeight() + mLineHeight - 6) * 0.5f, mTextPaint);
        }
        /**
         * 滑动提示部分内容绘制
         * */
        if (mIndicatorShow && scrollable()) {
            if (mPlayable) {
                drawPlayer(canvas);
                drawIndicator(canvas);
            }
        }
    }

    /**
     * 绘制右侧的播放按钮
     *
     * @param canvas
     */
    private void drawPlayer(Canvas canvas) {
        mBtnBound = new Rect(getMeasuredWidth() - mDefaultMargin - mBtnWidth, (int) (getMeasuredHeight() * 0.5f - mBtnWidth * 0.5f), getMeasuredWidth() - mDefaultMargin, (int) (getMeasuredHeight() * 0.5f + mBtnWidth * 0.5f));

        Path path = new Path();
        float radio = mBtnBound.width() * 0.3f;
        float value = (float) Math.sqrt(Math.pow(radio, 2) - Math.pow(radio * 0.5f, 2));
        path.moveTo(mBtnBound.centerX() - radio * 0.5f, mBtnBound.centerY() - value);
        path.lineTo(mBtnBound.centerX() - radio * 0.5f, mBtnBound.centerY() + value);
        path.lineTo(mBtnBound.centerX() + radio, mBtnBound.centerY());
        path.lineTo(mBtnBound.centerX() - radio * 0.5f, mBtnBound.centerY() - value);
        mBtnPaint.setAlpha(128);
        canvas.drawPath(path, mBtnPaint);  // 绘制播放按钮的三角形
        canvas.drawCircle(mBtnBound.centerX(), mBtnBound.centerY(), mBtnBound.width() * 0.48f, mBtnPaint);  // 绘制圆环
    }

    /**
     * 绘制指示器
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setAlpha(128);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(measureCurrentTime(), mTimerBound.width() * 0.5f + mDefaultMargin, (getMeasuredHeight() + mTimerBound.height() - 6) * 0.5f, mIndicatorPaint);

        Path path = new Path();
        mIndicatorPaint.setStrokeWidth(2.0f);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        path.moveTo(mTimerBound.width() + mDefaultMargin, getMeasuredHeight() * 0.5f);
        path.lineTo(mPlayable ? mBtnBound.left - 24 : getMeasuredWidth() - 24, getMeasuredHeight() * 0.5f);

        canvas.drawPath(path, mIndicatorPaint);
    }

    /**
     * 计算行高度
     */
    private void measureLineHeight() {
        Rect lineBound = new Rect();
        mTextPaint.getTextBounds(mDefaultHint, 0, mDefaultHint.length(), lineBound);
        mLineHeight = lineBound.height() + mLineSpace;
    }

    /**
     * 获取当前滑动到的位置的当前时间
     */
    private String measureCurrentTime() {
        DecimalFormat format = new DecimalFormat("00");
        if (mLyricInfo != null && mLineCount > 0 && mCurrentShowLine - 1 < mLineCount && mCurrentShowLine > 0) {
            return format.format(mLyricInfo.song_lines.get(mCurrentShowLine - 1).start / 1000 / 60) + ":" + format.format(mLyricInfo.song_lines.get(mCurrentShowLine - 1).start / 1000 % 60);
        }
        if (mLyricInfo != null && mLineCount > 0 && (mCurrentShowLine - 1) >= mLineCount) {
            return format.format(mLyricInfo.song_lines.get(mLineCount - 1).start / 1000 / 60) + ":" + format.format(mLyricInfo.song_lines.get(mLineCount - 1).start / 1000 % 60);
        }
        if (mLyricInfo != null && mLineCount > 0 && mCurrentShowLine - 1 <= 0) {
            return format.format(mLyricInfo.song_lines.get(0).start / 1000 / 60) + ":" + format.format(mLyricInfo.song_lines.get(0).start / 1000 % 60);
        }
        return mDefaultTime;
    }

    private float mDownX, mDownY, mLastScrollY;      // 记录手指按下时的坐标和当前的滑动偏移量


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("LyricView_dispatch", event.getAction() + "----");
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownX);
                final float deltaY = Math.abs(y - mDownY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX < deltaY) {
                    Log.e("MotionEvent", "down");
                    setUserTouch(true);
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    Log.e("MotionEvent", "lefttoright");
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("LyricView_onTouchEvent", event.getAction() + "----");

        if (!mTouchable) {
            return super.onTouchEvent(event);
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                actionCancel(event);
                break;
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
            default:
                break;
        }
        invalidateView();
        if (mIsMoved || mPlayerClick) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 手势取消执行事件
     *
     * @param event
     */
    private void actionCancel(MotionEvent event) {
        releaseVelocityTracker();
    }

    /**
     * 手势按下执行事件
     *
     * @param event
     */
    private void actionDown(MotionEvent event) {
        postman.removeMessages(MSG_PLAYER_SLIDE);
        postman.removeMessages(MSG_PLAYER_HIDE);
        mLastScrollY = mScrollY;
        mDownX = event.getX();
        mDownY = event.getY();
        if (mFlingAnimator != null) {
            mFlingAnimator.cancel();
            mFlingAnimator = null;
        }
        mIsMoved = false;
        mPlayerClick = false;
    }

    /**
     * 手势移动执行事件
     *
     * @param event
     */
    private void actionMove(MotionEvent event) {
        Log.e("actionMove", scrollable() + "----");
        if (scrollable()) {
            final VelocityTracker tracker = mVelocityTracker;
            tracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            float scrollY = mLastScrollY + mDownY - event.getY();   // 102  -2  58  42
            float value01 = scrollY - (mLineCount * mLineHeight * 0.5f);   // 52  -52  8  -8
            float value02 = ((Math.abs(value01) - (mLineCount * mLineHeight * 0.5f)));   // 2  2  -42  -42
            mScrollY = value02 > 0 ? scrollY - (measureDampingDistance(value02) * value01 / Math.abs(value01)) : scrollY;   //   value01 / Math.abs(value01)  控制滑动方向
            mVelocity = tracker.getYVelocity();
            measureCurrentLine();
            if (Math.abs(mVelocity) > 1) {
                mIsMoved = true;
            }
        }
    }

    /**
     * 计算阻尼效果的大小
     */
    private final int mMaxDampingDistance = 360;

    private float measureDampingDistance(float value02) {
        return value02 > mMaxDampingDistance ? (mMaxDampingDistance * 0.6f + (value02 - mMaxDampingDistance) * 0.72f) : value02 * 0.6f;
    }

    /**
     * 手势抬起执行事件
     */
    private void actionUp(MotionEvent event) {
        releaseVelocityTracker();
        // 2.4s 后发送一个指示器隐藏的请求
        postman.sendEmptyMessageDelayed(MSG_PLAYER_HIDE, 2400);
        if (scrollable()) {
            setUserTouch(false);  // 用户手指离开屏幕，取消触摸标记
            if (overScrolled() && mScrollY < 0) {
                smoothScrollTo(0);
                return;
            }
            if (overScrolled() && mScrollY > mLineHeight * (mLineCount - 1)) {
                smoothScrollTo(mLineHeight * (mLineCount - 1));
                return;
            }
            if (Math.abs(mVelocity) > mMinStartUpSpeed) {
                doFlingAnimator(mVelocity);
                return;
            }
            if (mIndicatorShow && clickPlayer(event)) {
                if (mCurrentShowLine != mCurrentPlayLine) {
                    mIndicatorShow = false;
                    mPlayerClick = true;
                    if (mClickListener != null) {
                        mClickListener.onPlayerClicked(mLyricInfo.song_lines.get(mCurrentShowLine - 1).start, mLyricInfo.song_lines.get(mCurrentShowLine - 1).content);
                    }
                }
            }
        } else {
            performClick();
        }
    }

    /**
     * 刷新View
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //  当前线程是主UI线程，直接刷新。
            invalidate();
        } else {
            //  当前线程是非UI线程，post刷新。
            postInvalidate();
        }
    }

    /**
     * 设置用户是否触摸的标记
     *
     * @param isUserTouch 标记用户是否触摸屏幕
     */
    private void setUserTouch(boolean isUserTouch) {
        if (mUserTouch == isUserTouch) {
            return;
        }
        mUserTouch = isUserTouch;
        if (isUserTouch) {
            mIndicatorShow = isUserTouch;
        }
    }

    /**
     * 释放速度追踪器
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 滑行动画
     *
     * @param velocity 滑动速度
     */
    private void doFlingAnimator(float velocity) {
        //注：     Math.abs(velocity)  < =  16000
        float distance = (velocity / Math.abs(velocity) * Math.min((Math.abs(velocity) * 0.050f), 640));   // 计算就已当前的滑动速度理论上的滑行距离是多少
        float to = Math.min(Math.max(0, (mScrollY - distance)), (mLineCount - 1) * mLineHeight);   // 综合考虑边界问题后得出的实际滑行距离

        mFlingAnimator = ValueAnimator.ofFloat(mScrollY, to);
        mFlingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScrollY = (float) animation.getAnimatedValue();
                measureCurrentLine();
                invalidateView();
            }
        });

        mFlingAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mVelocity = mMinStartUpSpeed - 1;
                mSliding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSliding = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });

        mFlingAnimator.setDuration(420);
        mFlingAnimator.setInterpolator(new DecelerateInterpolator());
        mFlingAnimator.start();
    }

    /**
     * To measure current showing line number based on the view's scroll Y
     */
    private void measureCurrentLine() {
        float baseScrollY = mScrollY + mLineHeight * 0.5f;
        mCurrentShowLine = (int) (baseScrollY / mLineHeight + 1);
    }

    /**
     * Input current showing line to measure the view's current scroll Y
     *
     * @param line 当前指定行号
     */
    private float measureCurrentScrollY(int line) {
        return (line - 1) * mLineHeight;
    }

    /**
     * 判断当前点击事件是否落在播放按钮触摸区域范围内
     *
     * @param event 触摸事件
     */
    private boolean clickPlayer(MotionEvent event) {
        if (mBtnBound != null && mDownX > (mBtnBound.left - mDefaultMargin) && mDownX < (mBtnBound.right + mDefaultMargin) && mDownY > (mBtnBound.top - mDefaultMargin) && mDownY < (mBtnBound.bottom + mDefaultMargin)) {
            float upX = event.getX();
            float upY = event.getY();
            return upX > (mBtnBound.left - mDefaultMargin) && upX < (mBtnBound.right + mDefaultMargin) && upY > (mBtnBound.top - mDefaultMargin) && upY < (mBtnBound.bottom + mDefaultMargin);
        }
        return false;
    }

    /**
     * 从当前位置滑动到指定位置上
     *
     * @param toY 指定纵坐标位置
     */
    private void smoothScrollTo(float toY) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mScrollY, toY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mUserTouch) {
                    animator.cancel();
                    return;
                }
                mScrollY = (float) animation.getAnimatedValue();
                invalidateView();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSliding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSliding = false;
                measureCurrentLine();
                invalidateView();
            }
        });
        animator.setDuration(640);
        animator.setInterpolator(new OvershootInterpolator(0.5f));

        animator.start();
    }

    /**
     * 判断是否可以进行滑动
     */
    private boolean scrollable() {
        return mLyricInfo != null && mLyricInfo.song_lines != null && mLyricInfo.song_lines.size() > 0;
    }

    /**
     * 判断当前View是否已经滑动到歌词的内容区域之外
     */
    private boolean overScrolled() {
        return scrollable() && (mScrollY > mLineHeight * (mLineCount - 1) || mScrollY < 0);
    }

    @SuppressLint("HandlerLeak")
    Handler postman = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PLAYER_HIDE:
                    postman.sendEmptyMessageDelayed(MSG_PLAYER_SLIDE, 1200);
                    mIndicatorShow = false;
                    invalidateView();
                case MSG_PLAYER_SLIDE:
                    smoothScrollTo(measureCurrentScrollY(mCurrentPlayLine));
                    invalidateView();
            }
        }
    };

    /**
     * 根据当前给定的时间戳滑动到指定位置
     *
     * @param time 时间戳
     */
    private void scrollToCurrentTimeMillis(long time) {
        int position = 0;
        if (scrollable()) {
            for (int i = 0, size = mLineCount; i < size; i++) {
                LyricInfo.LineInfo lineInfo = mLyricInfo.song_lines.get(i);
                if (lineInfo != null && lineInfo.start > time) {
                    position = i;
                    break;
                }
                if (i == mLineCount - 1) {
                    position = mLineCount;
                }
            }
        }
        if (mCurrentPlayLine != position && !mUserTouch && !mSliding && !mIndicatorShow) {
            mCurrentPlayLine = position;
            smoothScrollTo(measureCurrentScrollY(position));
        } else {
            if (!mSliding && !mIndicatorShow) {
                mCurrentPlayLine = mCurrentShowLine = position;
            }
        }
    }

    /**
     * 初始化控件
     */
    private void resetView() {
        mCurrentPlayLine = mCurrentShowLine = 0;
        mLyricInfo = null;
        invalidateView();
        mLineCount = 0;
        mScrollY = 0;
    }

    public interface OnPlayerClickListener {
        void onPlayerClicked(long progress, String content);
    }

    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            measureLineHeight();
            mScrollY = measureCurrentScrollY(mCurrentPlayLine);
            invalidateView();
        }
    }

    private float getRawSize(int unit, float size) {
        Context context = getContext();
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
    }


    /**
     *
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ *
     *                                                                                             对外API                                                                                        *
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ *
     *
     *  */

    /**
     * 设置当前时间显示位置
     *
     * @param current 时间戳
     */
    public void setCurrentTimeMillis(long current) {
        scrollToCurrentTimeMillis(current);
    }

    /**
     * 设置歌词文件
     *
     * @param file        歌词文件
     * @param charsetName 解析字符集
     */
    public void setLyricFile(File file, String charsetName) {
        mLyricInfo = LyricParseUtils.setLyricResource(file);
        if (mLyricInfo != null && mLyricInfo.getSong_lines() != null)
            mLineCount = mLyricInfo.getSong_lines().size();
        invalidateView();
    }

    /**
     * 设置歌词字符串
     *
     * @param lyricInfo   歌词字符串
     * @param charsetName 解析字符集
     */
    public void setLyricContent(String lyricInfo, String charsetName) {
        LogUtil.e("LyricView ", lyricInfo);
        mLyricInfo = LyricParseUtils.setLyricResource(lyricInfo);
        if (mLyricInfo != null && mLyricInfo.getSong_lines() != null)
            mLineCount = mLyricInfo.getSong_lines().size();
        invalidateView();
    }

    /**
     * 设置播放按钮点击监听事件
     *
     * @param mClickListener 监听器
     */
    public void setOnPlayerClickListener(OnPlayerClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    /**
     * 重置、设置歌词内容被重置后的提示内容
     *
     * @param message 提示内容
     */
    public void reset(String message) {
        mDefaultHint = message;
        resetView();
    }

    /**
     * 设置高亮显示文本的字体颜色
     *
     * @param color 颜色值
     */
    public void setHighLightTextColor(int color) {
        if (mHighLightColor != color) {
            mHighLightColor = color;
            invalidateView();
        }
    }

    public void setDefaultColor(int color) {
        if (mDefaultColor != color) {
            mDefaultColor = color;
            invalidateView();
        }
    }

    public int getDefaultColor() {
        return mDefaultColor;
    }

    /**
     * 设置歌词内容行间距
     *
     * @param lineSpace 行间距大小
     */
    public void setLineSpace(float lineSpace) {
        if (mLineSpace != lineSpace) {
            mLineSpace = getRawSize(TypedValue.COMPLEX_UNIT_SP, lineSpace);
            measureLineHeight();
            mScrollY = measureCurrentScrollY(mCurrentPlayLine);
            invalidateView();
        }
    }

    /**
     * 设置歌词文本内容字体大小
     *
     * @param unit
     * @param size
     */
    public void setTextSize(int unit, float size) {
        setRawTextSize(getRawSize(unit, size));
    }

    /**
     * 设置歌词文本内容字体大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setPlayable(boolean playable) {
        mPlayable = playable;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public void setHintColor(int color) {
        if (mHintColor != color) {
            mHintColor = color;
            invalidate();
        }
    }

}
