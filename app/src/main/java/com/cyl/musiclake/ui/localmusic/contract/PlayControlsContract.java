package com.cyl.musiclake.ui.localmusic.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;

import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;


public interface PlayControlsContract {

    interface View extends BaseView {

        Context getContext();

        void setAlbumArt(Bitmap albumArt);

        void setAlbumArt(Drawable albumArt);

        void setTitle(String title);

        void setArtist(String artist);

        void setOtherInfo(String source);

        void setPalette(Palette palette);

        void showLyric(String lyric);

        void setPlayPauseButton(boolean isPlaying);

        boolean getPlayPauseStatus();

        void updateProgress(int progress);

        void setProgressMax(int max);

        void setErrorInfo(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void onPlayPauseClick();

        void onPreviousClick();

        void loadLyric();

        void onNextClick();

        void updateNowPlayingCard();

    }
}
