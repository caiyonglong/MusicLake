package com.cyl.music_hnust.lyric;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cyl.music_hnust.PlayerActivity;
import com.cyl.music_hnust.R;

import java.util.List;

/**
 * 自定义LrcView,可以同步显示歌词，拖动歌词，缩放歌词
 */
public class LyricView extends View implements ILrcView {

    public final static String TAG = "LrcView";

    /**
     * 正常歌词模式
     */
    public final static int DISPLAY_MODE_NORMAL = 0;
    /**
     * 拖动歌词模式
     */
    public final static int DISPLAY_MODE_SEEK = 1;
    /**
     * 缩放歌词模式
     */
    public final static int DISPLAY_MODE_SCALE = 2;
    /**
     * 歌词的当前展示模式
     */
    private int mDisplayMode = DISPLAY_MODE_NORMAL;

    /**
     * 歌词集合，包含所有行的歌词
     */
    private List<LyricItem> mLrcRows;
    /**
     * 最小移动的距离，当拖动歌词时如果小于该距离不做处理
     */
    private int mMinSeekFiredOffset = 10;

    /**
     * 当前高亮歌词的行数
     */
    private int mHignlightRow = 0;
    /**
     * 当前高亮歌词的字体颜色为黄色
     */
    private int mHignlightRowColor = Color.YELLOW;
    /**
     * 不高亮歌词的字体颜色为白色
     */
    private int mNormalRowColor = Color.WHITE;

    /**
     * 拖动歌词时，在当前高亮歌词下面的一条直线的字体颜色
     **/
    private int mSeekLineColor = Color.CYAN;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体颜色
     **/
    private int mSeekLineTextColor = Color.CYAN;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小默认值
     **/
    private int mSeekLineTextSize = 40;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小最小值
     **/
    private int mMinSeekLineTextSize = 25;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小最大值
     **/
    private int mMaxSeekLineTextSize = 50;

    /**
     * 歌词字体大小默认值
     **/
    private int mLrcFontSize = 35;    // font size of lrc
    /**
     * 歌词字体大小最小值
     **/
    private int mMinLrcFontSize = 20;
    /**
     * 歌词字体大小最大值
     **/
    private int mMaxLrcFontSize = 45;

    /**
     * 两行歌词之间的间距
     **/
    private int mPaddingY = 20;
    /**
     * 拖动歌词时，在当前高亮歌词下面的一条直线的起始位置
     **/
    private int mSeekLinePaddingX = 0;

    /**
     * 拖动歌词的监听类，回调LrcViewListener类的onLrcSeeked方法
     **/
    private ILrcViewListener mLrcViewListener;

    /**
     * 当没有歌词的时候展示的内容
     **/
    private String mLoadingLrcTip = "暂无歌词";

    private Paint mPaint;
    private Paint currentPaint;

    private int dunringTime = 0;// 当前句歌词的持续时间
    private int starttime = 0;// 当前句歌词的持续时间
    private int endTime = 0;// 当前句歌词的持续时间

    private int[] paintColorsCurrent = {Color.YELLOW,
            R.color.setting_blue};// 卡拉OK模式绘画中画笔颜色数组

