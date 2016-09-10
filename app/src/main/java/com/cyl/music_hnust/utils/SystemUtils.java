package com.cyl.music_hnust.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.activity.MainActivity;
import com.cyl.music_hnust.model.Music;

import java.text.DecimalFormat;

/**
 * 作者：yonglong on 2016/8/12 16:45
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class SystemUtils {

    private NotificationManager mNotificationManager;
    public static final String BROADCAST_ACTION_SERVICE = "com.cyl.music_hnust.service";// 广播标志
    public static final String NOTIFICATION_ACTION_NEXT = "com.cyl.music_hnust.notify.next";// 广播标志
    public static final String NOTIFICATION_ACTION_PLAY = "com.cyl.music_hnust.notify.play";// 广播标志


    public NotificationManager getmNotificationManager() {
        return mNotificationManager;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Notification createNotification(Context context, Music music) {
        String title = music.getTitle();
        String subtitle = music.getArtist()==null ? music.getArtist(): music.getAlbum();
//        Bitmap cover;
//        if (music.getType() == Music.Type.LOCAL) {
//            cover = CoverLoader.getInstance().loadThumbnail(music.getCoverUri());
//        } else {
//            cover = music.getCover();
//        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("notify", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.ic_launcher)
//                .setLargeIcon(cover)
                .build();
        return builder;
    }
    //判断是否是android 6.0
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    //判断是否是android 5.0
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    //判断是否是android 4.0
    public static boolean isKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

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
     * 判断Service是否在运行
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
