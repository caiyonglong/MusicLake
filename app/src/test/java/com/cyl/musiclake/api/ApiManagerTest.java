package com.cyl.musiclake.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by D22434 on 2018/1/5.
 */
public class ApiManagerTest extends TestCase {

    public void testQQ() throws Exception {

        System.out.println("------------start load---------------");
        Map<String, String> params = new HashMap<>();
        params.put("p", "1"); //page
        params.put("n", "10");//limit
        params.put("w", "Beyond");// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");
        String result = FetchUtils.getDataFromUrl("http://c.y.qq.com/soso/fcgi-bin/search_cp?", params);
        System.out.println("--" + result);
        System.out.println("------------ end ---------------");

        System.out.println("------------start parse---------------");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new Gson();
        QQApi qqApi = gson.fromJson(result, QQApi.class);


        System.out.println("------------ end ---------------");


    }

    public void testXiami() throws Exception {

        System.out.println("------------start---------------");
        Map<String, String> params = new HashMap<>();
        params.put("p", "1"); //page
        params.put("n", "10");//limit
        params.put("w", "Beyond");// key
        params.put("aggr", "1");
        params.put("cr", "1");
        params.put("lossless", "1");
        params.put("format", "json");
        String result = FetchUtils.getDataFromUrl("http://c.y.qq.com/soso/fcgi-bin/search_cp?", params);
        System.out.println("--" + result);
        System.out.println("------------ end ---------------");
    }


}