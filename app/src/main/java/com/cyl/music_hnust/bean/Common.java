package com.cyl.music_hnust.bean;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by 永龙 on 2016/3/16.
 */
public class Common {
    public static String getDate(Context context){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String date =year+"-"+month+"-"+day+" "+hour+":"+minute;
        return date;
    }
//    public static User getUserinfo(Context context){
//        User user =new User();
//        Map userinfo = UserStatus.getUserInfo(context);
//        user.setUser_name(userinfo.get("userName").toString());
//        user.setUser_id(userinfo.get("userID").toString());
//        user.setUser_img("http://1.hcyl.sinaapp.com/music_BBS/img/1138359.png");
//        user.setSignature("你好");
//        user.setUser_email("643872807@qq.com");
//        user.setUser_sex("男");
//        return user;
//    }
}
