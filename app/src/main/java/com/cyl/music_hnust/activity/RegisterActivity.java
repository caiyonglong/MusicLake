package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.ToastUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/11 18:38
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class RegisterActivity extends BaseActivity {

    Toolbar mToolbar;
    FloatingActionButton fab;
    Button register;

    TextInputLayout emailWrapper;
    TextInputLayout userNameWrapper;
    TextInputLayout passWordWrapper;

    ProgressBar progressBar;
    Button success;


    @Override
    protected void listener() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        success = (Button) findViewById(R.id.success);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(getApplicationContext(),"注册功能开发中...");
            }
        });
    }

    private void register(String email,String username, String password) {

        OkHttpUtils.post()//
                .url(Constants.REGISTER_URL)//
                .addParams(Constants.USER_EMAIL, email)//
                .addParams(Constants.USERNAME, username)//
                .addParams(Constants.PASSWORD, password)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
    }
}
