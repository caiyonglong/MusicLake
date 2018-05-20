package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.api.netease.NeteaseList;
import com.cyl.musiclake.api.netease.NeteaseMusic;
import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;

public interface NeteaseListContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showTopList(NeteaseList musicList);

        void showMusicInfo(Music music);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadNeteaseMusicList(int idx);

        void getMusicInfo(NeteaseMusic neteaseMusic);
    }
}
