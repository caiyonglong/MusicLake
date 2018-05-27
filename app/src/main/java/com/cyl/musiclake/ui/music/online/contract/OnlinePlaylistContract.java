package com.cyl.musiclake.ui.music.online.contract;


import android.content.Context;

import com.cyl.musiclake.api.baidu.BaiduMusicList;
import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Playlist;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showCharts(List<Playlist> charts);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadBaiDuPlaylist();

        void loadTopList(Context context);
    }
}
