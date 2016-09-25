package com.cyl.music_hnust.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.cyl.music_hnust.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity {


    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        usernameWrapper.setHint("用户名");
        usernameWrapper.setHint("密码");
    }

    @Override
    protected void listener() {

    }


    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_go:
                String username = usernameWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();
                // TODO: 检查　
                if (!validatePassword(password)){
                    passwordWrapper.setError("密码需为6~18位的数字和字母");
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                }

                break;
        }
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
        return password.length() > 5&&password.length() <= 18;
    }
}
