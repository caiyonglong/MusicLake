package com.cyl.musiclake.api;

import com.cyl.musiclake.ui.login.user.User;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.ui.onlinemusic.model.OnlineArtistInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by D22434 on 2018/1/4.
 */

public class ApiManagerServiceImpl implements ApiManagerService {

    @Override
    public Observable<String> getSongUrl(String baseUrl) {
        return null;
    }

    @Override
    public Observable<ApiModel<User>> getUserInfo(String baseUrl, Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<ApiModel<List<Location>>> getNearPeopleInfo(String baseUrl, Map<String, String> params) {
        return null;
    }

    @Override
    public Observable<ApiModel<OnlineArtistInfo>> getArtistInfo(String baseUrl, Map<String, String> params) {
        return null;
    }
}
