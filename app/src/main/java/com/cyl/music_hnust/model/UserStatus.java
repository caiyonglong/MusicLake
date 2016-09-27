package com.cyl.music_hnust.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 永龙 on 2015/12/22.
 */
public class UserStatus {
    //保存个人信息到data.xml文件中
    public static boolean savaUserInfo(Context context, User userInfo){
        SharedPreferences sp=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("userID",userInfo.getUser_id());
    //    editor.putString("userPW", pw);
        editor.putString("userName", userInfo.getUser_name());
        editor.putString("userSex", userInfo.getUser_sex());
        editor.putString("userCollege", userInfo.getUser_college());
        editor.putString("userMajor", userInfo.getUser_major());
        editor.putString("userClass", userInfo.getUser_class());

        editor.putString("user_img", userInfo.getUser_img());
        editor.putString("user_email", userInfo.getUser_email());
        editor.putString("user_phone", userInfo.getPhone());
        editor.putString("nick", userInfo.getNick());
        editor.putBoolean("secret",userInfo.isSecret());

        editor.commit();
        return true;
    }
    //从data.xml文件中取出个人信息
    public static User getUserInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String userID = sp.getString("userID", "1305030212");
     //    String pw = sp.getString("userPW",null);
        String userName = sp.getString("userName","123213");
        String userSex = sp.getString("userSex","男");
        String userCollege = sp.getString("userCollege","====");
        String userMajor = sp.getString("userMajor","21132");
        String userClass = sp.getString("userClass", "121321");

        String user_img = sp.getString("user_img", null);
        String user_email = sp.getString("user_email", null);
        String user_phone = sp.getString("user_phone", null);
        boolean secret = sp.getBoolean("secret", true);
        String nick = sp.getString("nick", null);

        User user =new User();
        user.setUser_id(userID);
        user.setUser_name(userName);
        user.setUser_sex(userSex);
        user.setUser_college(userCollege);
        user.setUser_major(userMajor);
        user.setUser_class(userClass);

        user.setUser_img(user_img);
        user.setUser_email(user_email);
        user.setPhone(user_phone);
        user.setSecret(secret);
        user.setNick(nick);
        return user;
    }

    //登录状态、
    public static boolean saveuserstatus(Context context, boolean status){
        SharedPreferences sp=context.getSharedPreferences("status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putBoolean("status", status);
        editor.commit();
        return true;
    }
    public static boolean getstatus(Context context){
        SharedPreferences sp=context.getSharedPreferences("status", Context.MODE_PRIVATE);
        Boolean user_status = sp.getBoolean("status", Boolean.parseBoolean(null));
        return user_status;
    }


}
