package com.cyl.musiclake.view.custom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import com.cyl.musiclake.utils.LogUtil;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.LogUtil;


/**
 * Created by Mr_immortalZ on 2016/5/2.
 * email : mr_immortalz@qq.com
 */
public class RadarViewGroup extends ViewGroup implements RadarView.IScanningListener {

    private Context mContext;

    private int mWidth, mHeight;//viewgroup的宽高
    private SparseArray<Float> scanAngleList = new SparseArray<>();//记录展示的item所在的扫描位置角度
    private SparseArray<Info> mDatas;//数据源
    private int dataLength;//数据源长度
    private int minItemPosition;//最小距离的item所在数据源中的位置
    private CircleView currentShowChild;//当前展示的item
    private CircleView minShowChild;//最小距离的item
    private IRadarClickListener iRadarClickListener;//雷达图中点击监听CircleView小圆点回调接口

    public void setiRadarClickListener(IRadarClickListener iRadarClickListener) {
        this.iRadarClickListener = iRadarClickListener;
    }

    public RadarViewGroup(Context context) {
        this(context, null);
        this.mContext= context;
    }

    public RadarViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext= context;
    }

    public RadarViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext= context;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);
        //测量每个children
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getId() == R.id.id_scan_circle) {
                //为雷达扫描图设置需要的属性
                ((RadarView) child).setScanningListener(this);
                //考虑到数据没有添加前扫描图在扫描，但是不会开始为CircleView布局
                if (mDatas != null && mDatas.size() > 0) {
                    ((RadarView) child).setMaxScanItemCount(mDatas.size());
                    ((RadarView) child).startScan();
                }
                continue;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //首先放置雷达扫描图
        View view = findViewById(R.id.id_scan_circle);
        if (view != null) {
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        //放置雷达图中需要展示的item圆点
        for (int i = 0; i < childCount; i++) {
            final int j = i;
            final View child = getChildAt(i);
            if (child.getId() == R.id.id_scan_circle) {
                //如果不是Circleview跳过
                continue;
            }
            //设置CircleView小圆点的坐标信息
            //坐标 = 旋转角度 * 半径 * 根据远近距离的不同计算得到的应该占的半径比例
            ((CircleView) child).setDisX((float) Math.cos(Math.toRadians(scanAngleList.get(i - 1) - 5))
                    * ((CircleView) child).getProportion() * mWidth / 2);
            ((CircleView) child).setDisY((float) Math.sin(Math.toRadians(scanAngleList.get(i - 1) - 5))
                    * ((CircleView) child).getProportion() * mWidth / 2);
            //如果扫描角度记录SparseArray中的对应的item的值为0，
            // 说明还没有扫描到该item，跳过对该item的layout
            //（scanAngleList设置数据时全部设置的value=0，
            // 当onScanning时，value设置的值始终不会0，具体可以看onScanning中的实现）
            if (scanAngleList.get(i - 1) == 0) {
                continue;
            }
            //放置Circle小圆点
            child.layout((int) ((CircleView) child).getDisX() + mWidth / 2, (int) ((CircleView) child).getDisY() + mHeight / 2,
                    (int) ((CircleView) child).getDisX() + child.getMeasuredWidth() + mWidth / 2,
                    (int) ((CircleView) child).getDisY() + child.getMeasuredHeight() + mHeight / 2);
            //设置点击事件
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetAnim(currentShowChild);
                    currentShowChild = (CircleView) child;
                    //因为雷达图是childAt(0),所以这里需要作-1才是正确的Circle
                    startAnim(currentShowChild, j - 1);
                    if (iRadarClickListener != null) {
                        iRadarClickListener.onRadarItemClick(j - 1);

                    }
                }
            });
        }


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

    /**
     * 设置数据
     *
     * @param mDatas
     */
    public void setDatas(SparseArray<Info> mDatas) {
        this.mDatas = mDatas;
        dataLength = mDatas.size();
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        //找到距离的最大值，最小值对应的minItemPosition
        for (int j = 0; j < dataLength; j++) {
            Info item = mDatas.get(j);
            if (item.getDistance() < min) {
                min = item.getDistance();
                minItemPosition = j;
            }
            if (item.getDistance() > max) {
                max = item.getDistance();
            }
            scanAngleList.put(j, 0f);
        }
        LogUtil.e("datalength:",dataLength+"++++++++++++++++>>>>");
        //根据数据源信息动态添加CircleView1
        for (int i = 0; i < dataLength; i++) {
            CircleView circleView = new CircleView(getContext());
            if (mDatas.get(i).getSex()) {
                circleView.setPaintColor(ContextCompat.getColor(mContext,R.color.bg_color_pink));
            } else {
                circleView.setPaintColor(ContextCompat.getColor(mContext,R.color.bg_color_blue));
            }
            //根据远近距离的不同计算得到的应该占的半径比例 0.312-0.832
            circleView.setProportion((mDatas.get(i).getDistance() / max + 0.6f) * 0.52f);
            if (minItemPosition == i) {
                minShowChild = circleView;
            }
            addView(circleView);
        }
    }

    /**
     * 雷达图没有扫描完毕时回调
     *
     * @param position
     * @param scanAngle
     */
    @Override
    public void onScanning(int position, float scanAngle) {
        if (scanAngle == 0) {
            scanAngleList.put(position, 1f);
        } else {
            scanAngleList.put(position, scanAngle);
        }
        requestLayout();
    }

    /**
     * 雷达图扫描完毕时回调
     */
    @Override
    public void onScanSuccess() {
        LogUtil.m("完成回调");
        resetAnim(currentShowChild);
        currentShowChild = minShowChild;
        startAnim(currentShowChild, minItemPosition);
    }

    /**
     * 恢复CircleView小圆点原大小
     *
     * @param object
     */
    private void resetAnim(CircleView object) {
        if (object != null) {
            object.clearPortaitIcon();
            ObjectAnimator.ofFloat(object, "scaleX", 1f).setDuration(300).start();
            ObjectAnimator.ofFloat(object, "scaleY", 1f).setDuration(300).start();
        }

    }

    /**
     * 放大CircleView小圆点大小
     *
     * @param object
     * @param position
     */
    private void startAnim(CircleView object, int position) {
        if (object != null) {
            object.setPortraitIcon(mDatas.get(position).getPortraitId());
            ObjectAnimator.ofFloat(object, "scaleX", 2f).setDuration(300).start();
            ObjectAnimator.ofFloat(object, "scaleY", 2f).setDuration(300).start();
        }
    }

    /**
     * 雷达图中点击监听CircleView小圆点回调接口
     */
    public interface IRadarClickListener {
        void onRadarItemClick(int position);
    }

    /**
     * 根据position，放大指定的CircleView小圆点
     *
     * @param position
     */
    public void setCurrentShowItem(int position) {
        CircleView child = (CircleView) getChildAt(position + 1);
        resetAnim(currentShowChild);
        currentShowChild = child;
        startAnim(currentShowChild, position);
    }
}
