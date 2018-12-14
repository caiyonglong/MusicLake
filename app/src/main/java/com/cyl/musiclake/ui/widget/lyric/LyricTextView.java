package com.cyl.musiclake.ui.widget.lyric;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.LogUtil;

public class LyricTextView extends View {

    private static final String TAG = "LyricTextView";

    private int mLineCount = 0;  // 行数
    private float mLineHeight;  // 行高

    private float mShaderWidth = 0;  // 渐变过渡的距离
    private int mCurrentPlayLine = 0;  // 当前播放位置对应的行数

    /***/
    private int mDefaultMargin = 12;
    private int mDefaultSize = 35; //默认歌词大小
    private int lyricMaxWidth = 0; //默认歌词大小
    private float fontSize = 16;    // 设置字体大小
    private int fontColor = Color.RED;    // 设置字体颜色

    private LyricInfo mLyricInfo;
    private String mDefaultHint = "音乐湖";
    private Paint mTextPaint, mHighLightPaint;//默认画笔、已读歌词画笔

    /**
     * 是否有歌词
     */
    private boolean hasLyric = false;

    /**
     * 当前歌词的第几个字
     */
    private int lyricsWordIndex = -1;

    /**
     * 当前歌词第几个字 已经播放的时间
     */
    private int lyricsWordHLEDTime = 0;

    /**
     * 当前歌词第几个字 已经播放的长度
     */
    private float lineLyricsHLWidth = 0;

    private Context context;

    private String content;
    private String nextContent;
    private String lightLyric;
    private long mStartMillis, mCurrentMillis, mEndMillis, mDuration;


