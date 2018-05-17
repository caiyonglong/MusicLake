package com.cyl.musiclake.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.cyl.musiclake.R;


/**
 * Created by Mr_immortalZ on 2016/5/2.
 * email : mr_immortalz@qq.com
 */
public class RadarView extends View {

    private Context mContext;
    Canvas canvas;
    private Paint mPaintLine;//画圆线需要用到的paint
    private Paint mPaintCircle;//画圆需要用到的paint
    private Paint mPaintScan;//画扫描需要用到的paint

    private int mWidth, mHeight;//整个图形的长度和宽度

    private Matrix matrix = new Matrix();//旋转需要的矩阵
    private int scanAngle;//扫描旋转的角度
    private Shader scanShader;//扫描渲染shader
    private Bitmap centerBitmap =null;//最中间icon

    //每个圆圈所占的比例
    private static float[] circleProportion = {1 / 13f, 2 / 13f, 3 / 13f, 4 / 13f, 5 / 13f, 6 / 13f};
    private int scanSpeed = 5;

    private int currentScanningCount;//当前扫描的次数
    private int currentScanningItem;//当前扫描显示的item
    private int maxScanItemCount;//最大扫描次数
    private boolean startScan = false;//只有设置了数据后才会开始扫描
    private IScanningListener iScanningListener;//扫描时监听回调接口

    public void setScanningListener(IScanningListener iScanningListener) {
        this.iScanningListener = iScanningListener;
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            scanAngle = (scanAngle + scanSpeed) % 360;
            matrix.postRotate(scanSpeed, mWidth / 2, mHeight / 2);
            invalidate();
            postDelayed(run, 130);
            //开始扫描显示标志为true 且 只扫描一圈
            if (startScan && currentScanningCount <= (360 / scanSpeed)) {
                if (iScanningListener != null && currentScanningCount % scanSpeed == 0
                        && currentScanningItem < maxScanItemCount) {

                    iScanningListener.onScanning(currentScanningItem, scanAngle);
                    currentScanningItem++;
                } else if (iScanningListener != null && currentScanningItem == maxScanItemCount) {
                    iScanningListener.onScanSuccess();
                }
                currentScanningCount++;
            }
        }
    };

    public RadarView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        post(run);
    }


    private void init() {
        mPaintLine = new Paint();
        mPaintLine.setColor(ContextCompat.getColor(mContext, R.color.line_color_blue));
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStrokeWidth(1);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.WHITE);
        mPaintCircle.setAntiAlias(true);

        mPaintScan = new Paint();
        mPaintScan.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(widthMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);

        centerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle);

        //设置扫描渲染的shader
        scanShader = new SweepGradient(mWidth / 2, mHeight / 2,
                new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        drawCircle(canvas);
        drawScan(canvas);
        drawCenterIcon(canvas);
    }

    /**
     * 绘制圆线圈
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[1], mPaintLine);     // 绘制小圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[2], mPaintLine);   // 绘制中圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[3], mPaintLine); // 绘制中大圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[4], mPaintLine);  // 绘制大圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[5], mPaintLine);  // 绘制大大圆
    }

    /**
     * 绘制扫描
     *
     * @param canvas
     */
    private void drawScan(Canvas canvas) {
        canvas.save();
        mPaintScan.setShader(scanShader);
        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[4], mPaintScan);
        canvas.restore();
    }

    /**
     * 绘制最中间的图标
     *
     * @param canvas
     */
    private void drawCenterIcon(Canvas canvas) {
        if (canvas != null&& centerBitmap!=null) {
            canvas.drawBitmap(centerBitmap, null,
                    new Rect((int) (mWidth / 2 - mWidth * circleProportion[0]), (int) (mHeight / 2 - mWidth * circleProportion[0]),
                            (int) (mWidth / 2 + mWidth * circleProportion[0]), (int) (mHeight / 2 + mWidth * circleProportion[0])), mPaintCircle);

        }
    }


    public interface IScanningListener {
        //正在扫描（此时还没有扫描完毕）时回调
        void onScanning(int position, float scanAngle);

        //扫描成功时回调
        void onScanSuccess();
    }

    public void setMaxScanItemCount(int maxScanItemCount) {
        this.maxScanItemCount = maxScanItemCount;
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        this.startScan = true;
    }
}
