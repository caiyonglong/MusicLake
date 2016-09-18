package com.cyl.music_hnust.model;

import android.content.Context;

import java.util.Calendar;

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
}
