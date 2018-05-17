package com.cyl.musiclake.ui.music.list.contract;

import com.cyl.musiclake.bean.FolderInfo;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public interface FoldersContract {

    interface View extends BaseView {

        void showEmptyView();

        void showFolders(List<FolderInfo> folderInfos);
    }

    interface Presenter extends BasePresenter<View> {

        void loadFolders();
    }
}
