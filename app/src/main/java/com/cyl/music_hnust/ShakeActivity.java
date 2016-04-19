package com.cyl.music_hnust;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.bean.Location;
import com.cyl.music_hnust.bean.ShakeManager;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.service.MusicPlayService;
import com.cyl.music_hnust.utils.FormatUtil;
import com.cyl.music_hnust.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 永龙 on 2016/3/22.
 */
public class ShakeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back;
    private Button btn_result_show;
    private RecyclerView shake_result;
    public static List<Location> mydatas;

    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private RecyclerView.LayoutManager mLayoutManager;

    private static MyLocationAdapter adapter;


    private MusicPlayService mService;

    private static ProgressDialog progDialog = null;
    private static MyHandler handler;

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
                        bundle =msg.getData();
                        try {
                            JSONObject jsonObject = new JSONObject(bundle.getString("response"));
                            mydatas = JsonParsing.getLocation(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.myDatas = mydatas;
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        dissmissProgressDialog();

                        break;
                }
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        MyApplication application = (MyApplication) getApplication();
        mService = application.getmService();
        handler = new MyHandler(ShakeActivity.this);

        initView();
        initdata();


        ShakeManager.with(this).startShakeListener(new ShakeManager.ISensor() {
            @Override
            public void onSensorChange(float force) {
                if (force > 10) {

                    jumpActivity();

                }
            }
        });
        ToastUtil.show(this, "『摇一摇』搜索");

    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索...");
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
        showProgressDialog();

        if (user.getUser_id() != null) {

            if (mService.isPlay()) {
                //上传正在听的歌曲
                Log.e("Tag", mService.getSongName().toString());
                String song = mService.getSongName().toString() + "";
                volley_StringRequest_GET(user.getUser_id(), song, 0);
                volley_StringRequest_GET(user.getUser_id(), "", 1);

            } else {

                handler.sendEmptyMessage(2);
                ToastUtil.show(getApplicationContext(), "未播放歌曲");
            }
        } else {
            handler.sendEmptyMessage(2);
            ToastUtil.show(getApplicationContext(), "暂未登录");
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
        adapter = new MyLocationAdapter(getApplicationContext(), mydatas);
        back.setOnClickListener(this);
        btn_result_show.setOnClickListener(this);
        shake_result.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        shake_result.setLayoutManager(mLayoutManager);

    }

    private void jumpActivity() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_result_show.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        findViewById(R.id.shake).startAnimation(animation);

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

                btn_result_show.setVisibility(View.GONE);
//                finish();
                break;
            case R.id.btn_result_show:
                shake_result.setVisibility(View.VISIBLE);
                getdata();
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

            holder.user_name.setText(myDatas.get(position).getUser().getUser_name() + "正在听" +
                    myDatas.get(position).getUser_song());
            holder.user_signature.setText(myDatas.get(position).getUser().getSignature());
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


            holder.location_time.setText(distime + "前");
            holder.user_distance.setVisibility(View.GONE);
            //  holder1.user_logo.setDefaultImageResId(R.mipmap.user_icon_default_main);
//            holder.user_img.setErrorImageResId(R.mipmap.user_icon_default_main);
//            holder.user_img.setImageUrl(myDatas.get(position).getUser().getUser_img(), imageLoader);

        }

        @Override
        public int getItemCount() {
            return myDatas.size();
        }

        public class MyLocationViewHolder extends RecyclerView.ViewHolder {
            public NetworkImageView user_img;
            public TextView user_name;
            public TextView user_signature;
            public TextView user_distance;
            public TextView location_time;

            public MyLocationViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                user_img = (NetworkImageView) itemView.findViewById(R.id.user_img);
                user_signature = (TextView) itemView.findViewById(R.id.user_signature);
                user_distance = (TextView) itemView.findViewById(R.id.user_distance);
                location_time = (TextView) itemView.findViewById(R.id.location_time);
            }
        }
    }


    /**
     * 利用StringRequest实现Get请求
     */
    private void volley_StringRequest_GET(String user_id, String song, final int requestcode) {
        String url = "";
        if (requestcode == 0) {
            //上传歌曲名
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id=" + user_id + "&newShake&user_song=" + song;
        } else if (requestcode == 1) {
            //附近的人
            url = "http://119.29.27.116/hcyl/music_BBS/operate.php?user_id=" + user_id + "&nearLocation";
        }
        // 2 创建StringRequest对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // VolleyLog.v("Response:%n %s", response.toString());
                        Log.i("log", response.toString());


                        Message message = new Message();
                        message.what = requestcode;
                        if (requestcode == 1) {

                            //ToastUtil.show(getApplicationContext(),response.toString()+"");
                            Bundle bundle = new Bundle();
                            bundle.putString("response", response.toString());
                            message.setData(bundle);


                            //  ToastUtil.show(getApplicationContext(), mdatas1.size() + "");


                        } else {
                            //  ToastUtil.show(getApplicationContext(), response.toString() + "");

                        }
                        handler.sendMessage(message);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                // VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }
}
