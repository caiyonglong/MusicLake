package com.cyl.musiclake.ui.music.contract;

import com.cyl.musiclake.ui.music.model.Album;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

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
