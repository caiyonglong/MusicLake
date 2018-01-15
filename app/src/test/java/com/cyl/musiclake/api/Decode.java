package com.cyl.musiclake.api;

import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by yonglong on 2018/1/15.
 */

public class Decode extends TestCase {
    public void testEE() {
        String lo = "5h3%2ae2F%82965l%ty555-%b1f7a%7tA28mt4725F239.3h%1EE%53f2275bt%F.i%42F115_9mF_363%5E7e133E%p2mx.2%4337396pakD565E-6a61e95%F1inF245%693_3ue1%%E-3353678E";
        System.out.println(parseLocation(lo));
        System.out.println(parseLocation(lo).replace("^","0"));
    }

    public static String parseLocation(String location) {
        int head = Integer.parseInt(location.substring(0, 1));
        String _str = location.substring(1);
        int rows = head;
        int cols = (_str.length() / rows) + 1;
        String output = "";
        int full_row = 0;
        for (int i = 0; i < head; i++) {
            if ((_str.length() - i) / head == (_str.length() / head)) {
                full_row = i;
            }
        }
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < head; r++) {
                if (c == (cols - 1) && r >= full_row) {
                    continue;
                }
                char tt;
                if (r < full_row) {
                    tt = _str.charAt(r * cols + c);
                } else {
                    tt = _str.charAt(cols * full_row + (r - full_row) * (cols - 1) + c);
                }
                output += tt;
            }
        }
//        return decodeURIComponent(output).replace( /\^/g, '0')
        try {
            return URLDecoder.decode(output,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return output;
        }
    }

}
