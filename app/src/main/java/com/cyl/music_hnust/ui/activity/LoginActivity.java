package com.cyl.music_hnust.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.UserCallback;
import com.cyl.music_hnust.model.user.UserInfo;
import com.cyl.music_hnust.model.user.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.qqlogin)
    com.getbase.floatingactionbutton.FloatingActionButton qqlogin;
    @Bind(R.id.register)
    Button register;

    @Bind(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    //QQ第三方登录
    String APP_ID = "1104846425";
    Tencent mTencent;
    IUiListener loginListener;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("用户登录");
        usernameWrapper.setHint("用户名");
        passwordWrapper.setHint("密码");
    }

    @Override
    protected void listener() {

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

    @OnClick(R.id.fab)
    public void loginto() {
        final String username = usernameWrapper.getEditText().getText().toString();
        final String password = passwordWrapper.getEditText().getText().toString();
        // TODO: 检查　
        if (!validatePassword(username)) {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);

            usernameWrapper.setError("邮箱或者学号");
        } else if (!validatePassword(password)) {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);

            passwordWrapper.setError("密码需为5~18位的数字或字母");
        } else {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            //TODO:登录
            progressBar.setVisibility(View.VISIBLE);
            fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constants.USER_EMAIL, username);
                    params.put(Constants.PASSWORD, password);
                    login(params);
                }
            });
        }
    }

    @OnClick(R.id.register)
    public void tofab() {

        final Intent intent = new Intent(this, RegisterActivity.class);

        if (SystemUtils.isLollipop()) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    Pair.create((View) register, "transition_next"),
                    Pair.create((View) fab, "transition_fab"),
                    Pair.create((View) cv, "transition_cardView")).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @OnClick(R.id.qqlogin)
    public void tologin() {
        progressBar.setVisibility(View.VISIBLE);
        qqLogin();
    }


    private void login(Map<String, String> params) {

        OkHttpUtils.post()//
                .url(Constants.LOGIN_URL)
                .params(params)
                .build()//
                .execute(new UserCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        fab.show();
                        ToastUtils.show(LoginActivity.this, "网络异常，请稍后重试！");
                    }

                    @Override
                    public void onResponse(UserInfo response) {
                        Log.e("===", response.toString());
                        progressBar.setVisibility(View.GONE);
                        fab.show();
                        if (response.getStatus() == 1) {
                            //保存用户信息
                            UserStatus.savaUserInfo(getApplicationContext(), response.getUser());
                            UserStatus.saveuserstatus(getApplicationContext(), true);
                            ToastUtils.show(LoginActivity.this, response.getMessage());
                            finish();
                        } else {
                            ToastUtils.show(LoginActivity.this, response.getMessage());
                        }
                    }
                });
    }

    //判断密码是否合法
    public boolean validatePassword(String password) {
        return password.length() >= 5 && password.length() <= 18;
    }

    /**
     * 实现QQ第三方登录
     */
    public void qqLogin() {
        //QQ第三方登录
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        mTencent.login(this, "all", loginListener);
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                progressBar.setVisibility(View.GONE);
                //登录成功后回调该方法,可以跳转相关的页面
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                JSONObject object = (JSONObject) o;
                try {
                    String accessToken = object.getString("access_token");
                    String expires = object.getString("expires_in");
                    String openID = object.getString("openid");
                    mTencent.setAccessToken(accessToken, expires);
                    mTencent.setOpenId(openID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            if (resultCode == -1) {
                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
                Tencent.handleResultData(data, loginListener);
                com.tencent.connect.UserInfo info = new com.tencent.connect.UserInfo(this, mTencent.getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        try {
                            JSONObject info = (JSONObject) o;
                            String nickName = info.getString("nickname");//获取用户昵称
                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url
                            String gender = info.getString("gender");//获取用户性别
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Constants.PARAM_METHOD, "qq");
                            params.put(Constants.USERNAME, nickName);
                            params.put(Constants.USER_SEX, gender);
                            params.put(Constants.USER_IMG, iconUrl);
                            params.put(Constants.USER_ID, mTencent.getOpenId());
                            login(params);
                        } catch (JSONException e) {
                            ToastUtils.show(LoginActivity.this, "网络异常，请稍后重试！");
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


}
