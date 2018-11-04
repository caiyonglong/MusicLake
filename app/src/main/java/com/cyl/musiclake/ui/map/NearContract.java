package com.cyl.musiclake.ui.map;

import com.cyl.musiclake.ui.base.BaseContract;

import java.util.Map;

/**
 * Created by D22434 on 2018/1/3.
 */

public interface NearContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showLoading(String msg);

    }

    interface Presenter extends BaseContract.BasePresenter<NearContract.View> {
        void getNearPeopleInfo(Map<String, String> params);
    }

}
