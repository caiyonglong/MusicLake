package com.cyl.musiclake.ui.login;

import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;
import com.cyl.musiclake.ui.login.user.User;

import java.util.Map;

/**
 * Created by D22434 on 2018/1/3.
 */

public interface UserContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void updateView(User user);

    }

    interface Presenter extends BasePresenter<UserContract.View> {
        void updateInfo(Map<String, String> params);

        void uploadHeader(String user_id, String path);

        void getUserInfo();
    }

    interface Model {
        void savaInfo(User userInfo);

        User getUserInfo();

        void cleanInfo();
    }
}
