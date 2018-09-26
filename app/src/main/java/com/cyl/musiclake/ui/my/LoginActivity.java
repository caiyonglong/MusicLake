package com.cyl.musiclake.ui.my;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

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

    private static final String TAG = "weibosdk";
    /** 显示认证后的信息，如 AccessToken */
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        usernameWrapper.setHint("用户名");
        passwordWrapper.setHint("密码");
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        mSsoHandler = new SsoHandler(LoginActivity.this);
    }

    @Override
    protected String setToolbarTitle() {
        return "用户登录";
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
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
                }
            });
        }
    }

    @OnClick(R.id.wbLogin)
    public void wbLogin() {
        mSsoHandler.authorize(new SelfWbAuthListener());
    }

    @OnClick(R.id.register)
    public void tofab() {
        final Intent intent = new Intent(this, RegisterActivity.class);
        if (SystemUtils.isLollipop()) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    Pair.create(register, "transition_next"),
                    Pair.create(fab, "transition_fab"),
                    Pair.create(cv, "transition_cardView")).toBundle());
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
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
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
        EventBus.getDefault().post(new LoginEvent(true, user));
        finish();
    }

    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            LoginActivity.this.runOnUiThread(() -> {
                mAccessToken = token;
                if (mAccessToken.isSessionValid()) {
                    // 显示 Token
                    updateTokenView(false);
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                    Toast.makeText(LoginActivity.this,
                            R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(LoginActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateTokenView(boolean b) {
        if (mPresenter != null) {
            mPresenter.loginServer(mAccessToken.getToken(), mAccessToken.getUid(),Constants.WEIBO);
        }
    }

}
