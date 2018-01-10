//package com.cyl.musiclake.service;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.text.TextUtils;
//
//import com.cyl.musiclake.callback.JsonCallback;
//import com.cyl.musiclake.R;
//import com.cyl.musiclake.ui.download.download.DownloadInfo;
//import com.cyl.musiclake.ui.music.model.Lrc;
//import com.cyl.musiclake.ui.music.model.Music;
//import com.cyl.musiclake.ui.onlinemusic.model.SearchMusic;
//import com.cyl.musiclake.utils.Constants;
//import com.cyl.musiclake.utils.FileUtils;
//import com.cyl.musiclake.utils.NetworkUtils;
//import com.cyl.musiclake.utils.PreferencesUtils;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import okhttp3.Call;
//
///**
// * 播放搜索的音乐
// * Created by hzwangchenyan on 2016/1/13.
// */
//public abstract class PlaySearchedMusic {
//    private Context mContext;
//    private SearchMusic.SongList songList;
//    private int mCounter = 0;
//
//    public PlaySearchedMusic(Context context, SearchMusic.SongList songList) {
//        this.mContext = context;
//        this.songList = songList;
//    }
//
//    public void execute() {
//        checkNetwork();
//    }
//
//    private void checkNetwork() {
//        boolean mobileNetworkPlay = PreferencesUtils.enableMobileNetworkPlay();
//        if (NetworkUtils.is4G(mContext) && !mobileNetworkPlay) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle(R.string.tips);
//            builder.setMessage(R.string.play_tips);
//            builder.setPositiveButton(R.string.play_tips_sure, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    PreferencesUtils.saveMobileNetworkPlay(true);
//                    getPlayInfo();
//                }
//            });
//            builder.setNegativeButton(R.string.cancel, null);
//            Dialog dialog = builder.create();
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//        } else {
//            getPlayInfo();
//        }
//    }
//
//    private void getPlayInfo() {
//        onPrepare();
//        String lrcFileName = FileUtils.getLrcFileName(songList.getAuthor(), songList.getTitle());
//        File lrcFile = new File(FileUtils.getLrcDir() + lrcFileName);
//        if (lrcFile.exists()) {
//            mCounter++;
//        }
//        final Music music = new Music();
//        music.setType(Music.Type.ONLINE);
//        music.setTitle(songList.getTitle());
//        music.setArtist(songList.getAuthor());
//        // 获取歌曲播放链接
//        OkHttpUtils.get().url(Constants.BASE_URL)
//                .addParams(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC)
//                .addParams(Constants.PARAM_SONG_ID, songList.getSong_id())
//                .build()
//                .execute(new JsonCallback<DownloadInfo>(DownloadInfo.class) {
//                    @Override
//                    public void onResponse(final DownloadInfo response) {
//                        if (response == null) {
//                            onFail(null, null);
//                            return;
//                        }
//                        music.setCoverUri(response.getSonginfo().getPic_small());
//                        music.setUri(response.getBitrate().getFile_link());
//                        music.setDuration(response.getBitrate().getFile_duration() * 1000);
//                        mCounter++;
//                        if (mCounter == 2) {
//                            onSuccess(music);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        onFail(call, e);
//                    }
//                });
//        // 下载歌词
//        OkHttpUtils.get().url(Constants.BASE_URL)
//                .addParams(Constants.PARAM_METHOD, Constants.METHOD_LRC)
//                .addParams(Constants.PARAM_SONG_ID, songList.getSong_id())
//                .build()
//                .execute(new JsonCallback<Lrc>(Lrc.class) {
//                    @Override
//                    public void onResponse(Lrc response) {
//                        if (response == null || TextUtils.isEmpty(response.getLrcContent())) {
//                            return;
//                        }
//                        String lrcFileName = FileUtils.getLrcFileName(songList.getAuthor(), songList.getTitle());
//                        saveLrcFile(lrcFileName, response.getLrcContent());
//
//                        mCounter++;
//                        if (mCounter == 2) {
//                            onSuccess(music);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e) {
//                    }
//
//                });
//    }
//
//    private void saveLrcFile(String fileName, String content) {
//        try {
//            FileWriter writer = new FileWriter(FileUtils.getLrcDir() + fileName);
//            writer.flush();
//            writer.write(content);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public abstract void onPrepare();
//
//    public abstract void onSuccess(Music music);
//
//    public abstract void onFail(Call call, Exception e);
//}
