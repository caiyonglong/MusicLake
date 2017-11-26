package com.cyl.musiclake.mvp.presenter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.R;
import com.cyl.musiclake.mvp.contract.PlayControlsContract;
import com.cyl.musiclake.mvp.model.music.Music;
import com.cyl.musiclake.service.PlayManager;

import java.io.File;


/**
 * Created by hefuyi on 2016/11/8.
 */

public class PlayControlsPresenter implements PlayControlsContract.Presenter {

    private PlayControlsContract.View mView;

    private boolean mDuetoplaypause = false;

    @Override
    public void attachView(PlayControlsContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onPlayPauseClick() {
        mDuetoplaypause = true;
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final Void... unused) {
                boolean isPlaying = PlayManager.isPlaying();
                PlayManager.playPause();
                return isPlaying;
            }

            @Override
            protected void onPostExecute(Boolean isPlaying) {
                if (isPlaying) {
                    mView.setPlayPauseButton(false);
                } else {
                    mView.setPlayPauseButton(true);
                }
            }
        }.execute();

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
        if (PlayManager.getPlayingMusic() == null)
            return;
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
            Glide.with(mView.getContext())
                    .asBitmap()
                    .load(PlayManager.getPlayingMusic().getCoverUri())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mView.setAlbumArt(resource);
                            new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    mView.setPalette(palette);
                                }
                            });
                        }
                    });
        }

        mDuetoplaypause = false;
        mView.setProgressMax(PlayManager.getDuration());
        mView.startUpdateProgress();
    }
}
