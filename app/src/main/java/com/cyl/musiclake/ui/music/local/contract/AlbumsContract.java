package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

public interface AlbumsContract {

    interface View extends BaseContract.BaseView {

        void showAlbums(List<Album> albumList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadAlbums(String action);

    }
}
