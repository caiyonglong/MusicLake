package com.cyl.musiclake.ui.music.mv;

import com.cyl.musicapi.netease.MvInfoDetail;
import com.cyl.musiclake.ui.base.BaseContract;

import java.util.List;


public interface MvListContract {

    interface View extends BaseContract.BaseView {
        void showMvList(List<MvInfoDetail> mvList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadPersonalizedMv();

        void loadMv(int offset);

        void loadRecentMv(int limit);

        void searchMv(String key, int offset);
    }
}
