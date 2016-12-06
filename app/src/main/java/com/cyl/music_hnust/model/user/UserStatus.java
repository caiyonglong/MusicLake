package com.cyl.music_hnust.model.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.cyl.music_hnust.utils.Constants;

/**
 * Created by 永龙 on 2015/12/22.
 */
public class UserStatus {
    //保存个人信息到user.xml文件中
    public static boolean savaUserInfo(Context context, User userInfo){
        SharedPreferences sp=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString(Constants.USER_ID,userInfo.getUser_id());
        editor.putString(Constants.PASSWORD, userInfo.getPassword());
        editor.putString(Constants.USERNAME, userInfo.getUser_name());
        editor.putString(Constants.USER_SEX, userInfo.getUser_sex());
        editor.putString(Constants.USER_COLLEGE, userInfo.getUser_college());
        editor.putString(Constants.USER_MAJOR, userInfo.getUser_major());
        editor.putString(Constants.USER_CLASS, userInfo.getUser_class());

        editor.putString(Constants.USER_IMG, userInfo.getUser_img());
        editor.putString(Constants.USER_EMAIL, userInfo.getUser_email());
        editor.putString(Constants.PHONE, userInfo.getPhone());
        editor.putString(Constants.NICK, userInfo.getNick());
        editor.putInt(Constants.SECRET,userInfo.getSecret());

        editor.commit();
        return true;
    }
    //从data.xml文件中取出个人信息
    public static User getUserInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        User user =new User();
        user.setUser_id(sp.getString(Constants.USER_ID,null));
        user.setUser_name(sp.getString(Constants.USERNAME,null));
        user.setPassword(sp.getString(Constants.PASSWORD,null));
        user.setUser_sex(sp.getString(Constants.USER_SEX,null));
        user.setUser_college(sp.getString(Constants.USER_COLLEGE,null));
        user.setUser_major(sp.getString(Constants.USER_MAJOR,null));
        user.setUser_class(sp.getString(Constants.USER_CLASS,null));
        user.setUser_img(sp.getString(Constants.USER_IMG,null));
        user.setUser_email(sp.getString(Constants.USER_EMAIL,null));
        user.setPhone(sp.getString(Constants.PHONE,null));
        user.setNick(sp.getString(Constants.NICK,null));
        user.setSecret(sp.getInt(Constants.SECRET,0));
        return user;
    }
    //从data.xml文件中清空个人信息
    public static void clearUserInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
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
        Boolean user_status = sp.getBoolean("status", false);
        return user_status;
    }


}
