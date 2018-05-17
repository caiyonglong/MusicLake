package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

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
