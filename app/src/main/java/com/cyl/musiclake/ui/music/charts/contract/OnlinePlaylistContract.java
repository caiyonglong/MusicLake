package com.cyl.musiclake.ui.music.charts.contract;


import com.cyl.musiclake.ui.base.BaseContract;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.ui.music.charts.GroupItemData;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showNeteaseCharts(List<GroupItemData> charts);

        void showQQCharts(List<GroupItemData> charts);

        void showBaiduCharts(List<GroupItemData> charts);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadBaiDuPlaylist();

        void loadTopList();

        void loadQQList();
    }
}
