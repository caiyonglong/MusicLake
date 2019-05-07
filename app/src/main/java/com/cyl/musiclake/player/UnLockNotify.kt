package com.cyl.musiclake.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R

/**
 * 作者：yonglong
 * 包名：com.cyl.musiclake.player
 * 时间：2019/5/7 23:49
 * 描述：
 */
class UnLockNotify {
    private val mContext: Context = MusicApp.getAppContext()
    private val mNotificationManager: NotificationManager

    init {
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initNotificationChanel()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun initNotificationChanel() {
        val notificationChannel = NotificationChannel(UNLOCK_NOTIFICATION_CHANNEL_ID, mContext.getString(R.string.unlock_notification), NotificationManager.IMPORTANCE_LOW)
        notificationChannel.setShowBadge(false)
        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)
        notificationChannel.description = mContext.getString(R.string.unlock_notification_description)
        mNotificationManager.createNotificationChannel(notificationChannel)
    }

    /**
     * 显示解锁通知栏
     */
    fun notifyToUnlock() {
        val notification = NotificationCompat.Builder(mContext, UNLOCK_NOTIFICATION_CHANNEL_ID)
                .setContentText(mContext.getString(R.string.float_lock))
                .setContentTitle(mContext.getString(R.string.click_to_unlock))
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setTicker(mContext.getString(R.string.float_lock_ticker))
                .setContentIntent(buildPendingIntent())
                .setSmallIcon(R.drawable.ic_music)
                .build()
        mNotificationManager.notify(UNLOCK_NOTIFICATION_ID, notification)
    }

    fun cancel() {
        mNotificationManager.cancel(UNLOCK_NOTIFICATION_ID)
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(MusicPlayerService.SERVICE_CMD)
        intent.putExtra(MusicPlayerService.CMD_NAME, MusicPlayerService.UNLOCK_DESKTOP_LYRIC)
        intent.component = ComponentName(mContext, MusicPlayerService::class.java)
        return PendingIntent.getService(mContext, 0x1123, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        private val UNLOCK_NOTIFICATION_CHANNEL_ID = "unlock_notification"
        private val UNLOCK_NOTIFICATION_ID = 2
    }
}