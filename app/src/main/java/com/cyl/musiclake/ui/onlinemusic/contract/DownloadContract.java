package com.cyl.musiclake.ui.onlinemusic.contract;


import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

public interface DownloadContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showSongs(List<Music> musicList);
    }

    interface Presenter extends BasePresenter<View> {
        void loadDownloadMusic();
    }
}