    public LyricView(Context context, AttributeSet attr) {
        super(context, attr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);

        // 高亮部分
        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);
        currentPaint.setColor(Color.argb(250, 251, 248, 29));
        currentPaint.setTextSize(mLrcFontSize);
        currentPaint.setTypeface(Typeface.SERIF);
    }

    public void setListener(ILrcViewListener l) {
        mLrcViewListener = l;
    }

    public void setLoadingTipText(String text) {
        mLoadingLrcTip = text;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight(); // height of this view
        final int width = getWidth(); // width of this view
        //当没有歌词的时候
        if (mLrcRows == null || mLrcRows.size() == 0) {
            if (mLoadingLrcTip != null) {
                // draw tip when no lrc.
                mPaint.setColor(mHignlightRowColor);
                mPaint.setTextSize(mSeekLineTextSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(mLoadingLrcTip, width / 2, height / 2 - mSeekLineTextSize, mPaint);
            }
            return;
        }

        int rowY = 0; // vertical point of each row.
        final int rowX = width / 2;
        int rowNum = 0;
        /**
         * 分以下三步来绘制歌词：
         *
         * 	第1步：高亮地画出正在播放的那句歌词
         *	第2步：画出正在播放的那句歌词的上面可以展示出来的歌词
         *	第3步：画出正在播放的那句歌词的下面的可以展示出来的歌词
         */
        // 1、 高亮地画出正在要高亮的的那句歌词
        String highlightText = mLrcRows.get(mHignlightRow).getLyric();
        int highlightRowY = height / 2 - mSeekLineTextSize;
        currentPaint.setColor(mHignlightRowColor);
        currentPaint.setTextSize(mSeekLineTextSize);
        currentPaint.setTextAlign(Paint.Align.CENTER);


        float len = this.getTextWidth(currentPaint, highlightText);// 该句歌词精确长度
        dunringTime = endTime - starttime;

        float position = dunringTime == 0 ? 0
                : ((float) currentTime - starttime)
                / (float) dunringTime;// 计算当前位置


        float start1 = len / 2;// 第一句的起点位置
        LinearGradient gradient = new LinearGradient(rowX-start1, 0,rowX+start1, 0,
                paintColorsCurrent, new float[] { position, position },
                TileMode.CLAMP);// 重绘渐变
        currentPaint.setShader(gradient);

        canvas.drawText(highlightText, rowX, highlightRowY, currentPaint);

        // 上下拖动歌词的时候 画出拖动要高亮的那句歌词的时间 和 高亮的那句歌词下面的一条直线
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            // 画出高亮的那句歌词下面的一条直线
            mPaint.setColor(mSeekLineColor);
            //该直线的x坐标从0到屏幕宽度  y坐标为高亮歌词和下一行歌词中间
            canvas.drawLine(mSeekLinePaddingX, highlightRowY + mPaddingY, width - mSeekLinePaddingX, highlightRowY + mPaddingY, mPaint);

            // 画出高亮的那句歌词的时间
            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Paint.Align.LEFT);


            canvas.drawText(
                    PlayerActivity.formatTime(mLrcRows.get(mHignlightRow).getTime())
                            + "", 0, highlightRowY, mPaint);
        }


        // 2、画出正在播放的那句歌词的上面可以展示出来的歌词
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        rowNum = mHignlightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;


        //画出正在播放的那句歌词的上面所有的歌词
        while (rowY > -mLrcFontSize && rowNum >= 0) {


            String text = mLrcRows.get(rowNum).getLyric();
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY -= (mPaddingY + mLrcFontSize);
            rowNum--;
            if (mHignlightRow - rowNum >= 5) {
                break;
            }
        }

        // 3、画出正在播放的那句歌词的下面的可以展示出来的歌词
        rowNum = mHignlightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;

        //只画出正在播放的那句歌词的下一句歌词
