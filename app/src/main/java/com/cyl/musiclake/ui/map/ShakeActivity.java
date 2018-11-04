package com.cyl.musiclake.ui.map;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.cyl.musiclake.utils.LogUtil;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.player.MusicPlayerService;
import com.cyl.musiclake.utils.ShakeManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 永龙 on 2016/3/22.
 */
public class ShakeActivity extends BaseActivity<NearPresenter> implements NearContract.View {

    @BindView(R.id.shake)
    ImageView imgBackground;
    @BindView(R.id.shake_result)
    RecyclerView shake_result;

    private LocationAdapter mAdapter;
    private MusicPlayerService mService;

    private MaterialDialog mProgressDialog;
    private static String user_id;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private VibrationEffect effect;

    private final String TAG = "ShakeSensorActivity";


    long secondTime = 0;
    long firstTime = 0;

    Runnable jump = () -> jumpActivity();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shake;
    }

    @Override
    protected void initView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initData() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        effect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        shake_result.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void listener() {
        ShakeManager.with(this).startShakeListener(force -> {
            if (force > 14) {
                secondTime = System.currentTimeMillis();
                mHandler.postDelayed(jump, 200);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(effect);
                }
            }
        });
    }

    private boolean isRequest = false;

    private void init() {
        LogUtil.e(TAG, secondTime + "+++++" + firstTime + "摇一摇动画结束");
        if (secondTime - firstTime > 700) {
            firstTime = secondTime;
            LogUtil.e(TAG, "正在搜索");
            showLoading("正在搜索......");
            if (!isRequest) {
                getData();
            }
        } else if (secondTime - firstTime > 500) {
            LogUtil.e(TAG, "正在搜索");
            showLoading("能不能缓一缓,我都受不了了！");
            hideLoading();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        isRequest = true;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.FUNC, Constants.SONG_ADD);
        params.put(Constants.USER_ID, user_id);

    }


    private void jumpActivity() {
        Animation animation = AnimationUtils.loadAnimation(ShakeActivity.this, R.anim.shake);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                init();
                //btn_result_show.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imgBackground.startAnimation(animation);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
        ShakeManager.with(this).cancel();
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
//            LogUtil.d(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(effect);
//                Toast.makeText(ShakeActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                jumpActivity();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showErrorInfo(String msg) {

    }


    @Override
    public void showLoading(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.loading)
                    .progress(true, 0)
                    .build();
        }
        mProgressDialog.show();
    }

}
