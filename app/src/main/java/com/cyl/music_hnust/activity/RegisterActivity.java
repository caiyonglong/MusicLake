package com.cyl.music_hnust.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.cyl.music_hnust.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/11 18:38
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    FloatingActionButton fab;
    Button bt_go;
    TextInputLayout emailWrapper;
    TextInputLayout userNameWrapper;
    TextInputLayout passWordWrapper;


    @Override
    protected void listener() {
        fab.setOnClickListener(this);
        bt_go.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

        emailWrapper.setHint("邮箱");
        userNameWrapper.setHint("昵称");
        passWordWrapper.setHint("密码");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        userNameWrapper = (TextInputLayout) findViewById(R.id.userNameWrapper);
        passWordWrapper = (TextInputLayout) findViewById(R.id.passWordWrapper);

    }
    @OnClick({R.id.fab,R.id.bt_go})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                finish();
                break;
            case R.id.bt_go:
                String emailAddress = emailWrapper.getEditText().getText().toString();
                String username = userNameWrapper.getEditText().getText().toString();
                String password = passWordWrapper.getEditText().getText().toString();
                // TODO: 检查　
                if (!validateEmail(emailAddress)) {
                    emailWrapper.setError("无效邮箱，请输入正确的邮箱");
                } else if (!validatePassword(password)){
                    passWordWrapper.setError("密码需为6~18位的数字和字母");
                } else {
                    emailWrapper.setErrorEnabled(false);
                    userNameWrapper.setErrorEnabled(false);
                    passWordWrapper.setErrorEnabled(false);
//
//                    CommUser commUser = new CommUser();
//                    commUser.id= emailAddress;
//                    commUser.name= username;
//                    // TODO：注册
//                    doRegister(commUser,password);

                }

                break;
        }
    }
//
//    private void doRegister(CommUser commUser,String password) {
//        CommunitySDKImpl.getInstance().registerToWsq(this, commUser, new LoginListener() {
//            @Override
//            public void onStart() {}
//
//            @Override
//            public void onComplete(int i, CommUser commUser) {
//                if(i == 0 || i == 10013) {
//                    Log.e("xxxxxx", "finish!!!!!!!");
//                    Log.e("++++++",commUser.toString());
////                    RegisterActivity.this.finish();
//                    if(i == 0) {
////                        To.showShortMsgByResName("umeng_comm_register_success");
//                    }
//
////                    RegisterActivity.mRegisterListener.onComplete(stCode, userInfo);
//                }
//            }
//        },password);
//    }

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
        return password.length() > 5&&password.length() <= 18;
    }
}