//        if (rowY < height && rowNum < mLrcRows.size()) {
//            String text2 = mLrcRows.get(rowNum).content;
//            canvas.drawText(text2, rowX, rowY, mPaint);
//        }

        //画出正在播放的那句歌词的所有下面的可以展示出来的歌词
        while (rowY < height && rowNum < mLrcRows.size()) {

            String text = mLrcRows.get(rowNum).getLyric();
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum++;
            if (rowNum - mHignlightRow >= 5) {
                break;
            }
        }

    }

    /**
     * 设置要高亮的歌词为第几行歌词
     *
     * @param position 要高亮的歌词行数
     * @param cb       是否是手指拖动后要高亮的歌词
     */
    public void seekLrc(int position, boolean cb, int time) {
        if (mLrcRows == null || position < 0 || position > mLrcRows.size()) {
            return;
        }
        LyricItem lrcRow = mLrcRows.get(position);
        if (lrcRow.getLyric().length() > 0) {
            mHignlightRow = position;
        }
        if (position ==mLrcRows.size()-1)
        {
            endTime =duration;
        }else if (position <mLrcRows.size()-1) {
            endTime = mLrcRows.get(position + 1).getTime();
        }
        starttime = lrcRow.getTime();
        //	mHignlightRow = position;
        currentTime = time;
        invalidate();
        //如果是手指拖动歌词后
        if (mLrcViewListener != null && cb) {
            //回调onLrcSeeked方法，将音乐播放器播放的位置移动到高亮歌词的位置
            mLrcViewListener.onLrcSeeked(position, lrcRow);
        }
    }

    private int currentTime = 0;

    private float mLastMotionY;
    /**
     * 第一个手指的坐标
     **/
    private PointF mPointerOneLastMotion = new PointF();
    /**
     * 第二个手指的坐标
     **/
    private PointF mPointerTwoLastMotion = new PointF();
    /**
     * 是否是第一次移动，当一个手指按下后开始移动的时候，设置为true,
     * 当第二个手指按下的时候，即两个手指同时移动的时候，设置为false
     */
    private boolean mIsFirstMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            //手指按下
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "down,mLastMotionY:" + mLastMotionY);
                mLastMotionY = event.getY();
                mIsFirstMove = true;
                invalidate();
                break;
            //手指移动
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    Log.d(TAG, "two move");
                    doScale(event);
                    return true;
                }
                Log.d(TAG, "one move");
                // single pointer mode ,seek
                //如果是双指同时按下，进行歌词大小缩放，抬起其中一个手指，另外一个手指不离开屏幕地移动的话，不做任何处理
                if (mDisplayMode == DISPLAY_MODE_SCALE) {
                    //if scaling but pointer become not two ,do nothing.
                    return true;
                }
                //如果一个手指按下，在屏幕上移动的话，拖动歌词上下
                doSeek(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                //手指抬起
            case MotionEvent.ACTION_UP:
                if (mDisplayMode == DISPLAY_MODE_SEEK) {
                    //高亮手指抬起时的歌词并播放从该句歌词开始播放
                    seekLrc(mHignlightRow, true, 0);
                }
                mDisplayMode = DISPLAY_MODE_NORMAL;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 处理双指在屏幕移动时的，歌词大小缩放
     */
    private void doScale(MotionEvent event) {
        //如果歌词的模式为：拖动歌词模式
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            //如果是单指按下，在进行歌词上下滚动，然后按下另外一个手指，则把歌词模式从 拖动歌词模式 变为 缩放歌词模式
            mDisplayMode = DISPLAY_MODE_SCALE;
            Log.d(TAG, "change mode from DISPLAY_MODE_SEEK to DISPLAY_MODE_SCALE");
            return;
        }
        // two pointer mode , scale font
        if (mIsFirstMove) {
            mDisplayMode = DISPLAY_MODE_SCALE;
            invalidate();
            mIsFirstMove = false;
            //两个手指的x坐标和y坐标
            setTwoPointerLocation(event);
        }
        //获取歌词大小要缩放的比例
        int scaleSize = getScale(event);
        Log.d(TAG, "scaleSize:" + scaleSize);
        //如果缩放大小不等于0，进行缩放，重绘LrcView
        if (scaleSize != 0) {
            setNewFontSize(scaleSize);
            invalidate();
        }
        setTwoPointerLocation(event);
    }

    /**
     * 处理单指在屏幕移动时，歌词上下滚动
     */
    private void doSeek(MotionEvent event) {
        float y = event.getY();//手指当前位置的y坐标
        float offsetY = y - mLastMotionY; //第一次按下的y坐标和目前移动手指位置的y坐标之差
        //如果移动距离小于10，不做任何处理
        if (Math.abs(offsetY) < mMinSeekFiredOffset) {
            return;
        }
        //将模式设置为拖动歌词模式
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = Math.abs((int) offsetY / mLrcFontSize); //歌词要滚动的行数

        Log.d(TAG, "move to new hightlightrow : " + mHignlightRow + " offsetY: " + offsetY + " rowOffset:" + rowOffset);

        if (offsetY < 0) {
            //手指向上移动，歌词向下滚动
            mHignlightRow += rowOffset;//设置要高亮的歌词为 当前高亮歌词 向下滚动rowOffset行后的歌词
        } else if (offsetY > 0) {
            //手指向下移动，歌词向上滚动
            mHignlightRow -= rowOffset;//设置要高亮的歌词为 当前高亮歌词 向上滚动rowOffset行后的歌词
        }
        //设置要高亮的歌词为0和mHignlightRow中的较大值，即如果mHignlightRow < 0，mHignlightRow=0
        mHignlightRow = Math.max(0, mHignlightRow);
        //设置要高亮的歌词为0和mHignlightRow中的较小值，即如果mHignlight > RowmLrcRows.size()-1，mHignlightRow=mLrcRows.size()-1
        mHignlightRow = Math.min(mHignlightRow, mLrcRows.size() - 1);
        //如果歌词要滚动的行数大于0，则重画LrcView
        if (rowOffset > 0) {
            mLastMotionY = y;
            invalidate();
        }
    }

    /**
     * 设置当前两个手指的x坐标和y坐标
     */
    private void setTwoPointerLocation(MotionEvent event) {
        mPointerOneLastMotion.x = event.getX(0);
        mPointerOneLastMotion.y = event.getY(0);
        mPointerTwoLastMotion.x = event.getX(1);
        mPointerTwoLastMotion.y = event.getY(1);
    }

    /**
     * 设置缩放后的字体大小
     */
    private void setNewFontSize(int scaleSize) {
        //设置歌词缩放后的的最新字体大小
        mLrcFontSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize, mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize, mMaxLrcFontSize);

        //设置显示高亮的那句歌词的时间最新字体大小
        mSeekLineTextSize += scaleSize;
        mSeekLineTextSize = Math.max(mSeekLineTextSize, mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize, mMaxSeekLineTextSize);
    }

    /**
     * 精确计算文字宽度
     *
     * @param paint
     * @param str
     * @return
     */
    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 获取歌词大小要缩放的比例
     */
    private int getScale(MotionEvent event) {
        Log.d(TAG, "scaleSize getScale");
        float x0 = event.getX(0);
        float y0 = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);

        float maxOffset = 0; // max offset between x or y axis,used to decide scale size

        boolean zoomin = false;
        //第一次双指之间的x坐标的差距
        float oldXOffset = Math.abs(mPointerOneLastMotion.x - mPointerTwoLastMotion.x);
        //第二次双指之间的x坐标的差距
        float newXoffset = Math.abs(x1 - x0);

        //第一次双指之间的y坐标的差距
        float oldYOffset = Math.abs(mPointerOneLastMotion.y - mPointerTwoLastMotion.y);
        //第二次双指之间的y坐标的差距
        float newYoffset = Math.abs(y1 - y0);

        //双指移动之后，判断双指之间移动的最大差距
        maxOffset = Math.max(Math.abs(newXoffset - oldXOffset), Math.abs(newYoffset - oldYOffset));
        //如果x坐标移动的多一些
        if (maxOffset == Math.abs(newXoffset - oldXOffset)) {
            //如果第二次双指之间的x坐标的差距大于第一次双指之间的x坐标的差距则是放大，反之则缩小
            zoomin = newXoffset > oldXOffset ? true : false;
        }
        //如果y坐标移动的多一些
        else {
            //如果第二次双指之间的y坐标的差距大于第一次双指之间的y坐标的差距则是放大，反之则缩小
            zoomin = newYoffset > oldYOffset ? true : false;
        }
        Log.d(TAG, "scaleSize maxOffset:" + maxOffset);
        if (zoomin) {
            return (int) (maxOffset / 10);//放大双指之间移动的最大差距的1/10
        } else {
            return -(int) (maxOffset / 10);//缩小双指之间移动的最大差距的1/10
        }
    }

    /**
     * 设置歌词行集合
     *
     * @param lrcRows
     */
    public void setLrc(List<LyricItem> lrcRows) {
        invalidate();
        mLrcRows = lrcRows;
        mHignlightRow = 0;
    }

    /**
     * 播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     *
     * @param time
     */
    int duration =0;
    public void seekLrcToTime(long time,long duration) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }
        if (mDisplayMode != DISPLAY_MODE_NORMAL) {
            return;
        }
        duration =duration;
        for (int i = 0; i < mLrcRows.size(); i++) {

            LyricItem current = mLrcRows.get(i);
            LyricItem next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i + 1);
            /**
             *  正在播放的时间大于current行的歌词的时间而小于next行歌词的时间， 设置要高亮的行为current行
             *  正在播放的时间大于current行的歌词，而current行为最后一句歌词时，设置要高亮的行为current行
             */
            if ((time >= current.getTime() && next != null && time < next.getTime())
                    || (time > current.getTime() && next == null)) {
                seekLrc(i, false, (int) time);
                return;
            }
        }
    }

}
