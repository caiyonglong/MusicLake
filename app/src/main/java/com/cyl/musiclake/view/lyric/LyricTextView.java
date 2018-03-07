package com.cyl.musiclake.view.lyric;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class LyricTextView extends View {

    private int mDefaultColor = Color.parseColor("#FFFFFF");  // 默认字体颜色
    private int mHighLightColor = Color.parseColor("#ffb701");  // 当前播放位置的颜色

    private int mLineCount = 0;  // 行数
    private float mLineHeight;  // 行高

    private float mScrollY = 0;  // 纵轴偏移量
    private float mShaderWidth = 0;  // 渐变过渡的距离
    private int mCurrentPlayLine = 0;  // 当前播放位置对应的行数

    /***/
    private int mDefaultMargin = 12;
    private int mDefaultSize = 35; //默认歌词大小
    private float fontSizeScale = 16;    // 设置字体大小
    private Rect mBtnBound, mTimerBound;

    private LyricInfo mLyricInfo;
    private String mDefaultTime = "00:00";
    private String mDefaultHint = "音乐湖";
    private Paint mTextPaint, mHighLightPaint;//默认画笔、已读歌词画笔


    private final int MSG_PLAYER_SLIDE = 0x158;
    private final int MSG_PLAYER_HIDE = 0x157;

    private ValueAnimator mFlingAnimator;
    private boolean mPlayable = false;
    private boolean mSliding = false;
    private boolean mTouchable = true;
    /**
     * 是否有歌词
     */
    private boolean blLrc = false;

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

    /**
     * 高亮歌词当前的其实x轴绘制坐标
     **/
    private float highLightLrcMoveX;
    private int progress;

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

    private void init(Context context) {
        this.context = context;

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
        mHighLightPaint.setColor(Color.RED);
    }

    @Override
    public void draw(Canvas canvas) {

        mTextPaint.setTextSize(fontSizeScale);
        mHighLightPaint.setTextSize(fontSizeScale);

        if (!blLrc) {

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
            if (mLyricInfo != null && mLyricInfo.song_lines != null && mLyricInfo.song_lines.size() > 0) {
                content = mLyricInfo.song_lines.get(mCurrentPlayLine).content;

                float tipTextWidth = mTextPaint.measureText(content);
                Paint.FontMetrics fm = mHighLightPaint.getFontMetrics();
                int height = (int) Math.ceil(fm.descent - fm.top) + 2;

                canvas.drawText(content, (getWidth() - tipTextWidth) / 2,
                        (getHeight() + height) / 2, mTextPaint);

                canvas.clipRect((getWidth() - tipTextWidth) / 2,
                        (getHeight() + height) / 2 - height,
                        (getWidth() - tipTextWidth) / 2 + tipTextWidth / 2 + 5,
                        (getHeight() + height) / 2 + height);

                canvas.drawText(content, (getWidth() - tipTextWidth) / 2,
                        (getHeight() + height) / 2, mHighLightPaint);
            }
        }


            /*
            // 画之前的歌词
            if (lyricsLineNum == -1) {
                String lyricsLeft = lyricsLineTreeMap.get(0).getLineLyrics();

                canvas.drawText(lyricsLeft, 10, SCALEIZEWORDDEF + INTERVAL,
                        paint);
                if (lyricsLineNum + 2 < lyricsLineTreeMap.size()) {
                    String lyricsRight = lyricsLineTreeMap.get(
                            lyricsLineNum + 2).getLineLyrics();

                    float lyricsRightWidth = paint.measureText(lyricsRight);
                    float textRightX = getWidth() - lyricsRightWidth - 10;
                    // 如果计算出的textX为负数，将textX置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
                    textRightX = Math.max(textRightX, 10);

                    canvas.drawText(lyricsRight, textRightX,
                            (SCALEIZEWORDDEF + INTERVAL) * 2, paint);
                }
            } else {
                if (lyricsLineNum % 2 == 0) {
                    if (lyricsLineNum + 1 < lyricsLineTreeMap.size()) {
                        String lyricsRight = lyricsLineTreeMap.get(
                                lyricsLineNum + 1).getLineLyrics();

                        float lyricsRightWidth = paint.measureText(lyricsRight);
                        float textRightX = getWidth() - lyricsRightWidth - 10;
                        // 如果计算出的textX为负数，将textX置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
                        textRightX = Math.max(textRightX, 10);
                        canvas.drawText(lyricsRight, textRightX,
                                (SCALEIZEWORDDEF + INTERVAL) * 2, paint);
                    }

                    KscLyricsLineInfo kscLyricsLineInfo = lyricsLineTreeMap
                            .get(lyricsLineNum);
                    // 整行歌词
                    String lineLyrics = kscLyricsLineInfo.getLineLyrics();
                    float textWidth = paint.measureText(lineLyrics);// 用画笔测量歌词的宽度

                    if (lyricsWordIndex != -1) {

                        String lyricsWords[] = kscLyricsLineInfo
                                .getLyricsWords();
                        int wordsDisInterval[] = kscLyricsLineInfo
                                .getWordsDisInterval();
                        // 当前歌词之前的歌词
                        String lyricsBeforeWord = "";
                        for (int i = 0; i < lyricsWordIndex; i++) {
                            lyricsBeforeWord += lyricsWords[i];
                        }
                        // 当前歌词
                        String lyricsNowWord = lyricsWords[lyricsWordIndex]
                                .trim();// 去掉空格
                        // 当前歌词之前的歌词长度
                        float lyricsBeforeWordWidth = paint
                                .measureText(lyricsBeforeWord);

                        // 当前歌词长度
                        float lyricsNowWordWidth = paint
                                .measureText(lyricsNowWord);

                        float len = lyricsNowWordWidth
                                / wordsDisInterval[lyricsWordIndex]
                                * lyricsWordHLEDTime;
                        lineLyricsHLWidth = lyricsBeforeWordWidth + len;
                    } else {
                        // 整行歌词
                        lineLyricsHLWidth = textWidth;
                    }

                    // save和restore是为了剪切操作不影响画布的其它元素
                    canvas.save();

                    float textX = 0;
                    if (textWidth > getWidth()) {
                        if (lineLyricsHLWidth >= getWidth() / 2) {
                            if ((textWidth - lineLyricsHLWidth) >= getWidth() / 2) {
                                highLightLrcMoveX = (getWidth() / 2 - lineLyricsHLWidth);
                            } else {
                                highLightLrcMoveX = getWidth() - textWidth - 10;
                            }
                        } else {
                            highLightLrcMoveX = 10;
                        }
                        // 如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
                        textX = highLightLrcMoveX;
                    } else {
                        // 如果歌词宽度小于view的宽
                        textX = 10;
                    }

                    drawBackground(canvas, lineLyrics, textX, SCALEIZEWORDDEF
                            + INTERVAL);
                    // 画当前歌词
                    canvas.drawText(lineLyrics, textX, SCALEIZEWORDDEF
                            + INTERVAL, paint);

                    FontMetrics fm = paint.getFontMetrics();
                    int height = (int) Math.ceil(fm.descent - fm.top) + 2;
                    canvas.clipRect(textX, INTERVAL, textX + lineLyricsHLWidth,
                            SCALEIZEWORDDEF + INTERVAL + height);
                    // /////////////////////////////////////////////////////////////////////////////////////////

                    drawBackground(canvas, lineLyrics, textX, SCALEIZEWORDDEF
                            + INTERVAL);

                    canvas.drawText(lineLyrics, textX, SCALEIZEWORDDEF
                            + INTERVAL, paintHL);
                    canvas.restore();
                } else {

                    // 画之前的歌词
                    if (lyricsLineNum + 1 != lyricsLineTreeMap.size()) {
                        String lyricsLeft = lyricsLineTreeMap.get(
                                lyricsLineNum + 1).getLineLyrics();

                        drawBackground(canvas, lyricsLeft, 10, SCALEIZEWORDDEF
                                + INTERVAL);

                        canvas.drawText(lyricsLeft, 10, SCALEIZEWORDDEF
                                + INTERVAL, paint);
                    }

                    KscLyricsLineInfo kscLyricsLineInfo = lyricsLineTreeMap
                            .get(lyricsLineNum);
                    // 整行歌词
                    String lineLyrics = kscLyricsLineInfo.getLineLyrics();
                    float lyricsRightWidth = paint.measureText(lineLyrics);

                    if (lyricsWordIndex != -1) {
                        String lyricsWords[] = kscLyricsLineInfo
                                .getLyricsWords();
                        int wordsDisInterval[] = kscLyricsLineInfo
                                .getWordsDisInterval();
                        // 当前歌词之前的歌词
                        String lyricsBeforeWord = "";
                        for (int i = 0; i < lyricsWordIndex; i++) {
                            lyricsBeforeWord += lyricsWords[i];
                        }
                        // 当前歌词
                        String lyricsNowWord = lyricsWords[lyricsWordIndex]
                                .trim();// 去掉空格
                        // 当前歌词之前的歌词长度
                        float lyricsBeforeWordWidth = paint
                                .measureText(lyricsBeforeWord);

                        // 当前歌词长度
                        float lyricsNowWordWidth = paint
                                .measureText(lyricsNowWord);

                        float len = lyricsNowWordWidth
                                / wordsDisInterval[lyricsWordIndex]
                                * lyricsWordHLEDTime;
                        lineLyricsHLWidth = lyricsBeforeWordWidth + len;
                    } else {
                        // 整行歌词
                        lineLyricsHLWidth = lyricsRightWidth;
                    }

                    // save和restore是为了剪切操作不影响画布的其它元素
                    canvas.save();

                    float textX = 0;
                    if (lyricsRightWidth > getWidth()) {
                        if (lineLyricsHLWidth >= getWidth() / 2) {
                            if ((lyricsRightWidth - lineLyricsHLWidth) >= getWidth() / 2) {
                                highLightLrcMoveX = (getWidth() / 2 - lineLyricsHLWidth);
                            } else {
                                highLightLrcMoveX = getWidth()
                                        - lyricsRightWidth - 10;
                            }
                        } else {
                            highLightLrcMoveX = 10;
                        }
                        // 如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
                        textX = highLightLrcMoveX;
                    } else {
                        // 如果歌词宽度小于view的宽
                        textX = getWidth() - lyricsRightWidth - 10;
                    }

                    drawBackground(canvas, lineLyrics, textX,
                            (SCALEIZEWORDDEF + INTERVAL) * 2);
                    // 画当前歌词
                    canvas.drawText(lineLyrics, textX,
                            (SCALEIZEWORDDEF + INTERVAL) * 2, paint);

                    FontMetrics fm = paint.getFontMetrics();
                    int height = (int) Math.ceil(fm.descent - fm.top) + 2;
                    canvas.clipRect(textX, SCALEIZEWORDDEF + INTERVAL * 2,
                            textX + lineLyricsHLWidth, SCALEIZEWORDDEF
                                    + INTERVAL * 2 + height);
                    // /////////////////////////////////////////////////////////////////////////////////////////

                    drawBackground(canvas, lineLyrics, textX,
                            (SCALEIZEWORDDEF + INTERVAL) * 2);
                    canvas.drawText(lineLyrics, textX,
                            (SCALEIZEWORDDEF + INTERVAL) * 2, paintHL);
                    canvas.restore();
                }
            }
        }
        */
        super.draw(canvas);
    }

    public boolean getBlLrc() {
        return blLrc;
    }

    public void setBlLrc(boolean blLrc) {
        this.blLrc = blLrc;
    }

    public void setContent(String content) {
        this.content = content;
        setBlLrc(true);
        invalidate();
    }

    public void setFontSizeScale(float fontSizeScale) {
        this.fontSizeScale = fontSizeScale;
        invalidate();
    }

    /**
     * 显示当前进度的歌词
     *
     * @param playProgress
     */
    public void showLrc(int playProgress) {
        progress = playProgress;
//        int newLyricsLineNum = kscLyricsParser
//                .getLineNumberFromCurPlayingTime(playProgress);
//        if (newLyricsLineNum != lyricsLineNum) {
//            lyricsLineNum = newLyricsLineNum;
//            highLightLrcMoveX = 0;
//        }
//        lyricsWordIndex = kscLyricsParser.getDisWordsIndexFromCurPlayingTime(
//                lyricsLineNum, playProgress);

//        lyricsWordHLEDTime = kscLyricsParser.getLenFromCurPlayingTime(
//                lyricsLineNum, playProgress);
        invalidate();
    }


    /**
     * 设置歌词文件
     *
     * @param lyricInfo 歌词文件
     */
    public void setLyricInfo(LyricInfo lyricInfo) {
        mLyricInfo = lyricInfo;
        if (mLyricInfo != null) {
            blLrc = true;
            mLineCount = mLyricInfo.song_lines.size();
        } else {
            blLrc = false;
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

    //工具
    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }


    /**
     * 根据当前给定的时间戳滑动到指定位置
     *
     * @param time 时间戳
     */
    private void scrollToCurrentTimeMillis(long time) {
        int position = 0;
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
        mCurrentPlayLine = position;
    }


    /**
     * 设置当前时间显示位置
     *
     * @param current 时间戳
     */
    public void setCurrentTimeMillis(long current) {
        scrollToCurrentTimeMillis(current);
        invalidateView();
    }

}
