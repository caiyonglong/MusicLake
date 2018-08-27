package com.cyl.musiclake.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.FormatUtil;

/**
 * Des    :
 * Author : master.
 * Date   : 2018/7/21 .
 */
public class CountDownTimerView extends android.support.v7.widget.AppCompatTextView {
    public CountDownTimerView(Context context) {
        super(context);
        init();
    }

    public CountDownTimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownTimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    CountDownTimer timer;

    /**
     * 初始化定时任务
     */
    private void init() {
        int padding = getResources().getDimensionPixelOffset(R.dimen.dp_16);
        setPadding(padding,padding,padding,padding);
        timer = new CountDownTimer(60 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = FormatUtil.INSTANCE.formatTime(millisUntilFinished);
                setText(time);
            }

            @Override
            public void onFinish() {
                setText("0.0");
            }
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (timer != null) {
            timer.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
    }
}
