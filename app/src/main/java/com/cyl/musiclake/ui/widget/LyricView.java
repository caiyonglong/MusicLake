package com.cyl.musiclake.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.cyl.musiclake.R;
import com.cyl.musiclake.player.FloatLyricViewManager;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.ui.widget.lyric.LyricInfo;
import com.cyl.musiclake.ui.widget.lyric.LyricInfo.LineInfo;
import com.cyl.musiclake.ui.widget.lyric.LyricParseUtils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义LrcView,可以同步显示歌词，拖动歌词
 * v2.5 对自定义控件添加自定义属性 defStyleAttr
 */
public class LyricView extends View {

    private static final String TAG = "LyricView";

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    private static final float SLIDE_COEFFICIENT = 0.2f;

    private static final int UNITS_SECOND = 1000;
    private static final int UNITS_MILLISECOND = 1;

    private static final int FLING_ANIMATOR_DURATION = 420 * UNITS_MILLISECOND;

    private static final int THRESHOLD_Y_VELOCITY = 1600;

    private static final int INDICATOR_ICON_PLAY_MARGIN_LEFT = 7;//dp
    private static final int INDICATOR_ICON_PLAY_WIDTH = 15;//sp
    private static final int INDICATOR_LINE_MARGIN = 10;//dp
    private static final int INDICATOR_TIME_TEXT_SIZE = 10;//sp
    private static final int INDICATOR_TIME_MARGIN_RIGHT = 2;//dp

    private static final int DEFAULT_TEXT_SIZE = 16;//sp
    private static final int DEFAULT_MAX_LENGTH = 300;//dp
    private static final int DEFAULT_LINE_SPACE = 25;//dp

    private int mHintColor;
    private int mDefaultColor;
    private int mHighLightColor;
    private int mTextAlign;


    private int mLineCount;
    private int mTextSize;
    private float mLineHeight;
    private LyricInfo mLyricInfo;
    private String mDefaultHint = "暂无歌词";
    private int mMaxLength;

    private TextPaint mTextPaint;
    private Paint mBtnPlayPaint;
    private Paint mLinePaint;
    private Paint mTimerPaint;

    private final int MSG_PLAYER_SLIDE = 0x158;
    private final int MSG_PLAYER_HIDE = 0x157;

    private Bitmap bitmap;
    private boolean mFling = false;
    private ValueAnimator mFlingAnimator;
    private float mScrollY = 0;
    private float mLineSpace = 0;
    private boolean mIsShade;
    private float mShaderWidth = 0;
    private int mCurrentPlayLine = 0;
    private boolean mShowIndicator;

    private VelocityTracker mVelocityTracker;
    private float mVelocity = 0;
    private float mDownX;
    private float mDownY;
    private float mLastScrollY;
    private boolean mUserTouch = false;
    private int maxVelocity;
    private int mLineNumberUnderIndicator = 0;
    private Rect mBtnPlayRect = new Rect();
    private Rect mTimerRect;
    private String mDefaultTime = "00:00";

    private int mLineColor = Color.parseColor("#EFEFEF");
    private int mBtnColor = Color.parseColor("#EFEFEF");

    private List<Integer> mLineFeedRecord = new ArrayList<>();
    private boolean mEnableLineFeed = false;
    private int mExtraHeight = 0;

    private int mTextHeight;

    private boolean mPlayable = false;
    private boolean mSliding = false;
    private boolean mTouchable = true;


    private OnPlayerClickListener mClickListener;


    public LyricView(Context context) {
        super(context);
        initMyView(context);
    }

