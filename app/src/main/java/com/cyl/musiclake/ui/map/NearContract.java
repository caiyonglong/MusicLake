package com.cyl.musiclake.ui.map;

import com.cyl.musiclake.base.BasePresenter;
import com.cyl.musiclake.base.BaseView;
import com.cyl.musiclake.ui.map.location.Location;

import java.util.List;
import java.util.Map;

/**
 * Created by D22434 on 2018/1/3.
 */

public interface NearContract {

    interface View extends BaseView {
        void showErrorInfo(String msg);

        void updateView();

        void showLoading(String msg);

        void showLocations(List<Location> locationList);
    }

    interface Presenter extends BasePresenter<NearContract.View> {
        void getNearPeopleInfo(Map<String, String> params);
    }

}
