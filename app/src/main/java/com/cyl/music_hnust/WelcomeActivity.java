package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cyl.music_hnust.Json.JsonParsing;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by 永龙 on 2016/3/19.
 */
public class WelcomeActivity extends AppCompatActivity {
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

                        activity.finish();

                        break;

                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler =new MyHandler(this);

        handler.sendEmptyMessageDelayed(0,5000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this,MyActivity.class);
        startActivity(intent);
    }
}
