package com.music.lake.musiclib.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

/**
 * Created by master on 2018/5/14.
 * 通知栏管理类
 */

public class NotifyManager {

    private static final int NOTIFICATION_ID = 123789;
    private long mNotificationPostTime = 0;
    private static final String setAlphaMethodName = "setImageAlpha";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private Notification mNotification;


    private RemoteViews notRemoteView;
    private RemoteViews bigNotRemoteView;
    private Service mService;
    private Player basePlayerImpl;

    public NotifyManager(Service service) {
        this.mService = service;
    }

 /*//////////////////////////////////////////////////////////////////////////
    // Notification
    //////////////////////////////////////////////////////////////////////////*/

//    private void resetNotification() {
//        mNotificationBuilder = createNotification();
//    }
//
//    private NotificationCompat.Builder createNotification() {
//        notRemoteView = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.player_notification);
//        bigNotRemoteView = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.player_notification_expanded);
//
//        setupNotification(notRemoteView);
//        setupNotification(bigNotRemoteView);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.notification_channel_id))
//                .setOngoing(true)
//                .setSmallIcon(R.drawable.ic_icon)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setCustomContentView(notRemoteView)
//                .setCustomBigContentView(bigNotRemoteView)
//                .setPriority(NotificationCompat.PRIORITY_MAX);
//        return builder;
//    }
//
//    private void setupNotification(RemoteViews remoteViews) {
//        if (basePlayerImpl == null) return;
//
////        remoteViews.setTextViewText(R.id.notificationSongName, basePlayerImpl.getVideoTitle());
////        remoteViews.setTextViewText(R.id.notificationArtist, basePlayerImpl.getUploaderName());
//
//        remoteViews.setOnClickPendingIntent(R.id.notificationPlayPause,
//                PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, new Intent(ACTION_PLAY_PAUSE), PendingIntent.FLAG_UPDATE_CURRENT));
//        remoteViews.setOnClickPendingIntent(R.id.notificationStop,
//                PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, new Intent(ACTION_CLOSE), PendingIntent.FLAG_UPDATE_CURRENT));
//
//        // Starts background player activity -- attempts to unlock lockscreen
//        final Intent intent = NavigationHelper.INSTANCE.getNowPlayingIntent(mContext);
//        remoteViews.setOnClickPendingIntent(R.id.notificationContent,
//                PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT));
//
//        if (true) {
//            remoteViews.setOnClickPendingIntent(R.id.notificationFRewind,
//                    PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, new Intent(ACTION_PREV), PendingIntent.FLAG_UPDATE_CURRENT));
//            remoteViews.setOnClickPendingIntent(R.id.notificationFForward,
//                    PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, new Intent(ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT));
//        } else {
//            remoteViews.setOnClickPendingIntent(R.id.notificationFRewind,
//                    PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, new Intent(ACTION_PREV), PendingIntent.FLAG_UPDATE_CURRENT));
//            remoteViews.setOnClickPendingIntent(R.id.notificationFForward,
//                    PendingIntent.getBroadcast(mContext, NOTIFICATION_ID, new Intent(ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT));
//        }
//
//    }
//    private synchronized void updateNotification(int drawableId) {
//        //if (DEBUG) LogUtil.d(TAG, "updateNotification() called with: drawableId = [" + drawableId + "]");
//        if (mNotificationBuilder == null) return;
//        if (drawableId != -1) {
//            if (notRemoteView != null)
//                notRemoteView.setImageViewResource(R.id.notificationPlayPause, drawableId);
//            if (bigNotRemoteView != null)
//                bigNotRemoteView.setImageViewResource(R.id.notificationPlayPause, drawableId);
//        }
//        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
//    }
//
//    private void setControlsOpacity(@IntRange(from = 0, to = 255) int opacity) {
//        if (notRemoteView != null)
//            notRemoteView.setInt(R.id.notificationPlayPause, setAlphaMethodName, opacity);
//        if (bigNotRemoteView != null)
//            bigNotRemoteView.setInt(R.id.notificationPlayPause, setAlphaMethodName, opacity);
//        if (notRemoteView != null)
//            notRemoteView.setInt(R.id.notificationFForward, setAlphaMethodName, opacity);
//        if (bigNotRemoteView != null)
//            bigNotRemoteView.setInt(R.id.notificationFForward, setAlphaMethodName, opacity);
//        if (notRemoteView != null)
//            notRemoteView.setInt(R.id.notificationFRewind, setAlphaMethodName, opacity);
//        if (bigNotRemoteView != null)
//            bigNotRemoteView.setInt(R.id.notificationFRewind, setAlphaMethodName, opacity);
//    }
//
//    public void close() {
//        if (mNotificationManager != null) mNotificationManager.cancel(NOTIFICATION_ID);
//
//        stopForeground(true);
//    }





    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewMessage";


    public static void notify(final Context context,
                              final String exampleString, final int number) {

//        notify(context, builder.build());
    }

    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG.hashCode(), notification);
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int)}.
     */
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        nm.cancel(NOTIFICATION_TAG.hashCode());
    }

}
