package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.api.baidu.BaiduMusicList;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showOnlineSongs(List<BaiduMusicList.Billboard> artistInfo);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadOnlinePlaylist();
    }
}
