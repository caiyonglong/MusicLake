package com.cyl.musiclake.ui.music.download;


import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.data.download.TasksManagerModel;

import java.util.List;

public interface DownloadContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showSongs(List<Music> musicList);

        void showDownloadList(List<TasksManagerModel> modelList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadDownloadMusic();

        void loadDownloading();
    }
}
