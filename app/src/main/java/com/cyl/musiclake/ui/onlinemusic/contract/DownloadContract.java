package com.cyl.musiclake.ui.onlinemusic.contract;

import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;
import com.cyl.musiclake.ui.download.model.FileState;

import java.util.List;

/**
 * Author   : D22434
 * version  : 2018/1/22
 * function :
 */

public interface DownloadContract {
    interface View extends BaseView {
        void showEmptyView();

        void updateProgress(List<FileState> fileStates);
    }

    interface Presenter extends BasePresenter<DownloadContract.View> {
        void updateDownloadStatus(FileState status);

        void updateProgress(String url, int finished);
    }
}
