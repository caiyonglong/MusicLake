package com.cyl.musiclake.ui.music.search;


import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.data.db.Music;
import com.cyl.musiclake.db.SearchHistoryBean;

import java.util.List;

public interface SearchContract {

    interface View extends BaseContract.BaseView {

        void showSearchResult(List<Music> list);

        void showSearchSuggestion(List<SearchHistoryBean> list);

        void showEmptyView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void search(String key, SearchEngine.Filter filter, int limit, int page);

        void getSuggestions(String query);

        void saveQueryInfo(String query);

    }
}
