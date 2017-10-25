package com.cyl.music_hnust.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.SizeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义LrcView,可以同步显示歌词，拖动歌词
 * v2.5 对自定义控件添加自定义属性 defStyleAttr
 */
public class LrcView extends View {

    public final static String TAG = "LrcView";
    public final static int DISPLAY_MODE_NORMAL = 0; //正常歌词模式
    public final static int DISPLAY_MODE_SEEK = 1;  // 拖动歌词模式
    private int mDisplayMode = DISPLAY_MODE_NORMAL; //当前模式
    private List<LineInfo> mLineInfos; //歌词集合，包含所有行的歌词
    private int mMinSeekFiredOffset = 10; //最小移动的距离，当拖动歌词时如果小于该距离不做处理

    private int mCurrentLine = 0; //当前行

    private float mTextSize;  //歌词字体大小默认值
    private float mDividerHeight;//间距
    private int mAnimationDuration; //动画周期

    private float mAnimOffset;//偏移


    private int mSeekLinePaddingX = 0;//拖动歌词时，在当前高亮歌词下面的一条直线的起始位置

    private ILrcViewListener mLrcViewListener;  //拖动歌词的监听类，回调LrcViewListener类的onLrcSeeked方法
    private String mLoadingText = "暂无歌词";  //当没有歌词的时候展示的内容
    private long mNextTime = 0L;

    private boolean isEnd = false;

    private Paint mNormalPaint;
    private Paint mCurrentPaint;

    private int dunringTime = 0;// 当前句歌词的持续时间
    private int starttime = 0;// 当前句歌词的持续时间
    private int endTime = 0;// 当前句歌词的持续时间


    private Context mContext;


    public LrcView(Context context) {
        super(context);
        this.mContext = context;
    }

    public LrcView(Context context, AttributeSet attr) {
        this(context, attr, 0);
        this.mContext = context;

    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LrcView);
        mTextSize = ta.getDimension(R.styleable.LrcView_textSize, SizeUtils.sp2px(mContext, 16));
        mDividerHeight = ta.getDimension(R.styleable.LrcView_dividerHeight, SizeUtils.dp2px(mContext, 24));
        mAnimationDuration = ta.getInt(R.styleable.LrcView_animationDuration, 1000);
        mAnimationDuration = mAnimationDuration < 0 ? 1000 : mAnimationDuration;
        int normalColor = ta.getColor(R.styleable.LrcView_normalTextColor, 0xFFFFFFFF);
        int currentColor = ta.getColor(R.styleable.LrcView_currentTextColor, 0xFFFF4081);
        ta.recycle();

        mLineInfos = new ArrayList<>();

        mNormalPaint = new Paint();
        mCurrentPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setColor(normalColor);
        mNormalPaint.setTextSize(mTextSize);

        mCurrentPaint.setAntiAlias(true);
        mCurrentPaint.setColor(currentColor);
        mCurrentPaint.setTextSize(mTextSize);

    }

    public void setListener(ILrcViewListener l) {
        mLrcViewListener = l;
    }

    public void setLoadingTipText(String text) {
        mLoadingText = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 中心Y坐标
        float centerY = getHeight() / 2 + mTextSize / 2 + mAnimOffset;
        //当没有歌词的时候
        if (mLineInfos == null || mLineInfos.size() == 0) {
            float centerX = (getWidth() - mCurrentPaint.measureText(mLoadingText)) / 2;
            mCurrentPaint.setUnderlineText(true);
            canvas.drawText(mLoadingText, centerX, centerY, mCurrentPaint);
            return;
        } else {
            mCurrentPaint.setUnderlineText(false);
        }
        // 当前文本
        String mCurrentText = "";
        // 上下拖动歌词的时候 画出拖动要高亮的那句歌词的时间 和 高亮的那句歌词下面的一条直线
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            // 画出中间的那一条直线和时间
            String time = FormatUtil.formatTime(mLineInfos.get(mCurrentLine).getTime());
            float[] ptx = {
                    0, centerY, getWidth() / 2 - 50, centerY,
                    getWidth() / 2 + 50, centerY, getWidth() - mNormalPaint.measureText(time) - 5, centerY
            };
            canvas.drawLines(ptx, mNormalPaint);

            canvas.drawText(time, getWidth() - mNormalPaint.measureText(time) - 5, centerY, mNormalPaint);
        }

        mCurrentText = mLineInfos.get(mCurrentLine).getText();


        /**
         * 分以下三步来绘制歌词：
         *
         * 	第1步：1.高亮画出当前歌词
         *	第2步：2.画当前行上面的
         *	第3步：3.画当前行下面的
         */
        // 1.高亮画出当前歌词

        float rowX = (getWidth() - mCurrentPaint.measureText(mCurrentText)) / 2;
        canvas.drawText(mCurrentText, rowX, centerY, mCurrentPaint);
