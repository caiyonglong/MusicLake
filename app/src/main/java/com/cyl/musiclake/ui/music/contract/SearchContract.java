package com.cyl.musiclake.ui.music.contract;


import com.cyl.musiclake.ui.base.BasePresenter;
import com.cyl.musiclake.ui.base.BaseView;

import java.util.List;

public interface SearchContract {

    interface View extends BaseView {

        void showSearchResult(List<Object> list);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View> {

        void search(String queryString);
    }

}
