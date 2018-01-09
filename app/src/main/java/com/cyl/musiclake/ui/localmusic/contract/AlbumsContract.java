package com.cyl.musiclake.ui.localmusic.contract;

import com.cyl.musiclake.data.model.Album;
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
