package com.cyl.musiclake.ui.music.local.contract;

import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public interface FolderSongsContract {

     interface View extends BaseContract.BaseView {

        void showEmptyView();

        void showSongs(List<BaseMusicInfo> baseMusicInfoInfoList);

    }

     interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs(String path);

    }
}
