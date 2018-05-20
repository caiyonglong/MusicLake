package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface DownloadContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showSongs(List<Music> musicList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadDownloadMusic();
    }
}
