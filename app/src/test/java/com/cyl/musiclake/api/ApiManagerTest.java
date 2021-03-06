package com.cyl.musiclake.api;

import com.cyl.musiclake.common.Constants;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by D22434 on 2018/1/5.
 */
public class ApiManagerTest extends TestCase {

    public void testQQ() throws Exception {

//        Scanner in = new Scanner(System.in);
        System.out.println("------------start load---------------");
        System.out.println("Please Input Key Words :");
        String key = "薛之谦";

        Map<String, String> params = new HashMap<>();
        params.put("p", "1"); //page
        params.put("n", "10");//limit
        params.put("w", key);// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");
        System.out.println("Result--");
//        String result = FetchUtils.getDataFromUrl("http://c.y.qq.com/soso/fcgi-bin/search_cp?", params);
//        System.out.println(result);
//        System.out.println("------------ end ---------------");
//
//        System.out.println("------------start parse---------------");
//        Gson gson = new Gson();
//        QQApiModel qqApi = gson.fromJson(result, QQApiModel.class);
//        System.out.println(qqApi.getCode());
//        System.out.println(qqApi.getTime());
//        System.out.println(qqApi.getData().getKeyword());
//        System.out.println(qqApi.getData().getSong().getTotalnum());
//        System.out.println(qqApi.getData().getSong().getCurpage());
//        System.out.println(qqApi.getData().getSong().getCurnum());
//        System.out.println(qqApi.getData().getSong().getList().size());
//
//        if (qqApi.getData().getSong().getList().size() > 0) {
//            System.out.println(qqApi.getData().getSong().getList().get(0).toString());
//
//            System.out.println("------------ song ---------------");
//            System.out.println(qqApi.getData().getSong().getList().get(0).getSongmid());
//            System.out.println(qqApi.getData().getSong().getList().get(0).getSongname());
//
//            System.out.println(qqApi.getData().getSong().getList().get(0).getPay().getPayplay());
//            System.out.println(qqApi.getData().getSong().getList().get(0).getSinger().toString());
//        }
//        System.out.println("------------ end ---------------");


    }

    public void testXiami() throws Exception {

        System.out.println("------------start 搜索 薛之谦---------------");
//        Map<String, String> params = new HashMap<>();
//        params.put("referer", "http://h.xiami.com/"); //page
//        String url = "http://api.xiami.com/web?v='2.0'&key=薛之谦&limit=10&page=1&r=search/songs&app_key=1";
//        String result = FetchUtils.getDataWithHeader(url, params);
//        System.out.println(result);
//        System.out.println("------------ end ---------------");
//        Gson gson = new Gson();
//        XiamiApiModel xiamiApiModel = gson.fromJson(result, XiamiApiModel.class);
//
//        System.out.println("------------start 获取歌曲详细信息---------------");
//        //获取歌曲详细信息
//        int songId = xiamiApiModel.getData().getSongs().get(0).getSong_id();
//        String ss = xiamiApiModel.getData().getSongs().get(0).toString();
//        System.out.println(songId);
//        System.out.println(ss);
//        String url1 = "http://www.xiami.com/song/playlist/id/" + songId + "/object_name/default/object_id/0/cat/json";
//        result = FetchUtils.getDataWithHeader(url1, params);
//        System.out.println(result);
//        System.out.println("------------ end ---------------");
//        //解析播放地址
//        System.out.println("------------ 解析播放地址 ---------------");
//        XiamiLyricInfo info = gson.fromJson(result,XiamiLyricInfo.class);
//        System.out.println(info.data.trackList.get(0).getSongName());
//        System.out.println(info.getData().getTrackList().get(0).getLocation());
//        System.out.println("------------ 解析 ---------------");
//        String xx =info.data.trackList.get(0).getLocation();
//        System.out.println(Decode.parseLocation(xx).replace("^","0"));

    }

    public void testOnline() throws Exception {
        System.out.println("xxxxxxxxxxx");
        String result = FetchUtils.getDataFromUrl(Constants.BASE_MUSIC_URL);
        System.out.println(result);
    }

}