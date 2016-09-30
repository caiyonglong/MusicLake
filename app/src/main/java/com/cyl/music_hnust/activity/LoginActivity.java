package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.UserCallback;
import com.cyl.music_hnust.model.UserInfo;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity {


    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.register)
    Button register;

    @Bind(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        SystemUtils.setSystemBarTransparent(this);
        usernameWrapper.setHint("用户名");
        passwordWrapper.setHint("密码");
    }

    @Override
    protected void listener() {

    }

    @OnClick(R.id.fab)
    public void loginto() {
        final String username = usernameWrapper.getEditText().getText().toString();
        final String password = passwordWrapper.getEditText().getText().toString();
        // TODO: 检查　
        if (!validateEmail(username)) {
            passwordWrapper.setError("请输入正确的邮箱");
        } else if (!validatePassword(password)) {
            passwordWrapper.setError("密码需为6~18位的数字和字母");
        } else {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            //TODO:登录
            progressBar.setVisibility(View.VISIBLE);
            fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    login(username, password);
                }
            });
        }
    }
//
    @OnClick(R.id.register)
    public void tofab(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login(String username, String password) {

        OkHttpUtils.post()//
                .url(Constants.LOGIN_URL)//
                .addParams(Constants.USER_EMAIL, username)//
                .addParams(Constants.PASSWORD, password)//
                .build()//
                .execute(new UserCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        progressBar.setVisibility(View.GONE);
                        fab.show();
                        ToastUtils.show(LoginActivity.this,"网路异常，请稍后！");
                    }

                    @Override
                    public void onResponse(UserInfo response) {
                        Log.e("===",response.toString());
                        progressBar.setVisibility(View.GONE);
                        fab.show();
                        if (response.getStatus()==1){
                            //保存用户信息
                            UserStatus.savaUserInfo(getApplicationContext(),response.getUser());
                            UserStatus.saveuserstatus(getApplicationContext(),true);
                            ToastUtils.show(LoginActivity.this,response.getMessage());
                            finish();
                        }else {
                            ToastUtils.show(LoginActivity.this,response.getMessage());
                        }
                    }
                });
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    //判断邮箱是否合法
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //判断密码是否合法
    public boolean validatePassword(String password) {
        return password.length() > 5 && password.length() <= 18;
    }
}
