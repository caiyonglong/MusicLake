package com.cyl.musiclake.ui.music.charts.contract;


import com.cyl.musiclake.ui.base.BaseContract;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showNeteaseCharts(List<Playlist> charts);

        void showQQCharts(List<Playlist> charts);

        void showBaiduCharts(List<Playlist> charts);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadBaiDuPlaylist();

        void loadTopList();

        void loadQQList();
    }
}
