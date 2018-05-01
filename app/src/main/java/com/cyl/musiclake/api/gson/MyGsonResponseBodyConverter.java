package com.cyl.musiclake.api.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by D22434 on 2018/1/16.
 */

public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    /**
     * 重写转换类
     *
     * @param value 巨坑。ResponseBody只能使用一次。使用完后会自动关闭，所以多次使用会报错 closed
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        System.out.println("GOSN:" + response);
        try {
            return adapter.fromJson(response);
        } catch (Exception e) {
            System.out.println("GOSN error:" + e.getMessage());
            //解析处理错误
            if (response.substring(0, 17).equals("MusicJsonCallback")) {
                response = response.substring(response.indexOf("{"), response.lastIndexOf(")"));
                System.out.println(response);
            }
            return adapter.fromJson(response);
        } finally {
            value.close();
        }
    }
}
