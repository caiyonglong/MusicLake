package com.cyl.musiclake.utils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static String formatTime(long time) {
        // TODO Auto-generated method stub
        if (time == 0) {
            return "00:00";
        }
        time = time / 1000;
        int m = (int) (time / 60 % 60);
        int s = (int) (time % 60);
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

    public static float Distance(double long1, double lat1, double long2,
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

        float ye = (float) Math.ceil(d);

        return ye;
    }

    public static String getTimeDifference(String starTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String endTime = dateFormat.format(new Date());
        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);

            //距当前时间大于15天时输出年月日
            if (day>15){
                timeString=dateFormat1.format(parse);
            }else if (day>0){
                timeString= day+"天前";
            }else if (hour>0){
                timeString= hour+"小时前";
            }else if (min>0){
                timeString= min+"分钟前";
            }else {
                timeString= s +"秒前";
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeString;

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

    /**
     * 毫秒转化成时间
     *
     * @return 时间
     */
    public static String distime(long time) {

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return dfs.format(date);
    }

}
