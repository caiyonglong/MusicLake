package com.cyl.musiclake.ui.localmusic.presenter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.service.PlayManager;
import com.cyl.musiclake.ui.localmusic.contract.PlayControlsContract;
import com.cyl.musiclake.utils.CoverLoader;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
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

    private boolean mDuetoplaypause = false;
    private boolean isDebug = true;
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
//        RxBus.getInstance().unregisterAll();
        mHandler.removeCallbacks(updateProgress);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onPlayPauseClick() {
        mDuetoplaypause = true;
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            PlayManager.playPause();
            if (PlayManager.getPlayingMusic() == null) {
                e.onError(new Throwable("请选择需要播放的音乐"));
            } else {
                boolean isPlaying = PlayManager.isPlaying();
                e.onNext(isPlaying);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isPlaying) {
                        mView.setPlayPauseButton(isPlaying);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setErrorInfo(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onPreviousClick() {
        PlayManager.prev();
    }

    @Override
    public void loadLyric() {
        String title = PlayManager.getSongName();
        String artist = PlayManager.getSongArtist();
        Music song = PlayManager.getPlayingMusic();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(artist)) {
            return;
        }
        if (song == null) {
            return;
        }
        if (song.getType() == Music.Type.LOCAL) {
            if (song.getUri().endsWith(".mp3")) {
                String lrcPath = song.getUri().replace(".mp3", ".lrc");
                Log.e("Presenter", lrcPath);
                File file = new File(lrcPath);
                if (file.exists()) {
                    mView.showLyric(file);
                } else {
                    mView.showLyric(null);
                }
            } else {
                mView.showLyric(null);
            }
        }
    }

    @Override
    public void onNextClick() {
        PlayManager.next();
    }


    @Override
    public void updateNowPlayingCard() {
        Log.d(TAG, "updateNowPlayingCard" + mProgress);
        Music music = null;
        if (PlayManager.getPlayingMusic() == null) {
            return;
        } else {
            music = PlayManager.getPlayingMusic();
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

        final String title = PlayManager.getSongName();
        final String artist = PlayManager.getSongArtist();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(artist)) {
            mView.setTitle(mView.getContext().getResources().getString(R.string.app_name));
            mView.setArtist("");
        } else {
            mView.setTitle(title);
            mView.setArtist(artist);
        }

        if (!mDuetoplaypause) {
            if (music != null) {
                GlideApp.with(mContext)
                        .asBitmap()
                        .load(CoverLoader.getInstance().getCoverUri(mContext, music.getAlbumId()))
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

        mDuetoplaypause = false;
        mView.setProgressMax(PlayManager.getDuration());
        mHandler.post(updateProgress);
    }

    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            mProgress = PlayManager.getCurrentPosition();
            if (isDebug) Log.d(TAG, "mProgress" + mProgress);
            mView.updateProgress(mProgress);
            mHandler.postDelayed(updateProgress, 100);
        }
    };


    public ObjectAnimator operatingAnim;
    public long currentPlayTime = 0;
    private LinearInterpolator mLinearInterpolator = new LinearInterpolator();
    /**
     * 旋转动画
     */
    public void initAlbumPic(View view) {
        operatingAnim = ObjectAnimator.ofFloat(view, "rotation", 0, 359);
        operatingAnim.setDuration(20 * 1000);
        operatingAnim.setRepeatCount(-1);
        operatingAnim.setRepeatMode(ObjectAnimator.RESTART);
        operatingAnim.setInterpolator(mLinearInterpolator);
    }

}
