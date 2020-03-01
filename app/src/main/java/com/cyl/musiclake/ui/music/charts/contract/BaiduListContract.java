package com.cyl.musiclake.ui.music.charts.contract;


import com.cyl.musiclake.ui.base.BaseContract;
import com.music.lake.musiclib.bean.BaseMusicInfo;

import java.util.List;

public interface BaiduListContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showOnlineMusicList(List<BaseMusicInfo> baseMusicInfoInfoList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadOnlineMusicList(String type, int limit, int mOffset);

    }
}
