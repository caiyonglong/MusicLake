package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.bean.Music;

public interface NeteaseListContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void showTopList(NeteaseList musicList);

        void playNeteaseMusic(Music music);
    }

    interface Presenter extends BasePresenter<View> {
        void loadNeteaseMusicList(int idx);

        void playCurrentMusic(NeteaseMusic neteaseMusic);
    }
}