    public LyricView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getAttrs(context, attributeSet);
        initMyView(context);

    }

    public LyricView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getAttrs(context, attributeSet);
        initMyView(context);
    }


    //
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.e("LyricView_dispatch", event.getAction() + "----");
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
                    LogUtil.e("MotionEvent", "down");
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    LogUtil.e("MotionEvent", "lefttoright");
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mTouchable) return super.onTouchEvent(event);
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
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBtnPlayRect.set((int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_ICON_PLAY_MARGIN_LEFT),
                (int) (getHeight() * 0.5f - getRawSize(TypedValue.COMPLEX_UNIT_SP, INDICATOR_ICON_PLAY_WIDTH) * 0.5f),
                (int) (getRawSize(TypedValue.COMPLEX_UNIT_SP, INDICATOR_ICON_PLAY_WIDTH) + getRawSize(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_ICON_PLAY_MARGIN_LEFT)),
                (int) (getHeight() * 0.5f + getRawSize(TypedValue.COMPLEX_UNIT_SP, INDICATOR_ICON_PLAY_WIDTH) * 0.5f));
        mShaderWidth = getWidth() * 0.4f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (scrollable()) {
            if (mShowIndicator) {
                drawIndicator(canvas);
            }
            for (int i = 0; i < mLineCount; i++) {
                float x = 0;
                switch (mTextAlign) {
                    case LEFT:
                        x = INDICATOR_ICON_PLAY_MARGIN_LEFT + INDICATOR_LINE_MARGIN + mBtnPlayRect.width();
                        break;
                    case CENTER:
                        x = getWidth() * 0.5f;
                        break;
                    case RIGHT:
                        x = getWidth() - INDICATOR_LINE_MARGIN * 2 - mTimerRect.width() - INDICATOR_ICON_PLAY_MARGIN_LEFT;
                        break;
                }

                float y;
                if (mEnableLineFeed && i > 0) {
                    y = getMeasuredHeight() * 0.5f + i * mLineHeight - mScrollY + mLineFeedRecord.get(i - 1);
                } else {
                    y = getMeasuredHeight() * 0.5f + i * mLineHeight - mScrollY;
                }

//                float y = getHeight() * 0.5f + i * mLineHeight - mScrollY;
                if (y < 0) {
                    continue;
                }
                if (y > getHeight()) {
                    break;
                }
                if (i == mCurrentPlayLine - 1) {
                    mTextPaint.setColor(mHighLightColor);
                } else if (i == mLineNumberUnderIndicator && mShowIndicator) {
                    mTextPaint.setColor(Color.LTGRAY);
                } else {
                    mTextPaint.setColor(mDefaultColor);
                }
                if (mIsShade && (y > getHeight() - mShaderWidth || y < mShaderWidth)) {
                    if (y < mShaderWidth) {
                        mTextPaint.setAlpha(26 + (int) (23000.0f * y / mShaderWidth * 0.01f));
                    } else {
                        mTextPaint.setAlpha(26 + (int) (23000.0f * (getHeight() - y) / mShaderWidth * 0.01f));
                    }
                } else {
                    mTextPaint.setAlpha(255);
                }

                if (mEnableLineFeed) {
                    @SuppressLint("DrawAllocation") StaticLayout staticLayout = new StaticLayout(mLyricInfo.songLines.get(i).content, mTextPaint,
                            mMaxLength,
                            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    canvas.save();
                    canvas.translate(x, y);
                    staticLayout.draw(canvas);
                    canvas.restore();
                    staticLayout = null;
                } else {
                    canvas.drawText(mLyricInfo.songLines.get(i).content, x, y, mTextPaint);
                }
            }
        } else {
            mTextPaint.setColor(mHintColor);
            canvas.drawText(mDefaultHint, getMeasuredWidth() / 2, getMeasuredHeight() / 2, mTextPaint);
        }
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LyricView);
        mIsShade = ta.getBoolean(R.styleable.LyricView_fadeInFadeOut, false);
        mDefaultHint = ta.getString(R.styleable.LyricView_hint) != null
                ? ta.getString(R.styleable.LyricView_hint)
                : getResources().getString(R.string.lyric_default_hint);
        mHintColor = ta.getColor(R.styleable.LyricView_hintColor, Color.parseColor("#FFFFFF"));
        mDefaultColor = ta.getColor(R.styleable.LyricView_textColor, Color.parseColor("#8D8D8D"));
        mHighLightColor = ta.getColor(R.styleable.LyricView_highlightColor, Color.parseColor("#FFFFFF"));
        mTextSize = ta.getDimensionPixelSize(R.styleable.LyricView_textSize, (int) getRawSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE));
        mTextAlign = ta.getInt(R.styleable.LyricView_textAlign, CENTER);
        mMaxLength = ta.getDimensionPixelSize(R.styleable.LyricView_maxLength, (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MAX_LENGTH));
        mLineSpace = ta.getDimensionPixelSize(R.styleable.LyricView_lineSpace, (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_SPACE));
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location);
//
        ta.recycle();

    }

    public void setOnPlayerClickListener(OnPlayerClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setAlignment(@Alignment int alignment) {
        mTextAlign = alignment;
    }

    public void setCurrentTimeMillis(long current) {
        scrollToCurrentTimeMillis(current);
    }

    public void setLyricFile(File file, String charsetName) {
        if (file != null && file.exists()) {
            mLyricInfo = LyricParseUtils.setLyricResource(file);
            if (mLyricInfo != null && mLyricInfo.getSongLines() != null) {
                mLineCount = mLyricInfo.getSongLines().size();
                for (int i = 0; i < mLineCount; i++) {

                    StaticLayout staticLayout = new StaticLayout(mLyricInfo.songLines.get(i).content, mTextPaint,
                            (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MAX_LENGTH),
                            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                    if (staticLayout.getLineCount() > 1) {
                        mEnableLineFeed = true;
                        mExtraHeight = mExtraHeight + (staticLayout.getLineCount() - 1) * mTextHeight;
                    }
                    mLineFeedRecord.add(i, mExtraHeight);
                }
            }
        } else {
            invalidateView();
        }
    }

    /**
     * 设置歌词字符串
     *
     * @param lyricInfo 歌词字符串
     */
    public void setLyricContent(String lyricInfo) {
        if (lyricInfo != null && lyricInfo.length() >= 1) {
            mLyricInfo = LyricParseUtils.setLyricResource(lyricInfo);
            if (mLyricInfo != null && mLyricInfo.getSongLines() != null && mLyricInfo.getSongLines().size() > 0) {
                mLineCount = mLyricInfo.getSongLines().size();

                for (int i = 0; i < mLyricInfo.songLines.size(); i++) {

                    StaticLayout staticLayout = new StaticLayout(mLyricInfo.songLines.get(i).content, mTextPaint,
                            (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MAX_LENGTH),
                            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                    if (staticLayout.getLineCount() > 1) {
                        mEnableLineFeed = true;
                        mExtraHeight = mExtraHeight + (staticLayout.getLineCount() - 1) * mTextHeight;
                    }
                    mLineFeedRecord.add(i, mExtraHeight);
                }
            } else {
                mDefaultHint = getContext().getString(R.string.lyric_default_hint);
                invalidateView();
            }
        } else {
            mLyricInfo = null;
            mDefaultHint = getContext().getString(R.string.lyric_default_hint);
            invalidateView();
        }
    }

    private void setLineSpace(float lineSpace) {
        if (mLineSpace != lineSpace) {
            mLineSpace = getRawSize(TypedValue.COMPLEX_UNIT_DIP, lineSpace);
            measureLineHeight();
            mScrollY = measureCurrentScrollY(mCurrentPlayLine);
            invalidateView();
        }
    }


    public void reset(String hintInfo) {
        mDefaultHint = hintInfo;
        resetView();
    }

    public void reset() {
        mDefaultHint = getResources().getString(R.string.lyric_default_hint);
        resetView();
    }

    private void actionCancel(MotionEvent event) {
        releaseVelocityTracker();
    }

    private void actionDown(MotionEvent event) {
//        removeCallbacks(hideIndicator);
        postman.removeMessages(MSG_PLAYER_HIDE);
        postman.removeMessages(MSG_PLAYER_SLIDE);
        mLastScrollY = mScrollY;
        mDownX = event.getX();
        mDownY = event.getY();
        if (mFlingAnimator != null) {
            mFlingAnimator.cancel();
            mFlingAnimator = null;
        }
        setUserTouch(true);
    }

    private boolean overScrolled() {
        return scrollable() && (mScrollY > mLineHeight * (mLineCount - 1) + mLineFeedRecord.get(mLineCount - 1) + (mEnableLineFeed ? mTextHeight : 0) || mScrollY < 0);
    }


    private void actionMove(MotionEvent event) {
        if (scrollable()) {
            final VelocityTracker tracker = mVelocityTracker;
            tracker.computeCurrentVelocity(UNITS_SECOND, maxVelocity);
            mScrollY = mLastScrollY + mDownY - event.getY();
            mVelocity = tracker.getYVelocity();
            measureCurrentLine();
        }
    }

    private void actionUp(MotionEvent event) {
        releaseVelocityTracker();
        // 2.4s 后发送一个指示器隐藏的请求
//        postDelayed(hideIndicator, 2 * UNITS_SECOND);
        postman.sendEmptyMessageDelayed(MSG_PLAYER_HIDE, 2400);

        if (scrollable()) {
            if (overScrolled() && mScrollY < 0) {
                smoothScrollTo(0);
                return;
            }
            if (overScrolled() && mScrollY > mLineHeight * (mLineCount - 1) + mLineFeedRecord.get(mLineCount - 1) + (mEnableLineFeed ? mTextHeight : 0)) {
                smoothScrollTo(mLineHeight * (mLineCount - 1) + mLineFeedRecord.get(mLineCount - 1) + (mEnableLineFeed ? mTextHeight : 0));
                return;
            }
            if (Math.abs(mVelocity) > THRESHOLD_Y_VELOCITY) {
                doFlingAnimator(mVelocity);
                return;
            }
            if (mShowIndicator && clickPlayer(event)) {
                if (mLineNumberUnderIndicator != mCurrentPlayLine) {
                    mShowIndicator = false;
                    if (mClickListener != null) {
                        setUserTouch(false);// 用户手指离开屏幕，取消触摸标记
                        mClickListener.onPlayerClicked(mLyricInfo.songLines.get(mLineNumberUnderIndicator).start, mLyricInfo.songLines.get(mLineNumberUnderIndicator).content);
                    }
                }
            }
        }
    }

    private String measureCurrentTime() {
        DecimalFormat format = new DecimalFormat("00");
        if (mLyricInfo != null && mLineCount > 0 && mLineNumberUnderIndicator - 1 < mLineCount && mLineNumberUnderIndicator > 0) {
            return format.format(mLyricInfo.songLines.get(mLineNumberUnderIndicator - 1).start / 1000 / 60) + ":" + format.format(mLyricInfo.songLines.get(mLineNumberUnderIndicator - 1).start / 1000 % 60);
        }
        if (mLyricInfo != null && mLineCount > 0 && (mLineNumberUnderIndicator - 1) >= mLineCount) {
            return format.format(mLyricInfo.songLines.get(mLineCount - 1).start / 1000 / 60) + ":" + format.format(mLyricInfo.songLines.get(mLineCount - 1).start / 1000 % 60);
        }
        if (mLyricInfo != null && mLineCount > 0 && mLineNumberUnderIndicator - 1 <= 0) {
            return format.format(mLyricInfo.songLines.get(0).start / 1000 / 60) + ":" + format.format(mLyricInfo.songLines.get(0).start / 1000 % 60);
        }
        return mDefaultTime;
    }

    private void drawIndicator(Canvas canvas) {

        //绘制 播放 按钮
        Path pathPlay = new Path();

        mBtnPlayRect = new Rect(getMeasuredWidth() - bitmap.getWidth(), (int) (getMeasuredHeight() * 0.5f - bitmap.getHeight() * 0.5f)
                , getMeasuredWidth(), (int) (getMeasuredHeight() * 0.5f + bitmap.getHeight() * 0.5f));

        float yCoordinate = mBtnPlayRect.left + (float) Math.sqrt(Math.pow(mBtnPlayRect.width(), 2) - Math.pow(mBtnPlayRect.width() * 0.5f, 2));
        //保留宽度
        float remainWidth = mBtnPlayRect.right - yCoordinate;
//
//        pathPlay.moveTo(mBtnPlayRect.centerX() - mBtnPlayRect.width() * 0.5f, mBtnPlayRect.centerY() - mBtnPlayRect.height() * 0.5f);
//        pathPlay.lineTo(mBtnPlayRect.centerX() - mBtnPlayRect.width() * 0.5f, mBtnPlayRect.centerY() + mBtnPlayRect.height() * 0.5f);
//        pathPlay.lineTo(yCoordinate, mBtnPlayRect.centerY());
//        pathPlay.lineTo(mBtnPlayRect.centerX() - mBtnPlayRect.width() * 0.5f, mBtnPlayRect.centerY() - mBtnPlayRect.height() * 0.5f);

//        canvas.drawPath(pathPlay, mBtnPlayPaint);
        canvas.drawBitmap(bitmap, mBtnPlayRect.left, mBtnPlayRect.top, mBtnPlayPaint);

        //绘制 分割线
        Path pathLine = new Path();
        pathLine.moveTo(mBtnPlayRect.left - getRawSize(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_LINE_MARGIN) - remainWidth, getMeasuredHeight() * 0.5f);
        pathLine.lineTo(mTimerRect.width() + getRawSize(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_TIME_MARGIN_RIGHT) + getRawSize(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_LINE_MARGIN), getHeight() * 0.5f);
        canvas.drawPath(pathLine, mLinePaint);

        //绘制 时间
        canvas.drawText(measureCurrentTime(), mTimerRect.width() + getRawSize(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_TIME_MARGIN_RIGHT), (getHeight() + mTimerRect.height()) * 0.5f, mTimerPaint);
    }

    private boolean clickPlayer(MotionEvent event) {
        if (mBtnPlayRect != null && mDownX > (mBtnPlayRect.left - INDICATOR_ICON_PLAY_MARGIN_LEFT) && mDownX < (mBtnPlayRect.right + INDICATOR_ICON_PLAY_MARGIN_LEFT) && mDownY > (mBtnPlayRect.top - INDICATOR_ICON_PLAY_MARGIN_LEFT) && mDownY < (mBtnPlayRect.bottom + INDICATOR_ICON_PLAY_MARGIN_LEFT)) {
            float upX = event.getX();
            float upY = event.getY();
            return upX > (mBtnPlayRect.left - INDICATOR_ICON_PLAY_MARGIN_LEFT) && upX < (mBtnPlayRect.right + INDICATOR_ICON_PLAY_MARGIN_LEFT) && upY > (mBtnPlayRect.top - INDICATOR_ICON_PLAY_MARGIN_LEFT) && upY < (mBtnPlayRect.bottom + INDICATOR_ICON_PLAY_MARGIN_LEFT);
        }
        return false;
    }


    /**
     * 滑行动画
     *
     * @param velocity 滑动速度
     */
    private void doFlingAnimator(float velocity) {

        float distance = (velocity / Math.abs(velocity) * (Math.abs(velocity) * SLIDE_COEFFICIENT));
        float to = Math.min(Math.max(0, (mScrollY - distance)), (mLineCount - 1) * mLineHeight + mLineFeedRecord.get(mLineCount - 1) + (mEnableLineFeed ? mTextHeight : 0));

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
                mVelocity = 0;
                mFling = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFling = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });

        mFlingAnimator.setDuration(FLING_ANIMATOR_DURATION);
        mFlingAnimator.setInterpolator(new DecelerateInterpolator());
        mFlingAnimator.start();
    }

    /**
     * 设置用户是否触摸的标记
     *
     * @param isUserTouch 标记用户是否触摸屏幕
     */
    private void setUserTouch(boolean isUserTouch) {
        if (isUserTouch) {
            mUserTouch = true;
            mShowIndicator = true;
        } else {
            mUserTouch = false;
            mShowIndicator = false;
        }
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void initMyView(Context context) {
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        initPaint();
        initAllBounds();
    }

    private void initAllBounds() {
        setRawTextSize(mTextSize);

        setLineSpace(mLineSpace);
        measureLineHeight();

        mTimerRect = new Rect();
        mTimerPaint.getTextBounds(mDefaultTime, 0, mDefaultTime.length(), mTimerRect);


    }

    private void initPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        switch (mTextAlign) {
            case LEFT:
                mTextPaint.setTextAlign(Paint.Align.LEFT);
                break;
            case CENTER:
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                break;
            case RIGHT:
                mTextPaint.setTextAlign(Paint.Align.RIGHT);
                break;
        }

        mBtnPlayPaint = new Paint();
        mBtnPlayPaint.setDither(true);
        mBtnPlayPaint.setAntiAlias(true);
        mBtnPlayPaint.setColor(mBtnColor);
        mBtnPlayPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mLinePaint = new Paint();
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setAlpha(64);
        mLinePaint.setStrokeWidth(1.0f);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTimerPaint = new Paint();
        mTimerPaint.setDither(true);
        mTimerPaint.setAntiAlias(true);
        mTimerPaint.setColor(Color.WHITE);
        mTimerPaint.setTextAlign(Paint.Align.RIGHT);
        mTimerPaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP, INDICATOR_TIME_TEXT_SIZE));


    }

    private float measureCurrentScrollY(int line) {
        if (mEnableLineFeed && line > 1) {
            return (line - 1) * mLineHeight + mLineFeedRecord.get(line - 1);
        }
        return (line - 1) * mLineHeight;
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //  当前线程是主UI线程，直接刷新。
            invalidate();
        } else {
            //  当前线程是非UI线程，post刷新。
            postInvalidate();
        }
    }

    private void measureLineHeight() {
        Rect lineBound = new Rect();
        mTextPaint.getTextBounds(mDefaultHint, 0, mDefaultHint.length(), lineBound);
        mTextHeight = lineBound.height();
        mLineHeight = mTextHeight + mLineSpace;
    }

    /**
     * To measure current showing line number based on the view's scroll Y
     */
    private void measureCurrentLine() {
        float baseScrollY = mScrollY + mLineHeight * 0.5f;

        if (mEnableLineFeed && mLyricInfo != null && mLyricInfo.songLines != null) {
            for (int i = mLyricInfo.songLines.size(); i >= 0; i--) {
                if (baseScrollY > measureCurrentScrollY(i) + mLineSpace * 0.2) {
                    mLineNumberUnderIndicator = i - 1;
                    break;
                }
            }
        } else {
            mLineNumberUnderIndicator = (int) (baseScrollY / mLineHeight);
        }
    }

    /**
     * 从当前位置滑动到指定位置上
     *
     * @param toY 指定纵坐标位置
     */
    private void smoothScrollTo(float toY) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mScrollY, toY);
        animator.addUpdateListener(valueAnimator -> {
            mScrollY = (Float) valueAnimator.getAnimatedValue();
            invalidateView();
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mFling = false;
                measureCurrentLine();
                invalidateView();
            }

            @Override
            public void onAnimationStart(Animator animator) {
                mFling = true;
            }
        });
        animator.setDuration(640);
        animator.setInterpolator(new OvershootInterpolator(0.5f));

        animator.start();
    }

    private boolean scrollable() {
        return mLyricInfo != null && mLyricInfo.songLines != null && mLyricInfo.songLines.size() > 0;
    }

    private void scrollToCurrentTimeMillis(long time) {

        int position = 0;
        if (scrollable()) {
            for (int i = 0, size = mLineCount; i < size; i++) {
                LineInfo lineInfo = mLyricInfo.songLines.get(i);
                if (lineInfo != null && lineInfo.start >= time) {
                    if (i + 1 < mLyricInfo.songLines.size() && lineInfo.start == mLyricInfo.songLines.get(i + 1).start) {
                        position = i - 1;
                        break;
                    } else {
                        position = i;
                        break;
                    }
                }
                if (i == mLineCount - 1) {
                    position = mLineCount;
                }
            }
        }
        if (mCurrentPlayLine != position) {
            mCurrentPlayLine = position;
            if (!mFling && !mUserTouch) {
                smoothScrollTo(measureCurrentScrollY(position));
            }
        }
    }


    private void resetLyricInfo() {
        if (mLyricInfo != null) {
            if (mLyricInfo.songLines != null) {
                mLyricInfo.songLines.clear();
                mLyricInfo.songLines = null;
            }
            mLyricInfo = null;
        }
    }

    private void resetView() {
        mCurrentPlayLine = 0;
        resetLyricInfo();
        invalidateView();
        mLineCount = 0;
        mScrollY = 0;
        mEnableLineFeed = false;
        mLineFeedRecord.clear();
        mExtraHeight = 0;
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

    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            measureLineHeight();
            mScrollY = measureCurrentScrollY(mCurrentPlayLine);
            invalidateView();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler postman = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PLAYER_HIDE:
                    postman.sendEmptyMessageDelayed(MSG_PLAYER_SLIDE, 1200);
                    setUserTouch(false);
                    invalidateView();
                case MSG_PLAYER_SLIDE:
                    smoothScrollTo(measureCurrentScrollY(mCurrentPlayLine));
                    invalidateView();
            }
        }
    };
    Runnable hideIndicator = new Runnable() {
        @Override
        public void run() {
            setUserTouch(false);
            invalidateView();
        }
    };

    @IntDef({LEFT, CENTER, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Alignment {
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FloatLyricViewManager.setLyricChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        FloatLyricViewManager.removeLyricChangeListener(this);
    }

    /**
     * 点击事件接口
     */
    public interface OnPlayerClickListener {
        void onPlayerClicked(long progress, String content);
    }

    /**
     * 设置歌词是否可触摸
     *
     * @param touchable true or false
     */
    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    /**
     * 隐藏指示器
     *
     * @param show true or false
     */
    public void setIndicatorShow(boolean show) {
        mShowIndicator = show;
    }

    /**
     * 设置歌词文本内容字体大小
     *
     * @param progress 进度条大小0-100
     */
    public void setTextSize(int progress) {
        setRawTextSize((float) (progress * 0.2 + 35));
    }

    /**
     * 设置高亮显示文本的字体颜色
     *
     * @param color 颜色值
     */
    public void setHighLightTextColor(int color) {
        mHighLightColor = color;
        invalidateView();
    }
}
