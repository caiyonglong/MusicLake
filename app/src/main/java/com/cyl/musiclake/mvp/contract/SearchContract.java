package com.cyl.musiclake.mvp.contract;


import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

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
