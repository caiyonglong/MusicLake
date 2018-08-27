package com.cyl.musiclake.ui.music.player;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;


public interface PlayControlsContract {

    interface View extends BaseContract.BaseView {

        void setAlbumArt(Bitmap albumArt);

        void setAlbumArt(Drawable albumArt);

        void setTitle(String title);

        void setArtist(String artist);

        void setOtherInfo(String source);

        void setPalette(Palette palette);

        void showLyric(String lyric, boolean isFilePath);


        void setPlayPauseButton(boolean isPlaying);

        boolean getPlayPauseStatus();

        void updateProgress(int progress);

        void updateFavorite(boolean love);

        void setProgressMax(int max);

        void updatePanelLayout(boolean scroll);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void onPlayPauseClick();

        void onPreviousClick();

        void loadLyric(String result, boolean status);

        void onNextClick();

        void updateNowPlayingCard(Music music);

        void updatePlayStatus(boolean isPlaying);

    }
}
