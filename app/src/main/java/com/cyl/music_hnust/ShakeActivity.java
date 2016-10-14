package com.cyl.music_hnust;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.activity.BaseActivity;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.callback.NearCallback;
import com.cyl.music_hnust.model.Location;
import com.cyl.music_hnust.model.LocationInfo;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.ShakeManager;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.service.PlayService;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ImageUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 永龙 on 2016/3/22.
 */
public class ShakeActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton back;
    private static ImageView imgBackground;
    private Button btn_result_show;
    private static RecyclerView shake_result;
    public static List<Location> mydatas;

    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private MyApplication application;

    private RecyclerView.LayoutManager mLayoutManager;

    private static MyLocationAdapter adapter;


    private PlayService mService;

    private static ProgressDialog progDialog = null;
    private static MyHandler handler;
    private static String user_id;


    static class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        break;
                    case 1:
                        dissmissProgressDialog();
                        Bundle bundle = new Bundle();
                        bundle = msg.getData();
                        try {
                            JSONObject jsonObject = new JSONObject(bundle.getString("response"));
                            mydatas = JsonParsing.getLocation(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.myDatas = mydatas;
                        adapter.notifyDataSetChanged();
                        shake_result.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        dissmissProgressDialog();

                        break;
                    case SENSOR_SHAKE:
                        Toast.makeText(activity, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "检测到摇晃，执行操作！");
                        jumpActivity();
                        break;
                }
            }
        }
    }

    long secondTime = 0;
    long firstTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        mService = MainActivity.getmPlayService();
        handler = new MyHandler(ShakeActivity.this);
        application = new MyApplication();
//        mRequestQueue = application.getHttpQueues();
//        imageLoader = application.getImageLoader();

        context = getApplicationContext();
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        initView();
        initdata();


        ShakeManager.with(this).startShakeListener(new ShakeManager.ISensor() {

            @Override
            public void onSensorChange(float force) {
                if (force > 14) {
                    secondTime = System.currentTimeMillis();
                    jumpActivity();
                    vibrator.vibrate(200);
                    init();
                }
            }
        });


        //ToastUtils.show(this, "『摇一{摇』搜索");

    }

    @Override
    protected void listener() {

    }

    private void init() {
        Log.e("ddd", secondTime + "+++++" + firstTime);
        if (secondTime - firstTime > 700) {
            firstTime = secondTime;
            showProgressDialog("正在搜索......");
            getdata();

        } else if (secondTime - firstTime > 500) {
            shake_result.setVisibility(View.VISIBLE);
            showProgressDialog("能不能缓一缓,我都受不了了！");
            handler.sendEmptyMessageDelayed(2, 2000);
        } else if (secondTime - firstTime > 0) {
            shake_result.setVisibility(View.GONE);
//            showProgressDialog("能不能缓一缓,我都受不了了！");
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog(String msg) {
        progDialog = new ProgressDialog(ShakeActivity.this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage(msg);
        progDialog.show();

    }

    /**
     * 隐藏进度框
     */
    private static void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    private void getdata() {
        User user = UserStatus.getUserInfo(getApplicationContext());


        if (user.getUser_id() != null) {

            if (mService.isPlaying()) {
                Music music = mService.getPlayingMusic();
                //上传正在听的歌曲
                Log.e("Tag", music.getTitle().toString());
                String song = music.getArtist().toString() + "";
                uploadsong(song);
            } else {

                handler.sendEmptyMessage(2);
                ToastUtils.show(getApplicationContext(), "未播放歌曲");
            }
        } else {
            handler.sendEmptyMessage(2);
            ToastUtils.show(getApplicationContext(), "暂未登录");
        }
    }

    private void initdata() {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void initView() {

        mydatas = new ArrayList<>();
        back = (ImageButton) findViewById(R.id.back);
        btn_result_show = (Button) findViewById(R.id.btn_result_show);
        shake_result = (RecyclerView) findViewById(R.id.shake_result);
        imgBackground = (ImageView) findViewById(R.id.shake);
        adapter = new MyLocationAdapter(getApplicationContext(), mydatas);
        back.setOnClickListener(this);
        //  btn_result_show.setOnClickListener(this);
        shake_result.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        shake_result.setLayoutManager(mLayoutManager);

    }

    static Context context;

    private static void jumpActivity() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //btn_result_show.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imgBackground.startAnimation(animation);

    }


    @Override
    public void onBackPressed() {
        finish();
        //  startActivityNoAnim(MainActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_result_show:
                //   shake_result.setVisibility(View.VISIBLE);
                //    getdata();
                break;
        }
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

            holder.user_name.setText(myDatas.get(position).getUser().getUser_name()
            );
            holder.user_signature.setText(myDatas.get(position).getUser_song());
            String distance = "";
            String distime = "";
//            if (myDatas.size() > 0) {
//            distance = FormatUtil.Distance(latLonPoint.getLongitude() + 0, latLonPoint.getLatitude() + 0,
//                    myDatas.get(position).getLocation_longitude() + 0, myDatas.get(position).getLocation_latitude() + 0);
            distime = FormatUtil.distime(myDatas.get(position).getLocation_time());
//            }
//
            Log.e("tag2", myDatas.get(position).getLocation_latitude() + "");
            Log.e("tag2", myDatas.get(position).getLocation_latitude() + "");
            Log.e("tag3", myDatas.get(position).getLocation_time() + "");

            holder.location_time.setText(distime);
            holder.user_distance.setVisibility(View.GONE);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                    myDatas.get(position).getUser().getUser_img(),holder.user_img, ImageUtils.getAlbumDisplayOptions()
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

    private void uploadsong(String song) {

        user_id = UserStatus.getUserInfo(this).getUser_id();

        OkHttpUtils.post()//
                .url(Constants.DEFAULT_URL)//
                .addParams(Constants.FUNC, Constants.SECRET_ADD)//
                .addParams(Constants.USER_ID, user_id)//
                .addParams(Constants.SONG, song)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("ccc","ddd");
                        ToastUtils.show(getApplicationContext(),"网络连接错误！");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("ccc",response);
                    }
                });

        OkHttpUtils.post()//
                .url(Constants.DEFAULT_URL)//
                .addParams(Constants.FUNC, Constants.NEAR)//
                .addParams(Constants.USER_ID, user_id)//
                .build()//
                .execute(new NearCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("ccc",":aa");

                    }

                    @Override
                    public void onResponse(LocationInfo response) {
                        Log.e("ccc",":bb");
                        adapter.myDatas = response.getData();
                        adapter.notifyDataSetChanged();
                        shake_result.setVisibility(View.VISIBLE);
                    }
                });

    }


    private SensorManager sensorManager;
    private Vibrator vibrator;

    private static final String TAG = "ShakeSensorActivity";
    private static final int SENSOR_SHAKE = 10;


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
            Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


}
