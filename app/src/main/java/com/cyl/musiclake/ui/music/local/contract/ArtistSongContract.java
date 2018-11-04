package com.cyl.musiclake.ui.music.local.contract;

import android.graphics.Bitmap;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

public interface ArtistSongContract {

    interface View extends BaseContract.BaseView {

        void showEmptyView();

        void showSongs(List<Music> songList);

        void showAlbumArt(Bitmap bitmap);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs(String artistName);
    }

}
