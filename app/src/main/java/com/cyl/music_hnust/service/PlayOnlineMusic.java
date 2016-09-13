package com.cyl.music_hnust.service;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.cyl.music_hnust.Json.JsonCallback;
import com.cyl.music_hnust.R;
import com.cyl.music_hnust.model.DownloadInfo;
import com.cyl.music_hnust.model.Music;
import com.cyl.music_hnust.model.OnlineMusicInfo;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.FileUtils;
import com.cyl.music_hnust.utils.NetworkUtils;
import com.cyl.music_hnust.utils.Preferences;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * 作者：yonglong on 2016/9/9 03:59
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public abstract class PlayOnlineMusic {
    private Context mContext;
    private OnlineMusicInfo mOnlineMusic;
    private int mCounter = 0;

    public PlayOnlineMusic(Context context, OnlineMusicInfo mOnlineMusic) {
        this.mContext = context;
        this.mOnlineMusic = mOnlineMusic;
    }

    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay();
        if (NetworkUtils.is4G(mContext) && !mobileNetworkPlay) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.play_tips);
            builder.setPositiveButton(R.string.play_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Preferences.saveMobileNetworkPlay(true);
                    getPlayInfo();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            getPlayInfo();
        }
    }

    private void getPlayInfo() {
        onPrepare();
        String lrcFileName = FileUtils.getLrcFileName(mOnlineMusic.getArtist_name(), mOnlineMusic.getTitle());
        File lrcFile = new File(FileUtils.getLrcDir() + lrcFileName);
        if (TextUtils.isEmpty(mOnlineMusic.getLrclink()) || lrcFile.exists()) {
            mCounter++;
        }
        String picUrl = TextUtils.isEmpty(mOnlineMusic.getPic_big()) ? TextUtils.isEmpty(mOnlineMusic.getPic_small())
                ? null : mOnlineMusic.getPic_small() : mOnlineMusic.getPic_big();
        if (TextUtils.isEmpty(picUrl)) {
            mCounter++;
        }
        final Music music = new Music();
        music.setType(Music.Type.ONLINE);
        music.setTitle(mOnlineMusic.getTitle());
        music.setArtist(mOnlineMusic.getArtist_name());
        music.setAlbum(mOnlineMusic.getAlbum_title());
        // 获取歌曲播放链接
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC)
                .addParams(Constants.PARAM_SONG_ID, mOnlineMusic.getSong_id())
                .build()
                .execute(new JsonCallback<DownloadInfo>(DownloadInfo.class) {
                    @Override
                    public void onResponse(final DownloadInfo response) {
                        if (response == null) {
                            onFail(null, null);
                            return;
                        }
                        music.setUri(response.getBitrate().getFile_link());
                        music.setDuration(response.getBitrate().getFile_duration() * 1000);
                        mCounter++;
                        if (mCounter == 3) {
                            onSuccess(music);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onFail(call, e);
                    }
                });
        // 下载歌词
        if (!TextUtils.isEmpty(mOnlineMusic.getLrclink()) && !lrcFile.exists()) {
            OkHttpUtils.get().url(mOnlineMusic.getLrclink()).build()
                    .execute(new FileCallBack(FileUtils.getLrcDir(), lrcFileName) {
                        @Override
                        public void inProgress(float progress, long total) {
                        }

                        @Override
                        public void onResponse(File response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }

                        @Override
                        public void onAfter() {
                            mCounter++;
                            if (mCounter == 3) {
                                onSuccess(music);
                            }
                        }
                    });
        }
        // 下载歌曲封面
        if (!TextUtils.isEmpty(picUrl)) {
            OkHttpUtils.get().url(picUrl).build()
                    .execute(new BitmapCallback() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            music.setCover(bitmap);
                            mCounter++;
                            if (mCounter == 3) {
                                onSuccess(music);
                            }
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            mCounter++;
                            if (mCounter == 3) {
                                onSuccess(music);
                            }
                        }
                    });
        }
    }

    public abstract void onPrepare();

    public abstract void onSuccess(Music music);

    public abstract void onFail(Call call, Exception e);
}