//        float len = this.getTextWidth(mCurrentPaint, highlightText);// 该句歌词精确长度
//        dunringTime = endTime - starttime;
//
//        float position = dunringTime == 0 ? 0
//                : ((float) currentTime - starttime)
//                / (float) dunringTime;// 计算当前位置
//
//
//        float start1 = len / 2;// 第一句的起点位置
//        LinearGradient gradient = new LinearGradient(rowX - start1, 0, rowX + start1, 0,
//                paintColorsCurrent, new float[]{position, position},
//                TileMode.CLAMP);// 重绘渐变
//        mCurrentPaint.setShader(gradient);


        // 2.画当前行上面的
        for (int i = mCurrentLine - 1; i >= 0; i--) {
            String upStr = mLineInfos.get(i).getText();

            float upX = (getWidth() - mNormalPaint.measureText(upStr)) / 2;
            float upY = centerY - (mTextSize + mDividerHeight) * (mCurrentLine - i);
            // 超出屏幕停止绘制
            if (upY - mTextSize < 0) {
                break;
            }
            canvas.drawText(upStr, upX, upY, mNormalPaint);
        }

        // 3.画当前行下面的
        for (int i = mCurrentLine + 1; i < mLineInfos.size(); i++) {
            String downStr = mLineInfos.get(i).getText();
            float downX = (getWidth() - mNormalPaint.measureText(downStr)) / 2;
            float downY = centerY + (mTextSize + mDividerHeight) * (i - mCurrentLine);
            // 超出屏幕停止绘制
            if (downY > getHeight()) {
                break;
            }
            canvas.drawText(downStr, downX, downY, mNormalPaint);
        }

    }

    /**
     * 设置要高亮的歌词为第几行歌词
     *
     * @param position 要高亮的歌词行数
     * @param cb       是否是手指拖动后要高亮的歌词
     */
    public void seekLrc(int position, boolean cb, int time) {
        if (mLineInfos == null || position < 0 || position > mLineInfos.size()) {
            return;
        }
        LineInfo lrcRow = mLineInfos.get(position);
        if (lrcRow.getText().length() > 0) {
            mCurrentLine = position;
        }
        if (position == mLineInfos.size() - 1) {
            endTime = duration;
        } else if (position < mLineInfos.size() - 1) {
            endTime = (int) mLineInfos.get(position + 1).getTime();
        }
        starttime = (int) lrcRow.getTime();
        //	mCurrentLine = position;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasLrc()) {
            switch (event.getAction()) {
                //手指按下
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "down,mLastMotionY:" + mLastMotionY);
                    mLastMotionY = event.getY();
                    invalidate();
                    break;
                //手指移动
                case MotionEvent.ACTION_MOVE:
                    //如果一个手指按下，在屏幕上移动的话，拖动歌词上下
                    mDisplayMode = DISPLAY_MODE_SEEK;
                    Log.d(TAG, "one move");
                    doSeek(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //手指抬起
                case MotionEvent.ACTION_UP:
                    if (mDisplayMode == DISPLAY_MODE_SEEK) {
                    }

                    invalidate();
                    break;
            }
            return true;
        } else {
            switch (event.getAction()) {
                //手指按下
                case MotionEvent.ACTION_DOWN:
                    if (!hasLrc()) {
                        mCurrentPaint.setColor(Color.YELLOW);
                        invalidate();
                    }
                    break;
                //手指抬起
                case MotionEvent.ACTION_UP:
                    if (!hasLrc()) {
                        mCurrentPaint.setColor(Color.WHITE);
                        invalidate();
                        new AlertDialog.Builder(mContext)
                                .setTitle("歌词下载")
                                .setMessage("匹配到相应的歌词")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", null).show();
                    }
                    break;
            }
            return true;
        }

    }

    /**
     * 处理单指在屏幕移动时，歌词上下滚动
     */
    private void doSeek(MotionEvent event) {
        float y = event.getY();//手指当前位置的y坐标
        float offsetY = y - mLastMotionY; //第一次按下的y坐标和目前移动手指位置的y坐标之差
        //如果移动距离小于间距，不做任何处理
        if (Math.abs(offsetY) < mDividerHeight) {
            return;
        }
        //将模式设置为拖动歌词模式
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = (int) Math.abs((int) offsetY / mTextSize); //歌词要滚动的行数

        Log.d(TAG, "move to new hightlightrow : " + mCurrentLine + " offsetY: " + offsetY + " rowOffset:" + rowOffset);

        if (offsetY < 0) {
            //手指向上移动，歌词向下滚动
            mCurrentLine += rowOffset;//设置要高亮的歌词为 当前高亮歌词 向下滚动rowOffset行后的歌词
            if (mCurrentLine > mLineInfos.size() - 1) {
                mCurrentLine = mLineInfos.size() - 1;
            }
        } else if (offsetY > 0) {
            //手指向下移动，歌词向上滚动
            mCurrentLine -= rowOffset;//设置要高亮的歌词为 当前高亮歌词 向上滚动rowOffset行后的歌词
            if (mCurrentLine < 1) {
                mCurrentLine = 0;
            }
        }
//        //设置要高亮的歌词为0和mHignlightRow中的较大值，即如果mHignlightRow < 0，mCurrentLine=0
//        mCurrentLine = Math.max(0, mCurrentLine);
//        //设置要高亮的歌词为0和mHignlightRow中的较小值，即如果mHignlight > RowmLineInfos.size()-1，mCurrentLine=mLineInfos.size()-1
//        mCurrentLine = Math.min(mCurrentLine, mLineInfos.size() - 1);
//        //如果歌词要滚动的行数大于0，则重画LrcView
        if (rowOffset > 0)

        {
            mLastMotionY = y;
            newLineAnim();
            invalidate();
        }

    }


    /**
     * 播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     *
     * @param time
     */
    int duration = 0;


    public void searchLrc() {
        reset();

        mLoadingText = "正在搜索歌词";
        postInvalidate();
    }

    /**
     * 加载歌词文件
     *
     * @param path 歌词文件路径
     */
    public void loadLrc(String path, Music.Type type) {
        reset();

        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            mLoadingText = "暂无歌词";
            postInvalidate();
            return;
        }
        String text = getStrignFromFile(path);

        StringReader reader = null;
        BufferedReader br = null;
        try {
            if (type == Music.Type.LOCAL) {
                reader = new StringReader(text);
                br = new BufferedReader(reader);
            } else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = parseLine(line);
                if (arr != null) {
                    //将歌词导入
                    for (int i = 0; i < arr.length - 1; i++) {
                        LineInfo lrcrow = new LineInfo();
                        lrcrow.setText(arr[arr.length - 1]);
                        lrcrow.setTime(Long.parseLong(arr[i]));
                        mLineInfos.add(lrcrow);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reset() {
        mLineInfos.clear();
        mCurrentLine = 0;
        mNextTime = 0L;
        isEnd = false;
    }

    /**
     * 更新进度
     *
     * @param time 当前时间
     */
    public synchronized void updateTime(long time) {
        // 避免重复绘制
        if (time < mNextTime || isEnd) {
            return;
        }
        for (int i = 0; i < mLineInfos.size(); i++) {
            if (mLineInfos.get(i).getTime() > time) {
                mNextTime = mLineInfos.get(i).getTime();
                mCurrentLine = i < 1 ? 0 : i - 1;
                mDisplayMode = DISPLAY_MODE_NORMAL;
                newLineAnim();
                break;
            } else if (i == mLineInfos.size() - 1) {
                // 最后一行
                mCurrentLine = mLineInfos.size() - 1;
                isEnd = true;
                mDisplayMode = DISPLAY_MODE_NORMAL;
                newLineAnim();
                break;
            }
        }
    }

    public void onDrag(int progress) {
        for (int i = 0; i < mLineInfos.size(); i++) {
            if (mLineInfos.get(i).getTime() > progress) {
                mNextTime = mLineInfos.get(i).getTime();
                mCurrentLine = i < 1 ? 0 : i - 1;
                isEnd = false;
                newLineAnim();
                break;
            }
        }
    }

    public boolean hasLrc() {
        return !mLineInfos.isEmpty();
    }

    /**
     * 解析一行
     *
     * @param line [01:15.33]我好想你 好想你
     *             [02:34.14][01:07.00]当你我不小心又想起她
     *             [02:45.69][02:42.20][02:37.69][01:10.60]就在记忆里画一个叉
     * @return {10610, 走过了人来人往}
     */
    private String[] parseLine(String line) {
        Matcher matcher = Pattern.compile("\\[(\\d)+:(\\d)+(\\.)(\\d+)\\].+").matcher(line);
        if (!matcher.matches()) {
            return null;
        }
        line = line.replaceAll("\\[", "");
        String[] result = line.split("\\]");
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = parseTime(result[i]);
        }
        return result;
    }

    /**
     * 解析时间
     *
     * @param time 00:10.61
     * @return long
     */
    private String parseTime(String time) {
        time = time.replaceAll(":", "\\.");
        String[] times = time.split("\\.");
        long l = 0L;
        try {
            long min = Long.parseLong(times[0]);
            long sec = Long.parseLong(times[1]);
            long mil = Long.parseLong(times[2]);
            l = min * 60 * 1000 + sec * 1000 + mil * 10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(l);
    }

    /**
     * 换行动画
     * Note:属性动画只能在主线程使用
     */
    private void newLineAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(mTextSize + mDividerHeight, 0.0f);
        animator.setDuration(mAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public interface ILrcView {
        /**
         * 设置歌词拖动时候的监听类
         */
        void setListener(ILrcViewListener l);
    }


    public interface ILrcViewListener {
        /**
         * 当歌词被用户上下拖动的时候回调该方法
         */
        void onLrcSeeked(int newPosition, LineInfo row);
    }

    /**
     * 歌词行
     * 包括该行歌词的时间，歌词内容
     */
    public class LineInfo {
        private String text;
        private long time;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }


    public List<LineInfo> getLineInfos(String path) {
        String rawLrc = null;
//        rawLrc = converfile(path);
        Log.d(TAG, "getLineInfos by rawString\n" + rawLrc);
        if (rawLrc == null || rawLrc.length() == 0) {
            Log.e(TAG, "getLineInfos rawLrc null or empty");
            return null;
        }
        StringReader reader = new StringReader(rawLrc);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        List<LineInfo> rows = new ArrayList<LineInfo>();
        try {
            //循环地读取歌词的每一行
            do {
                line = br.readLine();
                /**
                 一行歌词只有一个时间的  例如：徐佳莹   《我好想你》
                 [01:15.33]我好想你 好想你

                 一行歌词有多个时间的  例如：草蜢 《失恋战线联盟》
                 [02:34.14][01:07.00]当你我不小心又想起她
                 [02:45.69][02:42.20][02:37.69][01:10.60]就在记忆里画一个叉
                 **/
                Log.d(TAG, "lrc raw line: " + line);
                if (line != null && line.length() > 0) {
                    //解析每一行歌词 得到每行歌词的集合，因为有些歌词重复有多个时间，就可以解析出多个歌词行来
//                    List<LineInfo> lrcRows = LineInfo.createRows(line);
//                    if (lrcRows != null && lrcRows.size() > 0) {
//                        for (LineInfo row : lrcRows) {
//                            rows.add(row);
//                        }
//                    }
                }
            } while (line != null);

            if (rows.size() > 0) {
                // 根据歌词行的时间排序
//                Collections.sort(rows);
                if (rows != null && rows.size() > 0) {
                    for (LineInfo lrcRow : rows) {
                        Log.d(TAG, "lrcRow:" + lrcRow.toString());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "parse exceptioned:" + e.getMessage());
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return rows;
    }

    public static String getStrignFromFile(String filepath) {
        System.out.println("ConvertFileCode--------->" + filepath);
        File file = new File(filepath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedReader reader = null;
        String text = "";
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bis.mark(4);
            byte[] first3bytes = new byte[3];
//   System.out.println("");
            //找到文档的前三个字节并自动判断文档类型。
            bis.read(first3bytes);
            bis.reset();
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                    && first3bytes[2] == (byte) 0xBF) {// utf-8

                reader = new BufferedReader(new InputStreamReader(bis, "utf-8"));

            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFE) {

                reader = new BufferedReader(
                        new InputStreamReader(bis, "unicode"));
            } else if (first3bytes[0] == (byte) 0xFE
                    && first3bytes[1] == (byte) 0xFF) {

                reader = new BufferedReader(new InputStreamReader(bis,
                        "utf-16be"));
            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFF) {

                reader = new BufferedReader(new InputStreamReader(bis,
                        "utf-16le"));
            } else {

                reader = new BufferedReader(new InputStreamReader(bis, "GBK"));
            }
            String str = reader.readLine();

            while (str != null) {
                text = text + str + "\n";
                str = reader.readLine();
            }
            System.out.println("text" + text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return text;

    }


}
