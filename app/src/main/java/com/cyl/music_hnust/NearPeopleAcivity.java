package com.cyl.music_hnust;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created by trumi on 16-3-2.
 */
public class NearPeopleAcivity extends AppCompatActivity {

    private TextView user_name;
    private TextView user_num;
    private TextView user_departments;
    private TextView user_class;
    private TextView user_major;

    private NetworkImageView head;

    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_center_near);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
            }

            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }
        });

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

        String imgUrl = "http://119.29.27.116/hcyl/music_BBS";
        if (intent!=null) {
            user_name.setText(intent.getStringExtra("name"));
            user_num.setText(intent.getStringExtra("num"));
            user_departments.setText(intent.getStringExtra("college")==null?"保密":intent.getStringExtra("college"));
            user_class.setText(intent.getStringExtra("class")==null?"保密":intent.getStringExtra("class"));
            user_major.setText(intent.getStringExtra("major")==null?"保密":intent.getStringExtra("major"));
//            holder1.user_logo.setDefaultImageResId(R.mipmap.user_icon_default_main);
            head.setErrorImageResId(R.mipmap.user_icon_default_main);
            head.setImageUrl(imgUrl + intent.getStringExtra("img"), imageLoader);


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
        user_name = (TextView) findViewById(R.id.usercenter_name);
        user_class = (TextView) findViewById(R.id.usercenter_class);
        user_major = (TextView) findViewById(R.id.usercenter_major);
        user_departments = (TextView) findViewById(R.id.usercenter_departments);
        user_num = (TextView) findViewById(R.id.usercenter_num);
        head = (NetworkImageView) findViewById(R.id.backdrop);
    }


}
