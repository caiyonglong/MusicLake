package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

public interface AlbumsContract {

    interface View extends BaseView {

        void showAlbums(List<Album> albumList);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View> {

        void loadAlbums(String action);

    }
}
