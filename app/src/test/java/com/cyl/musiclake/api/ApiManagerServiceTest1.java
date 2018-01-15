package com.cyl.musiclake.api;

import com.cyl.musiclake.api.qq.QQApiModel;

import org.junit.Test;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by yonglong on 2018/1/15.
 */
public class ApiManagerServiceTest1 {
    @Test
    public void searchByQQ() throws Exception {
        Observable<QQApiModel> qqApiModel
                = ApiManager.getInstance().apiService.searchByQQ("http://c.y.qq.com/soso/fcgi-bin/search_cp?p=1&w=%E8%96%9B%E4%B9%8B%E8%B0%A6&format=json&lossless=1&aggr=1&n=10&cr=1");
        qqApiModel.subscribe(qqApi -> {
            List<QQApiModel.DataBean.SongBean.ListBean> data = qqApi.getData().getSong().getList();
            System.out.println(data.size());
            System.out.println(qqApi.getCode());
            System.out.println(qqApi.getTime());
            System.out.println(qqApi.getData().getKeyword());
            System.out.println(qqApi.getData().getSong().getTotalnum());
            System.out.println(qqApi.getData().getSong().getCurpage());
            System.out.println(qqApi.getData().getSong().getCurnum());
            System.out.println(qqApi.getData().getSong().getList().size());
            for (int i = 0; i < data.size(); i++) {
                System.out.println(data.get(i).getSinger().get(0).getId());
            }
        });
        assert (10 == 10);
    }

    @Test
    public void searchByXiami() throws Exception {
    }

}