package com.cyl.music_hnust.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.user.User;
import com.cyl.music_hnust.model.user.UserStatus;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {

    private TextView nick, email, phone;

    private CardView user_logout;

    private User user;


    @Override
    protected void listener() {
        user_logout.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        if (UserStatus.getstatus(getActivity())) {

            user = UserStatus.getUserInfo(getActivity());
            if (user.getNick()==null||user.getNick().length()<=0){
                nick.setText("");
            }else {
                nick.setText(user.getNick());
            }
            email.setText(user.getUser_email());
            phone.setText(user.getPhone());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_user;
    }

    @Override
    public void initViews() {

        nick = (TextView) rootView.findViewById(R.id.nick);
        email = (TextView) rootView.findViewById(R.id.email);
        phone = (TextView) rootView.findViewById(R.id.phone);

        user_logout = (CardView) rootView.findViewById(R.id.logout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                new AlertDialog.Builder(getActivity())
                        .setTitle("是否注销？")
                        .setMessage("注销后不能享有更多功能！")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                logout();
                            }
                        })
                        .show();
                break;
        }
    }

    private void logout() {
        UserStatus.clearUserInfo(getActivity());
        UserStatus.saveuserstatus(getActivity(), false);
        getActivity().finish();
    }
}

