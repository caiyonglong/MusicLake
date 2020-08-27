package com.cyl.musiclake.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.SizeUtils;

/**
 * 自定义暂停播放按钮，包括自定义进度条
 * 1、自定义圆环进度条
 * 2、中间状态播放监听
 */
public class PlayPauseView extends View {

    private int mWidth; //View宽度
    private int mHeight; //View高度
    private Paint mPaint;
    private Paint mRingPaint; //圆弧
    private Paint mProgressPaint; //圆弧
    private Path mLeftPath; //暂停时左侧竖条Path
    private Path mRightPath; //暂停时右侧竖条Path
    private float mBorderWidth; //两个暂停竖条中间的空隙,默认为两侧竖条的宽度
    private float mGapWidth; //两个暂停竖条中间的空隙,默认为两侧竖条的宽度
    private float mProgress; //动画Progress
    private Rect mRect;
    private RectF mRingRect;
    private RectF mProgressRect;
    private boolean isPlaying;
    private boolean isLoading;
    private boolean hasProgress;
    private float startAngle, sweepAngle, newAngle;
    private float mRectWidth;  //圆内矩形宽度
    private float mRectHeight; //圆内矩形高度
    private int mRectLT;  //矩形左侧上侧坐标
    private int mRadius;  //圆的半径
    private int mBgColor = Color.WHITE;
    private int mBtnColor = Color.BLACK;
    private int mLoadingColor;
    private int mDirection = Direction.POSITIVE.value;
    private float mPadding;
    private int mAnimDuration = 200;//动画时间

    public PlayPauseView(Context context) {
        super(context);
    }

    public PlayPauseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PlayPauseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayPauseView);
        mBgColor = ta.getColor(R.styleable.PlayPauseView_bg_color, Color.WHITE);
        mBtnColor = ta.getColor(R.styleable.PlayPauseView_btn_color, Color.BLACK);
        mGapWidth = SizeUtils.dp2px(context, ta.getDimension(R.styleable.PlayPauseView_gap_width, 0));
        mBorderWidth = SizeUtils.dp2px(context, ta.getDimension(R.styleable.PlayPauseView_border_width, 2));
        mDirection = ta.getInt(R.styleable.PlayPauseView_anim_direction, Direction.POSITIVE.value);
        mPadding = ta.getFloat(R.styleable.PlayPauseView_space_padding, 0);
        mAnimDuration = ta.getInt(R.styleable.PlayPauseView_anim_duration, 200);
        hasProgress = ta.getBoolean(R.styleable.PlayPauseView_hasProgress, false);
        mLoadingColor = ta.getColor(R.styleable.PlayPauseView_loadingColor, Color.parseColor("#e91e63"));
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mLoadingColor);
        mRingPaint.setStrokeWidth(mBorderWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);

