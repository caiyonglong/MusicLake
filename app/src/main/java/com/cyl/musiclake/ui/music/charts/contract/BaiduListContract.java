package com.cyl.musiclake.ui.music.charts.contract;


import com.cyl.musiclake.ui.base.BaseContract;
import com.cyl.musiclake.bean.Music;

import java.util.List;

public interface BaiduListContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showOnlineMusicList(List<Music> musicList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadOnlineMusicList(String type, int limit, int mOffset);

    }
}
