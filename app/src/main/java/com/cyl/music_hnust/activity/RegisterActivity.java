package com.cyl.music_hnust.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.callback.StatusCallback;
import com.cyl.music_hnust.model.user.StatusInfo;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.utils.ToastUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：yonglong on 2016/8/11 18:38
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.emailWrapper)
    TextInputLayout emailWrapper;
    @Bind(R.id.passWordWrapper)
    TextInputLayout passWordWrapper;
    @Bind(R.id.userNameWrapper)
    TextInputLayout userNameWrapper;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.publish)
    Button publish;

    @OnClick(R.id.publish)
    void publish(){
        final String email = emailWrapper.getEditText().getText().toString();
        final String password = passWordWrapper.getEditText().getText().toString();
        final String username = userNameWrapper.getEditText().getText().toString();
        // TODO: 检查　
        if (!isEmail(email)) {
            emailWrapper.setErrorEnabled(false);
            passWordWrapper.setErrorEnabled(false);
            userNameWrapper.setErrorEnabled(false);

            emailWrapper.setError("请输入正确的邮箱");
        } else if (username.length()<=0) {
            emailWrapper.setErrorEnabled(false);
            passWordWrapper.setErrorEnabled(false);
            userNameWrapper.setErrorEnabled(false);

            passWordWrapper.setError("昵称不能为空");
        } else if (!validatePassword(password)) {
            emailWrapper.setErrorEnabled(false);
            passWordWrapper.setErrorEnabled(false);
            userNameWrapper.setErrorEnabled(false);
            userNameWrapper.setError("密码长度需为8~16");
        }else {
            emailWrapper.setErrorEnabled(false);
            passWordWrapper.setErrorEnabled(false);
            userNameWrapper.setErrorEnabled(false);
            //TODO:登录
            progressBar.setVisibility(View.VISIBLE);
            register(email,username,password);
        }
    }

    @OnClick(R.id.fab)
    void exit(){
//        if (SystemUtils.isLollipop()) {
//            getWindow().getExitTransition().addListener(new EnterTransitionListener());
//        }
//        finish();
        onBackPressed();
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setTitle("用户注册");
    }

    @Override
    protected void initData() {
        if (SystemUtils.isLollipop()) {
            getWindow().getEnterTransition().addListener(new EnterTransitionListener());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 用户注册
     * @param email
     * @param password
     */
    private void register(String email,String username, String password) {
        OkHttpUtils.post()//
                .url(Constants.REGISTER_URL)//
                .addParams(Constants.USER_EMAIL, email)//
                .addParams(Constants.NICK, username)//
                .addParams(Constants.PASSWORD, password)//
                .build()//
                .execute(new StatusCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        progressBar.setVisibility(View.GONE);
                        ToastUtils.show(RegisterActivity.this,"网络请求错误");
                    }

                    @Override
                    public void onResponse(StatusInfo response) {
                        progressBar.setVisibility(View.GONE);
                        ToastUtils.show(RegisterActivity.this,response.getMessage());
                        Log.e("re",response.getStatus()+response.getMessage());
                        finish();
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class EnterTransitionListener implements Transition.TransitionListener {
        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }

    //判断密码是否合法
    public boolean validatePassword(String password) {
        return password.length() > 5 && password.length() <= 18;
    }
    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}
