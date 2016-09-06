package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.cyl.music_hnust.Json.JsonParsing;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.utils.Constants;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by trumi on 16-3-2.
 */
public class NearPeopleAcivity extends AppCompatActivity {


    private static int[] textViewId = {R.id.usercenter_name
            , R.id.usercenter_num, R.id.usercenter_sex, R.id.usercenter_departments
            , R.id.usercenter_major, R.id.usercenter_class
            , R.id.usercenter_sign, R.id.usercenter_phone
            , R.id.usercenter_email};
    private static TextView[] textViews = new TextView[textViewId.length];

    private static NetworkImageView head;

    private RequestQueue mRequestQueue;
    private static ImageLoader imageLoader;
    private MyApplication application;
    private static User userInfo;
    MyHandler handler;

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> myMusicfragment;

        private MyHandler(Activity myfragment) {
            myMusicfragment = new WeakReference<Activity>(myfragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    Log.e("log+++++++++++++++",jsonObject.toString());
                    userInfo = JsonParsing.getUserinfo(jsonObject.toString());
                    initData();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center_near);
        application = new MyApplication();
//        mRequestQueue =application.getHttpQueues();
//        imageLoader = application.getImageLoader();

        handler = new MyHandler(this);
        Intent intent = getIntent();


        //给页面设置工具栏
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        if (intent.getIntExtra("flag",0)==1) {
            for (int i = 0; i < textViewId.length; i++) {
                textViews[i].setText("保密");
            }
            textViews[6].setText(intent.getStringExtra("nick").toString() == null ? "暂无" :
                    intent.getStringExtra("nick").toString());
            head.setErrorImageResId(R.drawable.ic_account_circle_black_24dp);
            head.setImageUrl(intent.getStringExtra("img"), imageLoader);
        } else if (intent.getIntExtra("flag",0)==0) {

            textViews[0].setText(intent.getStringExtra("name") == null ? "暂无" :
                    intent.getStringExtra("name"));
            textViews[1].setText(intent.getStringExtra("num") == null ? "暂无" :
                    intent.getStringExtra("num"));
            textViews[2].setText(intent.getStringExtra("sex") == null ? "暂无" :
                    intent.getStringExtra("sex"));
            textViews[3].setText(intent.getStringExtra("college") == null ? "暂无" :
                    intent.getStringExtra("college"));
            textViews[4].setText(intent.getStringExtra("major") == null ? "暂无" :
                    intent.getStringExtra("major"));
            textViews[5].setText(intent.getStringExtra("class") == null ? "暂无" :
                    intent.getStringExtra("class"));
            textViews[6].setText(intent.getStringExtra("nick") == null ? "暂无" :
                    intent.getStringExtra("nick"));
            textViews[7].setText(intent.getStringExtra("phone") == null ? "暂无" :
                    intent.getStringExtra("phone"));
            textViews[8].setText(intent.getStringExtra("email") == null ? "暂无" :
                    intent.getStringExtra("email"));





            head.setDefaultImageResId(R.drawable.circle_photo);
            head.setErrorImageResId(R.drawable.circle_photo);
            head.setImageUrl(intent.getStringExtra("img"), imageLoader);

        } else if (intent.getIntExtra("flag",0)==2) {

            Log.e("useringofasdf======", intent.getStringExtra("user_id") + "");
             volley_StringRequest_GET(intent.getStringExtra("user_id"));
            //User userInfo = JsonParsing.getUserinfo(userinfo);

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


    private void initView() {

        for (int i = 0; i < textViewId.length; i++) {
            textViews[i] = (TextView) findViewById(textViewId[i]);
        }
        head = (NetworkImageView) findViewById(R.id.backdrop);
    }

    private static void initData(){
        if (!userInfo.isSecret()) {

            textViews[0].setText(userInfo.getUser_name() == null ? "暂无" :
                    userInfo.getUser_name());
            textViews[1].setText(userInfo.getUser_id() == null ? "暂无" :
                    userInfo.getUser_id());
            textViews[2].setText(userInfo.getUser_sex() == null ? "暂无" :
                    userInfo.getUser_sex());
            textViews[3].setText(userInfo.getUser_college() == null ? "暂无" :
                    userInfo.getUser_college());
            textViews[4].setText(userInfo.getUser_major() == null ? "暂无" :
                    userInfo.getUser_major());
            textViews[5].setText(userInfo.getUser_class() == null ? "暂无" :
                    userInfo.getUser_class());
            textViews[6].setText(userInfo.getNick() == null ? "暂无" :
                    userInfo.getNick());
            textViews[7].setText(userInfo.getPhone() == null ? "暂无" :
                    userInfo.getPhone());
            textViews[8].setText(userInfo.getUser_email() == null ? "暂无" :
                    userInfo.getUser_email());


//            holder1.user_logo.setDefaultImageResId(R.mipmap.user_icon_default_main);
            head.setErrorImageResId(R.drawable.ic_account_circle_black_24dp);
            head.setImageUrl(userInfo.getUser_img(), imageLoader);
        }else {
            for (int i = 0; i < textViewId.length; i++) {
                textViews[i].setText("保密");
            }
            textViews[6].setText(userInfo.getNick() == null ? "暂无" :
                    userInfo.getNick());
            head.setErrorImageResId(R.drawable.ic_account_circle_black_24dp);
            head.setImageUrl(userInfo.getUser_img(), imageLoader);
        }
    }

    /**
     * 利用StringRequest实现Get请求
     */
    private void volley_StringRequest_GET(String user_id) {
        String param = "user_id=" + user_id +
                "&GetUserinfo";
        String url = Constants.DEFAULT_URL + param;
        // 2 创建StringRequest对象
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // VolleyLog.v("Response:%n %s", response.toString());
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                // VolleyLog.e("Error: ", error.getMessage());
            }
        });
        jsonObjectRequest.setTag("info");
        // 3 将StringRequest添加到RequestQueue
        mRequestQueue.add(jsonObjectRequest);
    }

}
