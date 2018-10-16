package com.cyl.musiclake.player;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.session.MediaSession;

import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SystemUtils;

import static com.cyl.musiclake.player.MusicPlayerService.AUDIO_FOCUS_CHANGE;

/**
 * 音频管理类
 * 主要用来管理音频焦点
 */

public class AudioAndFocusManager {

    private AudioManager mAudioManager;
    private ComponentName mediaButtonReceiverComponent;
    private PendingIntent mPendingIntent;
    private MediaSession mediaSession;
    private MusicPlayerService.MusicPlayerHandler mHandler;


    public AudioAndFocusManager(Context mContext, MusicPlayerService.MusicPlayerHandler mHandler) {
        this.mHandler = mHandler;
        initAudioManager(mContext);
    }

    /**
     * 初始化AudioManager&Receiver
     *
     * @param mContext
     */
    private void initAudioManager(Context mContext) {
        mediaSession = new MediaSession(mContext, "AudioAndFocusManager");
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mediaButtonReceiverComponent = new ComponentName(mContext.getPackageName(),
                MediaButtonIntentReceiver.class.getName());
        mContext.getPackageManager().setComponentEnabledSetting(mediaButtonReceiverComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        mAudioManager.registerMediaButtonEventReceiver(mediaButtonReceiverComponent);
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mediaButtonReceiverComponent);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                mediaButtonIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mediaSession.setMediaButtonReceiver(mPendingIntent);
    }

    /**
     * 请求音频焦点
     */
    public void requestAudioFocus() {
        if (SystemUtils.isO()) {
            AudioFocusRequest mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener)
                    .build();
            int res = mAudioManager.requestAudioFocus(mAudioFocusRequest);
            if (res == 1) {
                LogUtil.e("requestAudioFocus=" + true);
            }
        } else {
            if (audioFocusChangeListener != null) {
                boolean result = AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                        mAudioManager.requestAudioFocus(audioFocusChangeListener,
                                AudioManager.STREAM_MUSIC,
                                AudioManager.AUDIOFOCUS_GAIN);
                LogUtil.e("requestAudioFocus=" + result);
            }
        }
    }

    /**
     * 关闭音频焦点
     */
    public void abandonAudioFocus() {
        if (audioFocusChangeListener != null) {
            boolean result = AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    mAudioManager.abandonAudioFocus(audioFocusChangeListener);
            LogUtil.e("requestAudioFocus=" + result);
        }
    }


    /**
     * 音频焦点改变监听器
     */
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            LogUtil.e("OnAudioFocusChangeListener", focusChange + "---");
            mHandler.obtainMessage(AUDIO_FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };

}
