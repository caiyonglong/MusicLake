package com.music.lake.musiclib.widgets.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.music.lake.musiclib.R
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
        }
    }

    override fun onViewsUpdate(context: Context, remoteViews: RemoteViews, serviceName: ComponentName, extras: Bundle?) {
        LogUtil.e("StandardWidget", "接收到广播------------- onViewsUpdate")
        if (isFirstCreate) {
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_next, PendingIntent.getService(
                    context,
                    REQUEST_NEXT,
                    retrievePlaybackAction(context, NotifyManager.ACTION_NEXT, serviceName),
                    0
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_prev, PendingIntent.getService(
                    context,
                    REQUEST_PREV,
                    retrievePlaybackAction(context, NotifyManager.ACTION_PREV, serviceName),
                    0
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_play_pause, PendingIntent.getService(
                    context,
                    REQUEST_PLAYPAUSE,
                    retrievePlaybackAction(context, NotifyManager.ACTION_PLAY_PAUSE, serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_cover,
                    PendingIntent.getActivity(
                            context,
                            0,
                            retrieveAction(context, NotifyManager.ACTION_MUSIC_NOTIFY, serviceName),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    ))
            remoteViews.setOnClickPendingIntent(R.id.app_widgets_lyric, PendingIntent.getBroadcast(
                    context,
                    0,
                    retrieveAction(context, NotifyManager.ACTION_LYRIC, serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
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

    private fun retrieveAction(context: Context, action: String, serviceName: ComponentName): Intent {
        return Intent(context, MusicPlayerService::class.java)
                .setAction(action)
                .setComponent(serviceName)
    }

    private fun retrievePlaybackAction(context: Context, action: String, serviceName: ComponentName): Intent {
        return Intent(context, MusicPlayerService::class.java)
                .setAction(action)
                .setComponent(serviceName)
    }


    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        LogUtil.e("BaseWidget", "接收到广播------------- 第一次创建")
        isFirstCreate = true
        val intent = Intent(context, MusicPlayerService::class.java)
        context.startService(intent)
    }
}
