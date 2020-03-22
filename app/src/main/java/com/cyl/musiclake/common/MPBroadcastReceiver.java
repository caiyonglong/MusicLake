package com.cyl.musiclake.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cyl.musiclake.data.PlayHistoryLoader;
import com.cyl.musiclake.data.PlayQueueLoader;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.ui.music.playpage.PlayerActivity;
import com.cyl.musiclake.utils.SPUtils;
import com.music.lake.musiclib.MusicPlayerManager;
import com.music.lake.musiclib.notification.NotifyManager;
import com.music.lake.musiclib.service.MusicPlayerService;
import com.music.lake.musiclib.utils.LogUtil;
import com.music.lake.musiclib.utils.ToastUtils;

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
            SPUtils.setPlayPosition(MusicPlayerManager.getInstance().getNowPlayingIndex());
        } else if (MusicPlayerService.PLAY_QUEUE_CHANGE.equals(intent.getAction())) {
            EventBus.getDefault().post(new PlaylistEvent(Constants.PLAYLIST_QUEUE_ID, null));
            PlayQueueLoader.INSTANCE.updateQueue(MusicPlayerManager.getInstance().getPlayList());
        } else if (NotifyManager.ACTION_MUSIC_NOTIFY.equals(intent.getAction())) {
            toPlayerActivity(context);
        } else if (NotifyManager.ACTION_LYRIC.equals(intent.getAction())) {
            showDesktopLyric(context);
        } else if (NotifyManager.ACTION_CLOSE.equals(intent.getAction())) {
            close(context);
        }
    }

    private void toPlayerActivity(Context context) {
        //通知栏点击
        Intent intent1 = new Intent(context, PlayerActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }

    private void showDesktopLyric(Context context) {
        //通知栏点击
        ToastUtils.show("显示桌面歌词");
    }

    private void close(Context context) {
        //通知栏点击
    }
}