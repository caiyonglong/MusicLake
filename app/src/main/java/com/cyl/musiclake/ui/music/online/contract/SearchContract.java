package com.cyl.musiclake.ui.music.online.contract;


import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.bean.Music;

import java.util.List;

public interface SearchContract {

    interface View extends BaseView {

        void showSearchResult(List<Music> list);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View> {

        void search(String key, int limit, int page);

        void play(Music music);
    }

}
