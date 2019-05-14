package com.cyl.musiclake.ui.widget.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.cyl.musiclake.R
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.LogUtil


/**
 * Created by nv95 on 08.07.16.
 */

class StandardWidget : BaseWidget() {

    private var isFirstCreate = true

    override fun getLayoutRes(): Int {
        return R.layout.widget_standard
    }

    internal override fun onViewsUpdate(context: Context, remoteViews: RemoteViews, serviceName: ComponentName, extras: Bundle?) {
        LogUtil.e("BaseWidget", "接收到广播------------- onViewsUpdate")
        if (isFirstCreate) {
            remoteViews.setOnClickPendingIntent(R.id.iv_next, PendingIntent.getService(
                    context,
                    REQUEST_NEXT,
                    Intent(context, MusicPlayerService::class.java)
                            .setAction(MusicPlayerService.ACTION_NEXT)
                            .setComponent(serviceName),
                    0
            ))
            remoteViews.setOnClickPendingIntent(R.id.iv_prev, PendingIntent.getService(
                    context,
                    REQUEST_PREV,
                    Intent(context, MusicPlayerService::class.java)
                            .setAction(MusicPlayerService.ACTION_PREV)
                            .setComponent(serviceName),
                    0
            ))
            remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, PendingIntent.getService(
                    context,
                    REQUEST_PLAYPAUSE,
                    Intent(context, MusicPlayerService::class.java)
                            .setAction(MusicPlayerService.ACTION_PLAY_PAUSE)
                            .setComponent(serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ))
            remoteViews.setOnClickPendingIntent(R.id.iv_cover, PendingIntent.getActivity(
                    context,
                    0,
                    NavigationHelper.getNowPlayingIntent(context)
                            .setComponent(serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ))

            remoteViews.setOnClickPendingIntent(R.id.iv_lyric, PendingIntent.getService(
                    context,
                    0,
                    NavigationHelper.getLyricIntent(context)
                            .setComponent(serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ))
            isFirstCreate = false;

        }
        if (extras != null) {
            remoteViews.setImageViewResource(R.id.iv_play_pause,
                    if (extras.getBoolean(Extras.PLAY_STATUS, false)) R.drawable.ic_pause else R.drawable.ic_play)
        }
        if (MusicPlayerService.getInstance() != null) {
            val music = MusicPlayerService.getInstance().playingMusic ?: return
            remoteViews.setTextViewText(R.id.tv_title, music.title + " - " + music.artist)
            CoverLoader.loadImageViewByMusic(context, music) { artwork ->
                if (artwork != null) {
                    remoteViews.setImageViewBitmap(R.id.iv_cover, artwork)
                } else {
                    remoteViews.setImageViewResource(R.id.iv_cover, R.drawable.default_cover)
                }
            }
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
