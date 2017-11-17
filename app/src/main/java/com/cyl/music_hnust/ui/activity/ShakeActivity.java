package com.cyl.music_hnust.ui.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.location.Location;
import com.cyl.music_hnust.bean.location.LocationInfo;
import com.cyl.music_hnust.bean.music.Music;
import com.cyl.music_hnust.bean.user.User;
import com.cyl.music_hnust.bean.user.UserStatus;
import com.cyl.music_hnust.callback.NearCallback;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.service.PlayManager;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ShakeManager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by 永龙 on 2016/3/22.
 */
public class ShakeActivity extends BaseActivity {

    @BindView(R.id.shake)
    ImageView imgBackground;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.shake_result)
    RecyclerView shake_result;
    public List<Location> mydatas = new ArrayList<>();

    private RecyclerView.LayoutManager mLayoutManager;

    private MyLocationAdapter adapter;

    private MusicPlayService mService;

    private static MaterialDialog mProgressDialog;
    private static String user_id;

    private SensorManager sensorManager;
    private Vibrator vibrator;

    private static final String TAG = "ShakeSensorActivity";

    long secondTime = 0;
    long firstTime = 0;

    Runnable jump = new Runnable() {
        @Override
        public void run() {
            jumpActivity();
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_shake;
    }

    @Override
    protected void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initData() {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        shake_result.setLayoutManager(mLayoutManager);

    }

    @Override
    protected void listener() {
        ShakeManager.with(this).startShakeListener(new ShakeManager.ISensor() {

            @Override
            public void onSensorChange(float force) {
                if (force > 14) {
                    secondTime = System.currentTimeMillis();
                    mHandler.postDelayed(jump, 200);
                    vibrator.vibrate(200);
                }
            }
        });
    }

    private boolean isRequest = false;

    private void init() {
        Log.e("ddd", secondTime + "+++++" + firstTime + "摇一摇动画结束");
        if (secondTime - firstTime > 700) {
            firstTime = secondTime;

            Log.e("init ----", "正在搜索");
            showProgressDialog("正在搜索......");
            if (!isRequest) {
                getdata();
            }

        } else if (secondTime - firstTime > 500) {

            Log.e("init ----", "正在搜索");

            showProgressDialog("能不能缓一缓,我都受不了了！");
            dissmissProgressDialog();
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

    /**
     * 显示进度框
     */
    private void showProgressDialog(String msg) {

        mProgressDialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .build();
    }

    /**
     * 隐藏进度框
     */
    private static void dissmissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    private void getdata() {
        isRequest = true;
        User user = UserStatus.getUserInfo(getApplicationContext());
        user_id = UserStatus.getUserInfo(this).getUser_id();
        if (user != null && user.getUser_id() != null) {
            String song = "";
            if (PlayManager.isPlaying()) {
                Music music = PlayManager.getPlayingMusic();
                song = FileUtils.getArtistAndAlbum(music.getArtist(), music.getTitle());
            }
            OkHttpUtils.post()//
                    .url(Constants.DEFAULT_URL)//
                    .addParams(Constants.FUNC, Constants.SONG_ADD)//
                    .addParams(Constants.USER_ID, user_id)//
                    .addParams(Constants.SONG, song)//
                    .build()//
                    .execute(new NearCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.e("ccc", ":aa");
                            isRequest = false;
                            dissmissProgressDialog();
                        }

                        @Override
                        public void onResponse(LocationInfo response) {
                            dissmissProgressDialog();
                            isRequest = false;
                            mydatas = response.getData();
                            Log.e("ccc", ":bb" + mydatas.size());

                            adapter = new MyLocationAdapter(getApplicationContext(), mydatas);
                            shake_result.setAdapter(adapter);
                        }
                    });


        }
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


    //适配器
    public class MyLocationAdapter extends RecyclerView.Adapter<MyLocationAdapter.MyLocationViewHolder> {


        public Context mContext;
        public List<Location> myDatas = new ArrayList<>();
        public LayoutInflater mLayoutInflater;

        public MyLocationAdapter(Context mContext, List<Location> myDatas) {
            this.mContext = mContext;
            mLayoutInflater = LayoutInflater.from(mContext);
            this.myDatas = myDatas;
        }

        @Override
        public MyLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = mLayoutInflater.inflate(R.layout.item_near, parent, false);
            MyLocationViewHolder mViewHolder = new MyLocationViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(MyLocationViewHolder holder, int position) {

            holder.user_name.setText(myDatas.get(position).getUser().getUser_name());
            holder.user_signature.setText(myDatas.get(position).getUser_song());
            String distance = "";
            String distime = "";
            distime = FormatUtil.getTimeDifference(myDatas.get(position).getLocation_time());
            Log.e("tag2", myDatas.get(position).getLocation_latitude() + "");
            Log.e("tag2", myDatas.get(position).getLocation_latitude() + "");
            Log.e("tag3", myDatas.get(position).getLocation_time() + "");

            holder.location_time.setText(distime + "在听：");
            holder.user_distance.setVisibility(View.GONE);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                    myDatas.get(position).getUser().getUser_img(), holder.user_img, ImageUtils.getAlbumDisplayOptions()
            );

        }

        @Override
        public int getItemCount() {
            return myDatas.size();
        }

        public class MyLocationViewHolder extends RecyclerView.ViewHolder {
            public ImageView user_img;
            public TextView user_name;
            public TextView user_signature;
            public TextView user_distance;
            public TextView location_time;

            public MyLocationViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                user_img = (ImageView) itemView.findViewById(R.id.user_img);
                user_signature = (TextView) itemView.findViewById(R.id.user_signature);
                user_distance = (TextView) itemView.findViewById(R.id.user_distance);
                location_time = (TextView) itemView.findViewById(R.id.location_time);
            }
        }
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

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
//            Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
//                Toast.makeText(ShakeActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                jumpActivity();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
