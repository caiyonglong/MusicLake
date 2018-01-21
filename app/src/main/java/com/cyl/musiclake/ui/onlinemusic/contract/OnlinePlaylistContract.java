package com.cyl.musiclake.ui.onlinemusic.contract;


import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;
import com.cyl.musiclake.api.baidu.OnlinePlaylists;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showOnlineSongs(List<OnlinePlaylists.Billboard> artistInfo);
    }

    interface Presenter extends BasePresenter<View> {
        void loadOnlinePlaylist();
    }
}
