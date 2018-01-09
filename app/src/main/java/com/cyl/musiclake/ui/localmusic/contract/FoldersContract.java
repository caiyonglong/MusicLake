package com.cyl.musiclake.ui.localmusic.contract;

import com.cyl.musiclake.data.model.FolderInfo;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public class FoldersContract {

    public interface View extends BaseView {

        void showEmptyView();

        void showFolders(List<FolderInfo> folderInfos);
    }

    public interface Presenter extends BasePresenter<View> {

        void loadFolders();
    }
}
