package com.cyl.musicapi.qq;

import java.util.List;

/**
 * Created by yonglong on 2018/1/14.
 */

public class QQApiKey {
    /**
     * code : 0
     * sip : ["http://dl.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"]
     * thirdip : ["http://thirdparty.gtimg.com/abcd1234/","http://thirdparty.gtimg.com/abcd1234/"]
     * testfile2g : C100003mAan70zUy5O.m4a
     * testfilewifi : C100003mAan70zUy5O.m4a
     * key : 8DA0CDC57B9BA57AB488AA1E582875CF2D41DF9201581863B24AF3758164C7D9C6AB21B33668F55E556262EB60AF60979EE8E0A0C1921C87
     */
    private int code;
    private String testfile2g;
    private String testfilewifi;
    private String key;
    private List<String> sip;
    private List<String> thirdip;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTestfile2g() {
        return testfile2g;
    }

    public void setTestfile2g(String testfile2g) {
        this.testfile2g = testfile2g;
    }

    public String getTestfilewifi() {
        return testfilewifi;
    }

    public void setTestfilewifi(String testfilewifi) {
        this.testfilewifi = testfilewifi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getSip() {
        return sip;
    }

    public void setSip(List<String> sip) {
        this.sip = sip;
    }

    public List<String> getThirdip() {
        return thirdip;
    }

    public void setThirdip(List<String> thirdip) {
        this.thirdip = thirdip;
    }
}
