package com.cyl.music_hnust.fragment;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.fragment.base.BaseFragment;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {

    private TextView user_name, user_num, user_college,
            user_class, user_major, nick, email, phone;

    private CardView user_logout;

    private User user ;


    @Override
    protected void listener() {
        user_logout.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        if (UserStatus.getstatus(getActivity())) {
            user = UserStatus.getUserInfo(getActivity());
            user_num.setText(user.getUser_id());
            user_name.setText(user.getUser_name());
            user_class.setText(user.getUser_class());
            user_major.setText(user.getUser_major());
            user_college.setText(user.getUser_college());
            nick.setText(user.getNick());
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
        user_num = (TextView) rootView.findViewById(R.id.user_num);
        user_name = (TextView) rootView.findViewById(R.id.user_name);
        user_class = (TextView) rootView.findViewById(R.id.user_class);
        user_major = (TextView) rootView.findViewById(R.id.user_major);
        user_college = (TextView) rootView.findViewById(R.id.user_college);

        nick = (TextView) rootView.findViewById(R.id.nick);
        email = (TextView) rootView.findViewById(R.id.email);
        phone = (TextView) rootView.findViewById(R.id.phone);

        user_logout = (CardView) rootView.findViewById(R.id.logout);
    }

    @Override
    public void onClick(View v) {

    }
}

