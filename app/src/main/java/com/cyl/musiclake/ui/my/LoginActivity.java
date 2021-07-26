package com.cyl.musiclake.ui.my;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.cyl.musicapi.netease.LoginInfo;
import com.cyl.musiclake.BuildConfig;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.utils.Tools;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sina.weibo.sdk.auth.AccessTokenHelper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;

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
    private IWBAPI mWBAPI;

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
        mWBAPI = MusicApp.iwbapi;
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
        if (mWBAPI != null) {
            mWBAPI.authorize(new SelfWbAuthListener());
        }
    }

    @OnClick(R.id.qqlogin)
    public void tologin() {
        mPresenter.loginByQQ(this);
    }

    @OnClick(R.id.githubLogin)
    public void toGihubLogin() {
        String auth = "https://github.com/login/oauth/authorize?client_id=" + Constants.GITHUB_CLIENT_ID + "&redirect_uri=" + Constants.GITHUB_REDIRECT_URI + "&state=" + BuildConfig.APPLICATION_ID;
        Tools.INSTANCE.openBrowser(this, auth);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            if (mPresenter != null && code != null && state != null) {
                LogUtil.d(TAG, "onNewIntent code=" + code + " state+" + state);
                mPresenter.loginByGithub(code, state);
            } else {
                ToastUtils.show("Github 授权失败");
            }
        } else {
            ToastUtils.show("Github 授权失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
        if (mWBAPI != null) {
            mWBAPI.authorizeCallback(requestCode, resultCode, data);
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

    private class SelfWbAuthListener implements WbAuthListener {

        @Override
        public void onComplete(Oauth2AccessToken oauth2AccessToken) {
            LoginActivity.this.runOnUiThread(() -> {
                mAccessToken = oauth2AccessToken;
                if (mAccessToken.isSessionValid()) {
                    // 显示 Token
                    updateTokenView(false);
                    // 保存 Token 到 SharedPreferences
                    AccessTokenHelper.refreshAccessToken(LoginActivity.this, mAccessToken.getAccessToken());
                    ToastUtils.show(LoginActivity.this, R.string.weibosdk_demo_toast_auth_success);
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            ToastUtils.show("微博授权出错");
        }

        @Override
        public void onCancel() {
            ToastUtils.show("微博授权取消");
        }
    }

    private void updateTokenView(boolean b) {
        if (mPresenter != null) {
            mPresenter.loginServer(mAccessToken.getAccessToken(), mAccessToken.getUid(), Constants.OAUTH_WEIBO);
        }
    }

}
