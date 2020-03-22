package com.music.lake.musiclib.widgets.appwidgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import com.music.lake.musiclib.service.MusicPlayerService
import com.music.lake.musiclib.utils.LogUtil


/**
 * Created by nv95 on 02.11.16.
 */

abstract class BaseWidget : AppWidgetProvider() {

    protected val REQUEST_NEXT = 1
    protected val REQUEST_PREV = 2
    protected val REQUEST_PLAYPAUSE = 3
    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        onUpdate(context, appWidgetManager, appWidgetIds, null, MusicPlayerService.META_CHANGED)
    }

    private fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray, extras: Bundle?, action: String) {
        val serviceName = ComponentName(context, MusicPlayerService::class.java)
        val remoteViews = RemoteViews(context.packageName, getLayoutRes())
        try {
            if (MusicPlayerService.META_CHANGED == action) {
                onViewsUpdate(context, remoteViews, serviceName, extras)
            } else {
                onViewsPlayStatus(context, remoteViews, serviceName, extras)
            }
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        LogUtil.e("BaseWidget", "接收到广播-------------" + action!!)
        if (action.startsWith("com.cyl.music_lake.")) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, this.javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            onUpdate(context, appWidgetManager, appWidgetIds, intent.extras, action)
        } else {
            super.onReceive(context, intent)
        }
    }

    abstract fun onViewsPlayStatus(context: Context, remoteViews: RemoteViews, serviceName: ComponentName, extras: Bundle?)

    abstract fun onViewsUpdate(context: Context, remoteViews: RemoteViews, serviceName: ComponentName, extras: Bundle?)

}
