package com.cyl.musiclake.ui.localmusic.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.qq.QQApiServiceImpl;
import com.cyl.musiclake.api.xiami.XiamiServiceImpl;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.localmusic.contract.PlayControlsContract;
import com.cyl.musiclake.utils.ConvertUtils;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FileUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hefuyi on 2016/11/8.
 */

public class PlayControlsPresenter implements PlayControlsContract.Presenter {

    private static final String TAG = "PlayControlsPresenter";
    private PlayControlsContract.View mView;

    private boolean isPlayPauseClick = false;
    private int mProgress;

    private Handler mHandler;
    private Context mContext;

    public PlayControlsPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(PlayControlsContract.View view) {
        mView = view;
        mHandler = new Handler();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mHandler.removeCallbacks(updateProgress);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onPlayPauseClick() {
        isPlayPauseClick = true;
        PlayManager.playPause();
        if (PlayManager.getPlayingMusic() == null) {
            mView.setErrorInfo("请选择需要播放的音乐");
        } else {
            boolean isPlaying = PlayManager.isPlaying();
            mView.setPlayPauseButton(isPlaying);
        }
    }

    @Override
    public void onPreviousClick() {
        PlayManager.prev();
    }

    @Override
    public void loadLyric() {
        Music music = PlayManager.getPlayingMusic();
        if (music == null) {
            return;
        }
        String localLyricInfo = null;
        if (music.getType() == Music.Type.LOCAL) {
            if (music.getUri().endsWith(".mp3")) {
                String lrcPath = music.getUri().replace(".mp3", ".lrc");
                localLyricInfo = FileUtils.readFile(lrcPath);
            }
            mView.showLyric(localLyricInfo);
        } else if (music.getType() == Music.Type.QQ) {
            QQApiServiceImpl.getQQLyric(music.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String lyricInfo) {
                            Log.e(TAG, lyricInfo);
                            mView.showLyric(lyricInfo);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showLyric(null);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (music.getType() == Music.Type.XIAMI) {
            Log.e(TAG, music.getLrcPath());
            XiamiServiceImpl.getXimaiLyric(music.getLrcPath())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String lyricInfo) {
                            Log.e(TAG, lyricInfo);
                            mView.showLyric(lyricInfo);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showLyric(null);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mView.showLyric(null);
        }
    }

    @Override
    public void onNextClick() {
        PlayManager.next();
    }


    @Override
    public void updateNowPlayingCard() {
        Log.d(TAG, "updateNowPlayingCard" + mProgress);
        Music music = PlayManager.getPlayingMusic();
        if (music == null) {
            return;
        }

        if (PlayManager.isPlaying()) {
            if (!mView.getPlayPauseStatus()) {//true表示按钮为待暂停状态
                mView.setPlayPauseButton(true);
            }
        } else {
            if (mView.getPlayPauseStatus()) {
                mView.setPlayPauseButton(false);
            }
        }

        final String title = music.getArtist();
        final String artist = ConvertUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum());
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(artist)) {
            mView.setTitle(mView.getContext().getResources().getString(R.string.app_name));
            mView.setArtist("");
        } else {
            mView.setTitle(title);
            mView.setArtist(artist);
        }

        if (music.getType() == Music.Type.LOCAL) {
            mView.setOtherInfo("本地音乐");
        } else if (music.getType() == Music.Type.QQ) {
            mView.setOtherInfo("QQ音乐");
        } else if (music.getType() == Music.Type.XIAMI) {
            mView.setOtherInfo("虾米音乐");
        }

        if (!isPlayPauseClick) {
            String url = null;
            if (music.getType() == Music.Type.LOCAL && music.getAlbumId() != -1) {
                url = CoverLoader.getInstance().getCoverUri(mContext, music.getAlbumId());
            } else {
                url = music.getCoverUri();
            }
            if (mContext != null) {
                GlideApp.with(mContext)
                        .asBitmap()
                        .load(url)
                        .error(R.drawable.default_cover)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                mView.setAlbumArt(resource);
                                new Palette.Builder(resource).generate(palette ->
                                        mView.setPalette(palette));
                            }
                        });
            }
        }
        isPlayPauseClick = false;
        mView.setProgressMax(PlayManager.getDuration());
        mHandler.post(updateProgress);
    }

    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            mProgress = PlayManager.getCurrentPosition();
            mView.updateProgress(mProgress);
            mHandler.postDelayed(updateProgress, 100);
        }
    };

}
