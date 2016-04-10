package com.cyl.music_hnust.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * <br>
 * <b>进行一些转换工作</b></br>
 *
 * @author CWD
 * @version 2013.05.18 v1.0
 */
public class FormatUtil {

    /**
     * 格式化时间
     *
     * @param time 时间值
     * @return 时间
     */
    public static String formatTime(String time) {
        // TODO Auto-generated method stub
        int time1 = Integer.parseInt(time);
        if (time1 == 0) {
            return "00:00";
        }
        time1 = time1 / 1000;
        int m = time1 / 60 % 60;
        int s = time1 % 60;
        return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
    }

    /**
     * 格式化文件大小
     *
     * @param size 文件大小值
     * @return 文件大小
     */
    public static String formatSize(long size) {
        // TODO Auto-generated method stub
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSize = "0B";
        if (size < 1024) {
            fileSize = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSize = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSize = df.format((double) size / 1048576) + "MB";
        } else {
            fileSize = df.format((double) size / 1073741824) + "GB";
        }
        return fileSize;
    }

    /**
     * 对乱码的处理
     *
     * @param s 原字符串
     * @return GBK处理后的数据
     */
    public static String formatGBKStr(String s) {
        String str = null;
        try {
            str = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public static String Distance(double long1, double lat1, double long2,
                                  double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));

        int ye = (int) Math.ceil(d);

        if (ye > 100) {
            return ye / 1000 + " km";
        } else {
            return ye + " m";
        }
    }

    public static String distime(String pre) {

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //  Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        java.util.Date begin = null;

        String date = dfs.format(new java.util.Date());

        long between = 0;
        try {
            begin = dfs.parse(pre);
            java.util.Date now = dfs.parse(date);
            between = (now.getTime() - begin.getTime());// 得到两者的毫秒数
            Log.e("time", between + "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (1000 * 60 * 60 * 24);
        long hour = (between - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long min = (between - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);


        long daynow = begin.getTime() / (24 * 60 * 60 * 1000);
        long hournow = (begin.getTime() / (60 * 60 * 1000) - daynow * 24);
        long minnow = ((begin.getTime() / (60 * 1000)) - hournow * 24 * 60 - hournow * 60);

//        System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" ;
        if (day == 0 && hour == 0 && min == 0) {
            return s+"秒前";
        } else if (day == 0 && hour == 0) {
            return min + " 分钟前";
        } else if (day == 0) {
            return  hour + " 小时前";
        } else {
            if (day == 1)
                return "昨天";
            else
                return day + " 天前" ;
        }


    }

    /**
     * 获取当前时间
     *
     * @return 时间
     */
    public static String getTime() {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = dfs.format(new java.util.Date());
        return date;
    }

}
