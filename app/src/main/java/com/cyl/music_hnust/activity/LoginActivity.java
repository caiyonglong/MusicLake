package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void listener() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_go:
                String user= etUsername.getText().toString();
                String password= etPassword.getText().toString();
                OkHttpUtils.post().url(Constants.DEFAULT_USER_URL)
                        .addParams("num",user)
                        .addParams("password",password)
                        .build()
                        .execute(new LogingSuccessCallBack());


                break;
        }
    }

    /**
     * 登录成功回调
     */
    private class LogingSuccessCallBack extends StringCallback {

        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(String response) {

        }
    }
}
