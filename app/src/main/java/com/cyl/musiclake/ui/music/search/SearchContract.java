package com.cyl.musiclake.ui.music.search;


import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;

import java.util.List;

public interface SearchContract {

    interface View extends BaseContract.BaseView {

        void showSearchResult(List<Music> list);

        void showSearchSuggestion(List<String> list);

        void showEmptyView();

        void showMusicInfo(int type, Music music);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void search(String key, int limit, int page);

        void getSuggestions(String query);


        void getMusicInfo(int type, Music music);
    }

}
