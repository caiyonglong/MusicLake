package com.cyl.musiclake.ui.music.discover;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;


public interface DiscoverContract {

    interface View extends BaseContract.BaseView {
        void showEmptyView();

        void showBaiduCharts(List<Playlist> charts);

        void showNeteaseCharts(List<Playlist> charts);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadBaidu();

        void loadNetease();
    }
}
