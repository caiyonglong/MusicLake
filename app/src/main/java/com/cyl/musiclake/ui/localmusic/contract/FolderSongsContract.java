package com.cyl.musiclake.ui.localmusic.contract;

import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public interface FolderSongsContract {

     interface View extends BaseView {

        void showEmptyView();

        void showSongs(List<Music> musicList);

    }

     interface Presenter extends BasePresenter<View> {

        void loadSongs(String path);

    }
}
