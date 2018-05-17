package com.cyl.musiclake.ui.music.local.contract;

import android.graphics.Bitmap;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

public interface ArtistSongContract {

    interface View extends BaseView {

        void showEmptyView();

        void showSongs(List<Music> songList);

        void showAlbumArt(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter<View> {

        void loadSongs(String artistName);
    }

}
