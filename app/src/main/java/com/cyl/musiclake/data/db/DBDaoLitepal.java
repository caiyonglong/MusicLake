package com.cyl.musiclake.data.db;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.bean.QueryInfo;
import com.cyl.musiclake.utils.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by master on 2018/4/26.
 */

public class DBDaoLitepal {

    public void deletePlaylist(long pId) {
        int result = DataSupport.delete(Playlist.class, pId);
        if (result != 0) {
            ToastUtils.show(MusicApp.getAppContext(), "删除成功");
        }
    }

    public void addSearchInfo(String info) {
        QueryInfo queryInfo = new QueryInfo(info);
        queryInfo.save();
    }

    public List<QueryInfo> getSearchInfos() {
        return DataSupport.findAll(QueryInfo.class);
    }

}
