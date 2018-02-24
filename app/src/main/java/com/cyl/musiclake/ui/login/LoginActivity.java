package com.cyl.musiclake.ui.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.common.Constants;
import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.qqlogin)
    com.getbase.floatingactionbutton.FloatingActionButton qqlogin;
    @BindView(R.id.register)
    Button register;

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    LoginPresenter mPresenter;

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
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
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
//                    login(params);
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
        mPresenter.loginByQQ(this);
    }


    //判断密码是否合法
    public boolean validatePassword(String password) {
        return password.length() >= 5 && password.length() <= 18;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorInfo(String msg) {
        ToastUtils.show(this, msg);
    }

    @Override
    public void success(User user) {
        RxBus.getInstance().post(user);
        finish();
    }
}
