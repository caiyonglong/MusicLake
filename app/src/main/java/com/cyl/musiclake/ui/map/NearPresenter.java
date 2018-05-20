package com.cyl.musiclake.ui.map;


import com.cyl.musiclake.base.BasePresenter;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by D22434 on 2018/1/3.
 */

public class NearPresenter extends BasePresenter<NearContract.View> implements NearContract.Presenter {
    @Inject
    public NearPresenter() {
    }

    @Override
    public void getNearPeopleInfo(Map<String, String> params) {

    }
}
