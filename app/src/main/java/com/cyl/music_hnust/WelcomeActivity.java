package com.cyl.music_hnust;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 1000);

    }

    class splashhandler implements Runnable{

        public void run() {
            startActivity(new Intent(getApplication(),MyActivity.class));
            WelcomeActivity.this.finish();
        }

    }


}
