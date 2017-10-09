package com.cyl.music_hnust;


import org.junit.Test;

public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        String url = "http://musicdata.baidu.com/data2/pic/260368391/260368391.jpg@";
        System.out.println(url);
        System.out.println(url.split("@")[0]);
    }
}