package com.cyl.musiclake.api;

import com.cyl.musiclake.api.qq.QQApiModel;
import com.cyl.musiclake.api.qq.QQApiServiceImpl;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by yonglong on 2018/1/15.
 */
public class ApiManagerServiceTest1 {
    @Test
    public void searchByQQ() throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("p", 1); //page
        params.put("n", 10);//limit
        params.put("w", "薛之谦");// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");

        Observable<QQApiModel> qqApiModel
                = ApiManager.getInstance().apiService.searchByQQ("http://c.y.qq.com/soso/fcgi-bin/search_cp?", params);
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
                System.out.println(data.get(i).getPubtime());
                System.out.println(data.get(i).getInterval());
            }
        });
        assert (10 == 10);
    }

    @Test
    public void searchByXiami() throws Exception {
        QQApiServiceImpl.getQQApiKey()
                .subscribe(map -> {
                    String uid = map.keySet().iterator().next();
                    String key = map.get(uid);
                    System.out.println("uid=" + uid + "---" + key);
                });

    }


    @Test
    public void searchLyric() throws Exception {
        QQApiServiceImpl.getQQLyric("001Qu4I30eVFYb")
                .subscribe(map -> {
                    System.out.println("uid=" + map);
                });
    }

}