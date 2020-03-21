package com.cyl.musiclake.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.music.lake.musiclib.MusicPlayerManager;
import com.music.lake.musiclib.service.MusicPlayerService;
import com.music.lake.musiclib.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

public class MPBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "MusicPlayerBroadCaster";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, "MPBroadcastReceiver =" + intent.getAction());
        if (MusicPlayerService.META_CHANGED.equals(intent.getAction())) {
            EventBus.getDefault().post(new MetaChangedEvent(MusicPlayerManager.getInstance().getNowPlayingMusic()));
            PlayHistoryLoader.INSTANCE.addSongToHistory(MusicPlayerManager.getInstance().getNowPlayingMusic());
            EventBus.getDefault().post(new PlaylistEvent(Constants.PLAYLIST_HISTORY_ID, null));
        } else if (MusicPlayerService.PLAY_QUEUE_CHANGE.equals(intent.getAction())) {
            EventBus.getDefault().post(new PlaylistEvent(Constants.PLAYLIST_QUEUE_ID, null));
        }
    }
}