package com.cyl.musiclake.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


/**
 * Created by D22434 on 2018/1/5.
 */

public class FetchUtils {

    public static String getDataFromUrl(String baseUrl, Map<String, String> paramsMap) {
        String result = "";
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();

            System.out.println("----success:" + requestUrl);
            URL uri = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("----success");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                result = new String(out.toByteArray());
                System.out.println("----result");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDataFromUrl(String baseUrl) {
        String result = "";
        try {
            String requestUrl = baseUrl;
            System.out.println("----success:" + requestUrl);
            URL uri = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("----success");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                result = new String(out.toByteArray());
                System.out.println("----result");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDataWithHeader(String baseUrl, Map<String, String> headers) {
        String result = "";
        try {
            String requestUrl = baseUrl;
            System.out.println("----success:" + requestUrl);
            URL uri = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("----success");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                result = new String(out.toByteArray());
                System.out.println("----result");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDataWithHeader(String baseUrl) {
        String result = "";
        try {
            String requestUrl = baseUrl;
            System.out.println("----success:" + requestUrl);
            URL uri = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("referer", "https://y.qq.com/portal/player.html");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("----success");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                result = new String(out.toByteArray());
                System.out.println("----result");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getHeaderCookie(String baseUrl, Map<String, String> headers, Map<String, String> paramsMap) {
        String result = "";
        try {
            String requestUrl = baseUrl;
            System.out.println("----success:" + requestUrl);
            URL uri = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
//            for (String key : headers.keySet()) {
//                conn.setRequestProperty(key, headers.get(key));
//            }
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("----success");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                for (int i = 0; i < conn.getHeaderFields().size(); i++) {
                    if (conn.getHeaderField(i).startsWith("_m_h5_tk=")) {
                        System.out.println(conn.getHeaderFieldKey(i));
                        System.out.println(conn.getHeaderField(i));
                        result = conn.getHeaderField(i);
                    }
                }
//                result = new String(out.toByteArray());
                System.out.println("----result" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDataWithAll(String baseUrl, Map<String, String> headers, Map<String, String> paramsMap) {
        String result = "";
        try {
            String requestUrl = baseUrl;
            System.out.println("----success:" + requestUrl);
            URL uri = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
//            for (String key : headers.keySet()) {
//                conn.setRequestProperty(key, headers.get(key));
//            }
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("----success");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                result = new String(out.toByteArray());
                System.out.println("----result" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
