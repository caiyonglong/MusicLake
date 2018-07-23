package com.cyl.musiclake.view.desktop;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.player.MusicPlayerService;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.CoverLoader;


/**
 * Created by nv95 on 08.07.16.
 */

public class StandardWidget extends BaseWidget {

    private boolean isFirstCreate = true;

    @Override
    int getLayoutRes() {
        return R.layout.widget_standard;
    }

    @Override
    void onViewsUpdate(Context context, RemoteViews remoteViews, ComponentName serviceName, Bundle extras) {
        if (isFirstCreate) {
            remoteViews.setOnClickPendingIntent(R.id.iv_next, PendingIntent.getService(
                    context,
                    REQUEST_NEXT,
                    new Intent(context, MusicPlayerService.class)
                            .setAction(MusicPlayerService.ACTION_NEXT)
                            .setComponent(serviceName),
                    0
            ));
            remoteViews.setOnClickPendingIntent(R.id.iv_prev, PendingIntent.getService(
                    context,
                    REQUEST_PREV,
                    new Intent(context, MusicPlayerService.class)
                            .setAction(MusicPlayerService.ACTION_PREV)
                            .setComponent(serviceName),
                    0
            ));
            remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, PendingIntent.getService(
                    context,
                    REQUEST_PLAYPAUSE,
                    new Intent(context, MusicPlayerService.class)
                            .setAction(MusicPlayerService.ACTION_PLAY_PAUSE)
                            .setComponent(serviceName),
                    0
            ));
            remoteViews.setOnClickPendingIntent(R.id.iv_cover, PendingIntent.getActivity(
                    context,
                    0,
                    NavigationHelper.INSTANCE.getNowPlayingIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ));

            remoteViews.setOnClickPendingIntent(R.id.iv_lyric, PendingIntent.getService(
                    context,
                    0,
                    NavigationHelper.INSTANCE.getLyricIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ));
        }

        remoteViews.setTextViewText(R.id.tv_title, PlayManager.getSongName() + "-" + PlayManager.getSongArtist());
        remoteViews.setImageViewResource(R.id.iv_play_pause,
                PlayManager.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
        if (PlayManager.getPlayingMusic() != null) {
            CoverLoader.loadImageViewByMusic(MusicApp.getAppContext(), PlayManager.getPlayingMusic(), artwork -> {
                if (artwork != null) {
                    remoteViews.setImageViewBitmap(R.id.iv_cover, artwork);
                } else {
                    remoteViews.setImageViewResource(R.id.iv_cover, R.drawable.default_cover);
                }
            });
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        isFirstCreate = false;
    }
}
