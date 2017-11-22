package com.cyl.musiclake.mvp.contract;

import com.cyl.musiclake.mvp.model.music.Album;
import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

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
