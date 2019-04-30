package com.cyl.musiclake.ui.my;


import com.cyl.musiclake.api.ApiModel;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.api.net.ApiManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by D22434 on 2018/1/3.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    @Inject
    public RegisterPresenter() {
    }

    @Override
    public void register(String email, String username, String password) {
        mView.showLoading();
        mView.updateView();
        // TODO: 检查　
        if (!isEmail(email)) {
            mView.showErrorInfo(1, "请输入正确的邮箱");
        } else if (username.length() <= 0) {
            mView.showErrorInfo(2, "昵称不能为空");
        } else if (!validatePassword(password)) {
            mView.showErrorInfo(3, "密码长度需为8~16");
        } else {
            //md5加密
//        password =
            //封装
            Map<String, String> params = new HashMap<>();
            params.put(Constants.USER_EMAIL, email);
            params.put(Constants.NICK, username);
            params.put(Constants.PASSWORD, password);
            register(params);
        }
    }


    private void register(Map<String, String> params) {
        ApiManager.getInstance().apiService
                .getUserInfo(Constants.LOGIN_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiModel result) {
                        if (result.getStatus().equals("success")) {
                            //保存用户信息
                            mView.showSuccessInfo("登录成功");
                        } else {
                            mView.showErrorInfo(0, result.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    private boolean isEmail(String strEmail) {
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    //判断密码是否合法
    public boolean validatePassword(String password) {
        return password.length() > 5 && password.length() <= 18;
    }


}
