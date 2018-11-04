package com.cyl.musiclake.ui.music.local.contract;

import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public interface FolderSongsContract {

     interface View extends BaseContract.BaseView {

        void showEmptyView();

        void showSongs(List<Music> musicList);

    }

     interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs(String path);

    }
}
