package com.cyl.musicapi.xiami;

import com.cyl.musiclake.api.FetchUtils;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by master on 2018/4/7.
 */
public class XiamiServiceImplTest {
    @Test
    public void getSongInfo() {
        String id = "";
        //
        String token = getXiamiToken();
//                "_m_h5_tk=951a567310df59fa7bdfe9b84a22333c_1523097529936; Domain=xiami.com; Expires=Sat, 14-Apr-2018 09:46:49 GMT; Path=/";
        token = token.replace("_m_h5_tk=", "").split(";")[0];
        String signedToken = token.split("_")[0];
        System.out.println("myToken =" + signedToken);

        String appKey = "12574478";
        String queryStr = "{" +
                "header: {" +
                "appId: 200," +
                "appVersion: 1000000," +
                "callId: " + new Date().getTime() + "," +
                "network: 1," +
                "platformId: 'android'," +
                "remoteIp: '192.168.1.101'," +
                "resolution: '1178*778'" +
                "}," +
                "model: {" +
                "songIds: [" + id + "]" +
                "}}";

        long t = new Date().getTime();
        String sign = "";
        try {
            sign = EncoderByMd5(signedToken + "&" + t + "&" + appKey + "&" + queryStr);
            System.out.println(sign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "http://acs.m.xiami.com/h5/mtop.alimusic.music.songservice.getsongs/1.0/";

        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("host", "acs.m.xiami.com"); //page
        headerParams.put("content-type", "application/x-www-form-urlencoded"); //page
        headerParams.put("cookie", token); //page

        url = url + appKey + "&" + t + "&" + sign + "&api=mtop.alimusic.social.commentservice.getcommentlist"
                + "&v=1.0&type=originaljson&dataType=json&data='" + queryStr+"'";

        String result = FetchUtils.getDataWithAll(url, headerParams, null);
        System.out.println(result);
    }

    public String getXiamiToken() {
        String url = "http://acs.m.xiami.com/h5/mtop.alimusic.music.songservice.getsongs/1.0/";
        return FetchUtils.getHeaderCookie(url, null, null);
    }

    public String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder base64en = Base64.getEncoder();
        //加密后的字符串
        String newstr = new String(base64en.encode(md5.digest(str.getBytes("utf-8"))), "utf-8");
        return newstr;
    }

}