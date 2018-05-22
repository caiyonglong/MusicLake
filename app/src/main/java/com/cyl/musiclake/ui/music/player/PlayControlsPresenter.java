package com.cyl.musiclake.ui.music.player;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;

import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.AppRepository;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.StatusChangedEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.ImageUtils;
import com.cyl.musiclake.utils.LogUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hefuyi on 2016/11/8.
 */

public class PlayControlsPresenter extends BasePresenter<PlayControlsContract.View> implements PlayControlsContract.Presenter {

    private static final String TAG = "PlayControlsPresenter";

    private boolean isPlayPauseClick = false;
    private int mProgress;
    private Handler mHandler;

    @Inject
    public PlayControlsPresenter() {
    }

    @Override
    public void attachView(PlayControlsContract.View view) {
        super.attachView(view);
        mHandler = new Handler();
        RxBus.getInstance().register(MetaChangedEvent.class)
                .compose(mView.bindToLife())
                .subscribe(metaChangedEvent -> {
                    updateNowPlayingCard();
                });
        RxBus.getInstance().register(StatusChangedEvent.class)
                .compose(mView.bindToLife())
                .subscribe(metaChangedEvent -> {
                    updatePlayStatus();
                    if (!metaChangedEvent.isPrepared()) {
                        mView.showLoading();
                    } else {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (updateProgress != null) {
            mHandler.removeCallbacks(updateProgress);
        }
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
        if (isPlayPauseClick)
            return;
        String lrcPath = FileUtils.getLrcDir() + music.getTitle() + "-" + music.getArtist() + ".lrc";
        if (FileUtils.exists(lrcPath)) {
            mView.showLyric(lrcPath, true);
        } else {
            Observable<String> observable = MusicApi.getLyricInfo(music);
            if (observable == null) {
                LogUtil.e(TAG, "本地文件为空");
                mView.showLyric(null, false);
            } else {
                observable.subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String lyricInfo) {
                        LogUtil.e(TAG, lyricInfo);
                        mView.showLyric(lyricInfo, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLyric(null, false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }
    }

    @Override
    public void onNextClick() {
        PlayManager.next();
    }


    @Override
    public void updateNowPlayingCard() {
        LogUtil.d(TAG, "updateNowPlayingCard" + mProgress);
        Music music = PlayManager.getPlayingMusic();
        if (music == null || PlayManager.getPlayList().size() == 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(mView.getContext().getResources(), R.drawable.header_material);
            mView.setAlbumArt(bitmap);
            mView.setAlbumArt(ImageUtils.createBlurredImageFromBitmap(bitmap, mView.getContext(), 12));
            new Palette.Builder(bitmap).generate(palette -> mView.setPalette(palette));
            mView.setTitle(mView.getContext().getResources().getString(R.string.app_name));
            mView.setArtist("");
            return;
        } else {
            mView.updatePanelLayout(true);
        }


        final String title = PlayManager.getSongName();
        final String artist = PlayManager.getSongArtist();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(artist)) {
            mView.setTitle(mView.getContext().getResources().getString(R.string.app_name));
            mView.setArtist("");
        } else {
            mView.setTitle(title);
            mView.setArtist(artist);
        }
        //设置音乐来源
        mView.setOtherInfo(music.getTypeName(false));
        //获取当前歌曲状态
        AppRepository.getMusicInfo(mView.getContext(), music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(music1 -> mView.updateFavorite(music1.isLove()));

        if (!isPlayPauseClick) {
            loadLyric();
            CoverLoader.loadImageViewByMusic(mView.getContext(), music, bitmap -> {
                mView.setAlbumArt(bitmap);
                LogUtil.d(TAG, "loadBitmap =");
                mView.setAlbumArt(ImageUtils.createBlurredImageFromBitmap(bitmap, mView.getContext(), 12));
                new Palette.Builder(bitmap).generate(palette -> mView.setPalette(palette));
            });
        }
        isPlayPauseClick = false;
        mHandler.post(updateProgress);
    }

    @Override
    public void updatePlayStatus() {
        if (PlayManager.isPlaying()) {
            if (!mView.getPlayPauseStatus()) {//true表示按钮为待暂停状态
                mView.setPlayPauseButton(true);
            }
        } else {
            if (mView.getPlayPauseStatus()) {
                mView.setPlayPauseButton(false);
            }
        }
    }

    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            mProgress = PlayManager.getCurrentPosition();
            mView.updateProgress(mProgress);
            mView.setProgressMax(PlayManager.getDuration());
            mHandler.postDelayed(updateProgress, 500);
        }
    };

    public void updateFavoriteSong() {
        Music music = PlayManager.getPlayingMusic();
        if (music == null)
            return;
        PlayManager.updateFavorite(music);
        mView.updateFavorite(!PlayManager.getPlayingMusic().isLove());
    }
}
