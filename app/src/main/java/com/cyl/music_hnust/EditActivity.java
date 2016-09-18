package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.model.Common;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.http.HttpUtil;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FormatUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by 永龙 on 2016/3/15.
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back;
    private EditText send_info;
    private Button btn_send_info;
    private MyHandler handler;


    private RequestQueue mRequestQueue;
    private MyApplication application;
    String sharecontent="";
    Intent in;

    static class MyHandler extends Handler {
        WeakReference<Activity > mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference= new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(activity, "发布成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        break;
                    case 2:
                        String error_code = (String) msg.obj;
                        Toast.makeText(activity, "发布失败,网络异常", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        handler =new MyHandler(EditActivity.this);
        in=getIntent();
        if (in.getStringExtra("sharecontent")!=null){
            sharecontent = in.getStringExtra("sharecontent");
            Log.e("edit",sharecontent);
        }
        application = new MyApplication();
//        mRequestQueue = application.getHttpQueues();

        initView();


    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.backImageButton);
        send_info = (EditText) findViewById(R.id.edit_content);
        btn_send_info = (Button) findViewById(R.id.btn_send_info);
        send_info.setText(sharecontent);
        back.setOnClickListener(this);
        btn_send_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImageButton:
                finish();
                break;
            case R.id.btn_send_info:
                String content = send_info.getText().toString().trim();
                Log.e("content", content + "");
                if (!TextUtils.isEmpty(content)) {
                    String date = Common.getDate(getApplicationContext());
                    Log.e("content", content + "");
                    User userinfo = UserStatus.getUserInfo(getApplicationContext());
                    if (userinfo.getUser_name() != null) {
                        //动态发布.
                        volley_Request_GET(userinfo.getUser_id(), content);

                     //   Dynamic dynamic = new Dynamic();

//                        dynamic.setLove(0);
//                        dynamic.setMyLove(false);
                        Log.e("Time",FormatUtil.getTime());
//                        dynamic.setTime(FormatUtil.getTime());
//                        dynamic.setComment(0);
//                        dynamic.setContent();
//                        dynamic.setUser(userinfo);

                        Intent it = new Intent();
                        it.putExtra("content", content);
                        it.putExtra("time", FormatUtil.getTime());

                        setResult(1, it);

                        finish();
                    } else {
                        Intent it = new Intent(this, LoginActivity.class);
                        startActivity(it);
                    }

                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("请输入内容")
                            .setTitleText("无内容")
                            .show();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ondestory", "finish");
    }

    private void volley_Request_GET(String user_id, String content) {

        String time = FormatUtil.getTime();
        String urlString =  Constants.DEFAULT_URL+"newSecret&" +
                "secretContent=" + content +
                "&secretTime=" + time +
                "&user_id=" + user_id;

        Log.e("content", urlString + "");
        HttpUtil.get(urlString, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                Log.e("res", response.toString());
                try {
                    int error_code = response.getInt("error");
                    Log.e("error_code", error_code + "");
                    if (error_code == -1) {
                        handler.sendEmptyMessage(0);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = error_code;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            }
        });
    }


}
