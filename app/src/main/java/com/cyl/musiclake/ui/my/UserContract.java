package com.cyl.musiclake.ui.my;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.ui.my.user.User;

import java.util.Map;

/**
 * Created by D22434 on 2018/1/3.
 */

public interface UserContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void updateView(User user);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void updateInfo(Map<String, String> params);

        void uploadHeader(String user_id, String path);

        void getUserInfo();

        void logout();
    }

    interface Model {
        void savaInfo(User userInfo);

        User getUserInfo();

        void cleanInfo();
    }
}