    public LyricTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public LyricTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LyricTextView(Context context) {
        super(context, null);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void init(Context context) {
        this.context = context;


        //获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int screensWidth = displayMetrics.widthPixels;

        //设置歌词的最大宽度
        int textMaxWidth = screensWidth / 7 * 6;
        setTextMaxWidth(textMaxWidth);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setColor(Color.WHITE);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        mTextPaint.setShadowLayer(5, 3, 3, 0xb5000000);

        mHighLightPaint = new Paint();
        mHighLightPaint.setDither(true);
        mHighLightPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTextPaint.setTextSize(fontSize);
        mHighLightPaint.setTextSize(fontSize);
        mHighLightPaint.setColor(fontColor);
        if (!hasLyric) {
            float tipTextWidth = mTextPaint.measureText(mDefaultHint);
            Paint.FontMetrics fm = mHighLightPaint.getFontMetrics();
            int height = (int) Math.ceil(fm.descent - fm.top) + 2;

            canvas.drawText(mDefaultHint, (getWidth() - tipTextWidth) / 2,
                    (getHeight() + height) / 2, mTextPaint);

            canvas.clipRect((getWidth() - tipTextWidth) / 2,
                    (getHeight() + height) / 2 - height,
                    (float) ((getWidth() - tipTextWidth) / 2 + tipTextWidth / 2 + 5),
                    (getHeight() + height) / 2 + height);

            canvas.drawText(mDefaultHint, (getWidth() - tipTextWidth) / 2,
                    (getHeight() + height) / 2, mHighLightPaint);

        } else {
            LogUtil.e("tmp =  " + mCurrentPlayLine + "-" + mStartMillis + "==" + mEndMillis + "==" + content + " length = " + mLyricInfo.songLines.size());

            if (mLyricInfo != null && mLyricInfo.songLines != null && mLyricInfo.songLines.size() > 0) {
                mStartMillis = mLyricInfo.songLines.get(mCurrentPlayLine).start;
                content = mLyricInfo.songLines.get(mCurrentPlayLine).content;
                if (mCurrentPlayLine >= mLyricInfo.songLines.size() - 1) {
                    mEndMillis = PlayManager.getDuration();
                    nextContent = "";
                } else {
                    mEndMillis = mLyricInfo.songLines.get(mCurrentPlayLine + 1).start;
                    nextContent = mLyricInfo.songLines.get(mCurrentPlayLine + 1).content;
                }
                lightLyric = content;

                LogUtil.e("tmp =  " + mCurrentPlayLine + "-" + mStartMillis + "==" + mEndMillis + "==" + content + " length = " + content.length());
                LogUtil.e("tmp =  " + mCurrentPlayLine + "-" + mStartMillis + "==" + mEndMillis + "==" + nextContent + " length = " + content.length());

                if (mEndMillis > mStartMillis) {
                    float tipTextWidth = mTextPaint.measureText(content);
                    Paint.FontMetrics fm = mHighLightPaint.getFontMetrics();
                    int height = (int) Math.ceil(fm.descent - fm.top) + 2;
                    mShaderWidth = (float) (1.0 * (mCurrentMillis - mStartMillis) / (mEndMillis - mStartMillis)) * tipTextWidth;

                    if (mCurrentPlayLine % 2 == 0) {
                        //绘制第一行
                        canvas.drawText(content, (getWidth() - tipTextWidth) / 2,
                                (getHeight() + height) / 4, mTextPaint);

                        //绘制第二行
                        canvas.drawText(nextContent, (getWidth() - mTextPaint.measureText(nextContent)) / 2,
                                (getHeight() + height) / 2, mTextPaint);

                        //绘制高亮部分
                        canvas.clipRect((getWidth() - tipTextWidth) / 2,
                                (getHeight() + height) / 4 - height,
                                (getWidth() - tipTextWidth) / 2 + mShaderWidth,
                                (getHeight() + height) / 4 + height);

                        canvas.drawText(lightLyric, (getWidth() - mTextPaint.measureText(lightLyric)) / 2,
                                (getHeight() + height) / 4, mHighLightPaint);

                    } else {
                        //绘制第二行
                        canvas.drawText(nextContent, (getWidth() - mTextPaint.measureText(nextContent)) / 2,
                                (getHeight() + height) / 4, mTextPaint);

                        //绘制第一行
                        canvas.drawText(content, (getWidth() - tipTextWidth) / 2,
                                (getHeight() + height) / 2, mTextPaint);

                        //绘制高亮部分
                        canvas.clipRect((getWidth() - tipTextWidth) / 2,
                                (getHeight() + height) / 2 - height,
                                (getWidth() - tipTextWidth) / 2 + mShaderWidth,
                                (getHeight() + height) / 2 + height);

                        canvas.drawText(lightLyric, (getWidth() - mTextPaint.measureText(lightLyric)) / 2,
                                (getHeight() + height) / 2, mHighLightPaint);
                    }


                } else if (mCurrentPlayLine > 0) {
                    content = mLyricInfo.getSongLines().get(mCurrentPlayLine - 1).content;
                    float tipTextWidth = mTextPaint.measureText(content);
                    Paint.FontMetrics fm = mHighLightPaint.getFontMetrics();
                    int height = (int) Math.ceil(fm.descent - fm.top) + 2;
                    canvas.drawText(content, (getWidth() - tipTextWidth) / 2,
                            (getHeight() + height) / 2, mHighLightPaint);
                }
            }
        }
    }

    public boolean getBlLrc() {
        return hasLyric;
    }

    public void setBlLrc(boolean hasLyric) {
        this.hasLyric = hasLyric;
        invalidateView();
    }

    public void setContent(String content) {
        this.content = content;
        if (content != null) {
            setBlLrc(true);
        }
        invalidateView();
    }

    public void setFontColorScale(int fontColorScale) {
        this.fontColor = fontColorScale;
        invalidateView();
    }

    public void setFontSizeScale(float progress) {
        this.fontSize = (float) (mDefaultSize + progress * 0.2);
        invalidateView();
    }

    /**
     * 设置歌词文件
     *
     * @param lyricInfo 歌词文件
     */
    public void setLyricInfo(LyricInfo lyricInfo) {
        if (lyricInfo != null) {
            mLyricInfo = lyricInfo;
            hasLyric = true;
            mLineCount = mLyricInfo.songLines.size();
            LogUtil.e(TAG, mLineCount + "===" + mLyricInfo.songLines.toString());
        } else {
            hasLyric = false;
            mDefaultHint = "音乐湖，暂无歌词";
        }
        invalidateView();
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
     * 根据当前给定的时间戳滑动到指定位置
     *
     * @param time 时间戳
     */
    private void scrollToCurrentTimeMillis(long time) {
        int position = 0;
        for (int i = 0, size = mLineCount; i < size; i++) {
            LyricInfo.LineInfo lineInfo = mLyricInfo.songLines.get(i);
            if (lineInfo != null && lineInfo.start > time) {
                position = i;
                break;
            }
            if (i == mLineCount - 1) {
                position = mLineCount;
            }
        }
        if (position > 0) {
            mCurrentPlayLine = position - 1;
        } else {
            mCurrentPlayLine = position;
        }
    }

    /**
     * 设置当前时间显示位置
     *
     * @param current 时间戳
     */
    public void setCurrentTimeMillis(long current) {
        if (mLyricInfo == null) return;
        mCurrentMillis = current;
        scrollToCurrentTimeMillis(current);
        invalidateView();
    }

    /**
     * 设置当前时间显示位置
     *
     * @param current 时间戳
     */
    public void setDurationMillis(long current) {
        if (current == 0) return;
        mDuration = current;
    }

    /**
     * 设置文本最大宽度，过长则滚动显示
     *
     * @param textMaxWidth
     */
    private void setTextMaxWidth(int textMaxWidth) {
        this.lyricMaxWidth = textMaxWidth;
    }


}
