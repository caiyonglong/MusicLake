package com.cyl.musiclake.ui.login;

import android.content.Context;

import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.login.user.UserStatus;

/**
 * Created by D22434 on 2018/1/3.
 */

public class UserModel implements UserContract.Model {

    private Context mContext;

    public UserModel(Context context) {
        mContext = context;
    }

    @Override
    public void savaInfo(User userInfo) {
        //保存用户信息
        UserStatus.savaUserInfo(mContext, userInfo);
        UserStatus.saveuserstatus(mContext, true);
    }

    @Override
    public User getUserInfo() {
        if (UserStatus.getstatus(mContext)) {
            return UserStatus.getUserInfo(mContext);
        } else {
            return null;
        }
    }

    @Override
    public void cleanInfo() {
        UserStatus.clearUserInfo(mContext);
        UserStatus.saveuserstatus(mContext, false);
    }
}