//        mRingPaint.setStrokeCap(Paint.Cap.ROUND);
//        mRingPaint.setStrokeJoin(Paint.Join.ROUND);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor("#809E9E9E"));
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mLeftPath = new Path();
        mRightPath = new Path();
        mRect = new Rect();
        mRingRect = new RectF();
        mProgressRect = new RectF();
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                mWidth = mHeight = Math.min(mWidth, mHeight);
                setMeasuredDimension(mWidth, mHeight);
                break;
            case MeasureSpec.AT_MOST:
                float density = getResources().getDisplayMetrics().density;
                mWidth = mHeight = (int) (50 * density); //默认50dp
                setMeasuredDimension(mWidth, mHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mHeight = w;
        initValue();
    }

    private void initValue() {
//        int rectLT = (int) (mWidth / 2 - radius / Math.sqrt(2));
//        int rectRB = (int) (mWidth / 2 + radius / Math.sqrt(2));
        mRadius = mWidth / 2;
        /* if (getPadding() > mRadius / Math.sqrt(2) || mPadding < 0) {
         *//*throw new IllegalArgumentException("The value of your padding is too large. " +
                    "The value must not be greater than " + (int) (mRadius / Math.sqrt(2)));*//*
        }*/
        mPadding = getSpacePadding() == 0 ? mRadius / 3f : getSpacePadding();
        if (getSpacePadding() > mRadius / Math.sqrt(2) || mPadding < 0) {
            mPadding = mRadius / 3f; //默认值
        }
        float space = (float) (mRadius / Math.sqrt(2) - mPadding); //矩形宽高的一半
        mRectLT = (int) (mRadius - space);
        int rectRB = (int) (mRadius + space);
        mRect.top = mRectLT;
        mRect.bottom = rectRB;
        mRect.left = mRectLT;
        mRect.right = rectRB;
        mRingRect.top = mBorderWidth / 4;
        mRingRect.bottom = mWidth - mBorderWidth / 4;
        mRingRect.left = mBorderWidth / 4;
        mRingRect.right = mWidth - mBorderWidth / 4;

        mProgressRect.top = mBorderWidth / 4;
        mProgressRect.bottom = mWidth - mBorderWidth / 4;
        mProgressRect.left = mBorderWidth / 4;
        mProgressRect.right = mWidth - mBorderWidth / 4;

        //        mRectWidth = mRect.width();
//        mRectHeight = mRect.height();
        mRectWidth = 2 * space + 2; //改为float类型，否则动画有抖动。并增加一像素防止三角形之间有缝隙
        mRectHeight = 2 * space + 2;
        mGapWidth = getGapWidth() != 0 ? getGapWidth() : mRectWidth / 3;
        mProgress = isPlaying ? 0 : 1;
        mAnimDuration = getAnimDuration() < 0 ? 200 : getAnimDuration();
        startAngle = -90;
        sweepAngle = 120;
        mRingPaint.setStrokeWidth(mBorderWidth / 2);
        mProgressPaint.setStrokeWidth(mBorderWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLeftPath.rewind();
        mRightPath.rewind();

//        mPaint.setStrokeWidth(1);
//        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mBgColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);

        //是否显示进度条
        if (hasProgress) {
            canvas.drawArc(mProgressRect, -90, 360, false, mProgressPaint); //
        }

        if (mBorderWidth > 0) {
            //周边底部颜色 一般为白色
            //设置了周边弧度的宽度 每次重新绘制都要画上边上的弧度
            if (isLoading) {
                canvas.drawArc(mRingRect, startAngle, sweepAngle, false, mRingPaint); //
            } else {
                canvas.drawArc(mRingRect, -90, newAngle, false, mRingPaint); //
            }
        }

        float distance = mGapWidth * (1 - mProgress);  //暂停时左右两边矩形距离
        float barWidth = mRectWidth / 2 - distance / 2;     //一个矩形的宽度
        float leftLeftTop = barWidth * mProgress;       //左边矩形左上角

        float rightLeftTop = barWidth + distance;       //右边矩形左上角
        float rightRightTop = 2 * barWidth + distance;  //右边矩形右上角
        float rightRightBottom = rightRightTop - barWidth * mProgress; //右边矩形右下角

        mPaint.setColor(mBtnColor);

        if (mDirection == Direction.NEGATIVE.value) {
            mLeftPath.moveTo(mRectLT, mRectLT);
            mLeftPath.lineTo(leftLeftTop + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectLT);
            mLeftPath.close();

            mRightPath.moveTo(rightLeftTop + mRectLT, mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightBottom + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightTop + mRectLT, mRectLT);
            mRightPath.close();
        } else {
            mLeftPath.moveTo(leftLeftTop + mRectLT, mRectLT);
            mLeftPath.lineTo(mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectLT);
            mLeftPath.close();

            mRightPath.moveTo(rightLeftTop + mRectLT, mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT + barWidth, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightBottom + mRectLT, mRectLT);
            mRightPath.close();
        }

        canvas.save();

        canvas.translate(mRectHeight / 8f * mProgress, 0);

        float progress = isPlaying ? (1 - mProgress) : mProgress;
        int corner = mDirection == Direction.NEGATIVE.value ? -90 : 90;
        float rotation = isPlaying ? corner * (1 + progress) : corner * progress;
        canvas.rotate(rotation, mWidth / 2f, mHeight / 2f);

        canvas.drawPath(mLeftPath, mPaint);
        canvas.drawPath(mRightPath, mPaint);

        canvas.restore();
    }


    /**
     * 显示Loading 动画
     *
     * @return
     */
    public ValueAnimator getLoadingAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 360f);
        valueAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        valueAnimator.setRepeatMode(ObjectAnimator.RESTART);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            if (sweepAngle >= 90) {
                startAngle++;
                if (startAngle >= 360) {
                    startAngle = 0;
                }
            }
            invalidate();
        });
        return valueAnimator;
    }


    public ValueAnimator getPlayPauseAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(isPlaying ? 1 : 0, isPlaying ? 0 : 1);
        valueAnimator.setDuration(mAnimDuration);
        valueAnimator.addUpdateListener(animation -> {
            mProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        return valueAnimator;
    }

    /**
     * 开始Loading
     */
    public void startLoading() {
        if (getLoadingAnim() != null) {
            getLoadingAnim().cancel();
        }
        getLoadingAnim().start();
    }

    /**
     * 停止Loading
     */
    public void stopLoading() {
        if (getLoadingAnim() != null) {
            getLoadingAnim().cancel();
        }
    }

    public void setProgress(float progress) {
        this.newAngle = (int) (progress * 360);
        if (isLoading && progress > 0) {
            isLoading = false;
        }
//        LogUtil.d("playPauseView ", "newAngle =" + newAngle);
        postInvalidate();
    }

    public void play() {
        if (getPlayPauseAnim() != null) {
            getPlayPauseAnim().cancel();
        }
        setPlaying(true);
        getPlayPauseAnim().start();
    }

    public void pause() {
        if (getPlayPauseAnim() != null) {
            getPlayPauseAnim().cancel();
        }
        setPlaying(false);
        getPlayPauseAnim().start();
    }

    private PlayPauseListener mPlayPauseListener;

    public void setPlayPauseListener(PlayPauseListener playPauseListener) {
        mPlayPauseListener = playPauseListener;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying()) {
                    pause();
                    if (null != mPlayPauseListener) {
                        mPlayPauseListener.pause();
                    }
                } else {
                    play();
                    if (null != mPlayPauseListener) {
                        mPlayPauseListener.play();
                    }
                }
            }
        });
    }

    public interface PlayPauseListener {
        void play();

        void pause();
    }

    /* ------------下方是参数------------- */

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setGapWidth(int gapWidth) {
        mGapWidth = gapWidth;
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public int getBtnColor() {
        return mBtnColor;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public void setBtnColor(int btnColor) {
        mBtnColor = btnColor;
        invalidate();
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            startLoading();
        } else {
            stopLoading();
        }
        invalidate();
    }

    public void setDirection(Direction direction) {
        mDirection = direction.value;
    }

    public float getSpacePadding() {
        return mPadding;
    }

    public void setSpacePadding(float padding) {
        mPadding = padding;
    }

    public int getAnimDuration() {
        return mAnimDuration;
    }

    public void setAnimDuration(int animDuration) {
        mAnimDuration = animDuration;
    }

    public enum Direction {
        POSITIVE(1),//顺时针
        NEGATIVE(2);//逆时针
        int value;

        Direction(int value) {
            this.value = value;
        }
    }
}