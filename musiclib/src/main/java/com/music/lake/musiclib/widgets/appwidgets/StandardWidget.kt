package com.music.lake.musiclib.widgets.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import com.music.lake.musiclib.R
import com.music.lake.musiclib.manager.PlayListManager
import com.music.lake.musiclib.notification.NotifyManager
import com.music.lake.musiclib.service.MusicPlayerService
import com.music.lake.musiclib.utils.LogUtil

class StandardWidget : BaseWidget() {

    private var isFirstCreate = true

    override fun getLayoutRes(): Int {
        return R.layout.widget_standard
    }

    override fun onViewsPlayStatus(context: Context, remoteViews: RemoteViews, serviceName: ComponentName, extras: Bundle?) {
        if (extras != null) {
            remoteViews.setImageViewResource(R.id.app_widgets_play_pause,
                    if (extras.getBoolean(MusicPlayerService.PLAY_STATE_CHANGED, false)) R.drawable.ic_pause else R.drawable.ic_play)
            val id = when (extras.getInt(PlayListManager.PLAY_MODE, PlayListManager.PLAY_MODE_RANDOM)) {
                PlayListManager.PLAY_MODE_LOOP -> R.drawable.ic_repeat
                PlayListManager.PLAY_MODE_REPEAT -> R.drawable.ic_repeat_one
                else -> R.drawable.ic_shuffle
            }
            remoteViews.setImageViewResource(R.id.app_widgets_shuffle, id)
        }
    }

    override fun onViewsUpdate(context: Context, remoteViews: RemoteViews, serviceName: ComponentName, extras: Bundle?) {
        LogUtil.e("StandardWidget", "接收到广播------------- onViewsUpdate")
        if (isFirstCreate) {
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_next, retrievePlaybackAction(
                    context,
                    1,
                    serviceName
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_prev, retrievePlaybackAction(
                    context,
                    2,
                    serviceName
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_play_pause, retrievePlaybackAction(
                    context,
                    3,
                    serviceName
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_cover, retrievePlaybackAction(
                    context,
                    4,
                    serviceName
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_lyric, retrievePlaybackAction(
                    context,
                    5,
                    serviceName
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_shuffle, retrievePlaybackAction(
                    context,
                    6,
                    serviceName
            ))
            isFirstCreate = false;
        }
        if (extras != null) {
            remoteViews.setImageViewResource(R.id.app_widgets_play_pause,
                    if (extras.getBoolean(MusicPlayerService.PLAY_STATE_CHANGED, false)) R.drawable.ic_pause else R.drawable.ic_play)
        }
        if (MusicPlayerService.getInstance() != null) {
            val music = MusicPlayerService.getInstance().playingMusic ?: return
            remoteViews.setTextViewText(R.id.app_widgets_title, music.title + " - " + music.artist)
            if (MusicPlayerService.getInstance().coverBitmap != null) {
                remoteViews.setImageViewBitmap(R.id.app_widgets_cover, MusicPlayerService.getInstance().coverBitmap)
            }
        }
    }

    private fun retrievePlaybackAction1(context: Context, action: String, serviceName: ComponentName): Intent {
        return Intent(context, MusicPlayerService::class.java)
                .setAction(action)
                .setComponent(serviceName)
    }

    private fun retrievePlaybackAction(context: Context, requestCode: Int, serviceName: ComponentName): PendingIntent {
        val action = when (requestCode) {
            1 -> {
                NotifyManager.ACTION_NEXT
            }
            2 -> {
                NotifyManager.ACTION_PREV
            }
            3 -> {
                NotifyManager.ACTION_PLAY_PAUSE
            }
            4 -> {
                NotifyManager.ACTION_MUSIC_NOTIFY
            }
            5 -> {
                NotifyManager.ACTION_LYRIC
            }
            else -> NotifyManager.ACTION_SHUFFLE
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                    context,
                    requestCode,
                    retrievePlaybackAction1(context, action, serviceName),
                    0
            )
        } else {
            PendingIntent.getService(
                    context,
                    requestCode,
                    retrievePlaybackAction1(context, action, serviceName),
                    0
            )
        }
    }


    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        LogUtil.e("BaseWidget", "接收到广播------------- 第一次创建")
        isFirstCreate = true
        val intent = Intent(context, MusicPlayerService::class.java)
        context.startService(intent)
    }
}
