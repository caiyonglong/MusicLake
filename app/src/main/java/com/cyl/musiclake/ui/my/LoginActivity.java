package com.cyl.musiclake.ui.my;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyl.musicapi.netease.LoginInfo;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.utils.ToastUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.qqlogin)
    FloatingActionButton qqlogin;
    @BindView(R.id.wbLogin)
    FloatingActionButton wbLogin;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private static final String TAG = "LoginActivity";
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
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        mSsoHandler = new SsoHandler(LoginActivity.this);
    }

    @Override
    protected String setToolbarTitle() {
        return getString(R.string.login_title);
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

    @OnClick(R.id.wbLogin)
    public void wbLogin() {
        mSsoHandler.authorize(new SelfWbAuthListener());
    }

    @OnClick(R.id.qqlogin)
    public void tologin() {
        mPresenter.loginByQQ(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void bindSuccess(LoginInfo loginInfo) {
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
            mPresenter.loginServer(mAccessToken.getToken(), mAccessToken.getUid(), Constants.WEIBO);
        }
    }

}
