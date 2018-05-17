package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.bean.Music;

import java.util.List;

public interface BaiduListContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showOnlineMusicList(List<Music> musicList);
    }

    interface Presenter extends BasePresenter<View> {
        void loadOnlineMusicList(String type, int limit, int mOffset);

        void playCurrentMusic(Music mid);
    }
}
