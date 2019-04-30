package com.cyl.musiclake.api.music.netease;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by master on 2018/3/19.
 */
public class NeteaseApiServiceImplTest {
    // 加密
    public static String Encrypt(String sSrc, String sKey, String cKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(cKey.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    @Test
    public void test() {
        String text = "{s:薛之谦,type:1,limit:10,offset:0}";
        String nonce = "0CoJUm6Qyw8W8jud";
        try {
            String secKey = createSecretKey(16);
            System.out.println(secKey);
            String tt = Encrypt(text, nonce, "0102030405060708");
            System.out.println(tt);
            String tt2 = Encrypt(tt, secKey, "0102030405060708");
            System.out.println(tt2);
            crawlAjaxUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createSecretKey(int size) {
        String keys = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String key = "";
        for (int i = 0; i < size; i++) {
            double pos = Math.random() * keys.length();
            pos = Math.floor(pos);
            key = key + keys.charAt((int) pos);
        }
        return key;
    }

    public String crawlAjaxUrl() {

        String text = "{s:薛之谦,type:1,limit:10,offset:0}";
        //first_param = first_param.replace("limit_param", ONE_PAGE + "");
        try {
            // 参数加密
            // 16位随机字符串，
            String secKey = createSecretKey(16);
            // 两遍ASE加密
            String encText = aesEncrypt(aesEncrypt(text, "0CoJUm6Qyw8W8jud"), secKey);
            System.out.println("encText = " + encText);
            //RSA加密
            String encSecKey = rsaEncrypt();
            System.out.println("encSecKey = " + encSecKey);

            System.out.println("{params:" + encText + ",encSecKey:" + encSecKey + "}");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ASE-128-CBC加密模式可以需要16位
     *
     * @param src 加密内容
     * @param key 密钥
     * @return
     */
    public static String aesEncrypt(String src, String key) throws Exception {
        String encodingFormat = "UTF-8";
        String iv = "0102030405060708";

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        // 使用CBC模式，需要一个向量vi，增加加密算法强度
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(src.getBytes(encodingFormat));
        return Base64.getEncoder().encodeToString(encrypted);

    }

    public static String rsaEncrypt() {
        String secKey = "257348aecb5e556c066de214e531faadd1c55d814f9be95fd06d6bff9f4c7a41f831f6394d5a3fd2e3881736d94a02ca919d952872e7d0a50ebfa1769a7a62d512f5f1ca21aec60bc3819a9c3ffca5eca9a0dba6d6f7249b06f5965ecfff3695b54e1c28f3f624750ed39e7de08fc8493242e26dbc4484a01c76f739e135637c";
        return secKey;
    }

    public static String parseMillisecone(long millisecond) {
        String time = null;
        try {
            long yushu_day = millisecond % (1000 * 60 * 60 * 24);
            long yushu_hour = (millisecond % (1000 * 60 * 60 * 24))
                    % (1000 * 60 * 60);
            long yushu_minute = millisecond % (1000 * 60 * 60 * 24)
                    % (1000 * 60 * 60) % (1000 * 60);
            @SuppressWarnings("unused")
            long yushu_second = millisecond % (1000 * 60 * 60 * 24)
                    % (1000 * 60 * 60) % (1000 * 60) % 1000;
            if (yushu_day == 0) {
                return (millisecond / (1000 * 60 * 60 * 24)) + "天";
            } else {
                if (yushu_hour == 0) {
                    return (millisecond / (1000 * 60 * 60 * 24)) + "天"
                            + (yushu_day / (1000 * 60 * 60)) + "时";
                } else {
                    if (yushu_minute == 0) {
                        return (millisecond / (1000 * 60 * 60 * 24)) + "天"
                                + (yushu_day / (1000 * 60 * 60)) + "时"
                                + (yushu_hour / (1000 * 60)) + "分";
                    } else {
                        return (millisecond / (1000 * 60 * 60 * 24)) + "天"
                                + (yushu_day / (1000 * 60 * 60)) + "时"
                                + (yushu_hour / (1000 * 60)) + "分"
                                + (yushu_minute / 1000) + "秒";

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = s;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将emoji表情替换成*
     *
     * @return 过滤后的字符串
     */
//    public static String filterEmoji(String source) {
//        if (StringUtils.isNotBlank(source)) {
//            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
//        } else {
//            return source;
//        }
//    }
    @Test
    public void getToplist() {
        List<String> list = new ArrayList<>();
        list.add("云音乐新歌榜");
        list.add("云音乐热歌榜");
        list.add("网易原创歌曲榜");
        list.add("云音乐飙升榜");
        list.add("云音乐电音榜");
        list.add("UK排行榜周榜");
        list.add("美国Billboard周榜 ");
        list.add("KTV嗨榜 ");
        list.add("iTunes榜 ");
        list.add("Hit FM Top榜 ");
        list.add("日本Oricon周榜 ");
        list.add("韩国Melon排行榜周榜 ");
        list.add("韩国Mnet排行榜周榜 ");
        list.add("韩国Melon原声周榜 ");
        list.add("中国TOP排行榜(港台榜) ");
        list.add("中国TOP排行榜(内地榜)");
        list.add("香港电台中文歌曲龙虎榜 ");
        list.add("华语金曲榜");
        list.add("中国嘻哈榜");
        list.add("法国 NRJ EuroHot 30 周榜");
        list.add("台湾Hito排行榜 ");
        list.add("Beatport全球电子舞曲榜");
        list.add("云音乐ACG音乐榜");
        list.add("云音乐嘻哈榜");
        Map<String, String> maps = new HashMap<>();

//        for (int i = 0; i < list.size(); i++) {
//            try {
//                Observable<NeteaseList> qqApiModel
//                        = MusicApi.getTopList(i);
//                int finalI = i;
//
//                qqApiModel.subscribe(qqApi -> {
//                    maps.put(list.get(finalI), qqApi.getCoverImgUrl());
//                });
//            } catch (Exception e) {
//
//            }
//        }
//        for (int i = 0; i < maps.size(); i++) {
//            System.out.println(maps.get(list.get(i)));
//        }
    }
}