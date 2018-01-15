package com.cyl.musiclake.ui.onlinemusic.contract;


import com.cyl.musiclake.data.model.Music;
import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineMusicList;

import java.util.List;

public interface OnlineMusicListContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showHeaderInfo(OnlineMusicList.JBillboard playlistInfo);

        void showOnlineMusicList(List<Music> musicList);
    }

    interface Presenter extends BasePresenter<View> {
        void loadOnlineMusicList(String type, int limit, int mOffset);

        void playCurrentMusic(String mid);
    }
}
