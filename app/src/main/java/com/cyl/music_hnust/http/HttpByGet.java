package com.cyl.music_hnust.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.util.Log;
import android.widget.Toast;

import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

public class HttpByGet {
    private static String TAG_GET = "Log";

    // HttpGet方式请求
    public static String requestByHttpGet2(String key) {
        String path = Constants.DEFAULT_MUSIC_INTERFACE+ key;
        String json = "";
        try {
            // 新建HttpGet对象
            URL url = new URL(path);

            // 获取HttpClient对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            // 判断是够请求成功
            if (conn.getResponseCode() == 200) {
                // 获取返回的数据

                InputStream is = conn.getInputStream();
                json = readInputStream(is);
                //	json = EntityUtils.toString(httpResp.getEntity(), "GBK");


                Log.e(TAG_GET, "HttpGet方式请求成功，返回数据如下：");
                //	JsonParsing.getmusicId(json);
                //	Log.e(TAG_GET, result);
            } else {
                json="ERROR";
                Log.e(TAG_GET, "HttpGet方式请求失败");
            }
        } catch (IOException e) {
            json="ERROR";
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;

    }

    // HttpURLConnection
    public static String requestByHttpGet(String urlpath, String path) {
        String json = "";
        try {
            Log.e("requestByHttpGet",path);
            String strURL = URLEncoder.encode(path, "utf-8");
            Log.e("requestByHttpGet",strURL);
            // 新建HttpGet对象
            URL url = new URL(urlpath+strURL);

            // 获取HttpClient对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            // 判断是够请求成功
            if (conn.getResponseCode() == 200) {
                // 获取返回的数据

                InputStream is = conn.getInputStream();
                json = readInputStream(is);
                //	json = EntityUtils.toString(httpResp.getEntity(), "GBK");


                Log.e(TAG_GET, "HttpGet方式请求成功，返回数据如下："+json);
                //	JsonParsing.getmusicId(json);
                //	Log.e(TAG_GET, result);
            } else {
                json="ERROR";
                Log.e(TAG_GET, "HttpGet方式请求失败");
            }
        } catch (IOException e) {
            json="ERROR";
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    public static String LoginByGet(String usernumber, String password, int flagcode, User userinfo) {
        String path = "";
        if (flagcode == 1) {
            path = "http://suen.pw/interface/senate/verify.php?num=" + usernumber + "&pw=" + password;
        } else if (flagcode == 2) {
            String param = "user_id=" + userinfo.getUser_id() +
                    "&user_name=" + userinfo.getUser_name() +
                    "&user_sex=" + userinfo.getUser_sex() +
                    "&user_major=" + userinfo.getUser_major() +
                    "&user_class=" + userinfo.getUser_class() +
                    "&user_college=" + userinfo.getUser_college() +
                    "&newUser";
            path = Constants.DEFAULT_URL + param;

        }
        else if (flagcode == 3) {
            String param = "user_id=" + usernumber +
                    "&GetUserinfo";
            path = Constants.DEFAULT_URL + param;
        }
        try {
            //创建URL实例
            URL url = new URL(path);
            //获取HettpConnection对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                String text = null;
                text = readInputStream(is);
                Log.e("path===",path);
                Log.e("text", text);
                return text;
            } else {
                Log.e("text", code + "");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("text", "网络异常");
        }
        Log.e("text", "网络异常");
        return null;
    }

    /**
     * 把输入流转换成字符数组
     *
     * @param inputStream 输入流
     * @throws Exception
     * @return 字符数组
     */
    public static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();

        return bout.toByteArray();
    }

    /**
     * 把输入流内容转化成字符串
     */
    public static String readInputStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            byte[] result = baos.toByteArray();
            //试着解析result中的字符串
            String temp = new String(result);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }

    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    public static boolean isURL(String str) {
        if (str != null) {
            if (str.startsWith("http://")) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
    /**
     * @param url
     *            要下载的文件URL
     * @throws Exception
     */
    public static void downloadFile(String url, final String tempPath) throws Exception {

        AsyncHttpClient client = new AsyncHttpClient();
        // 指定文件类型
        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
        // 获取二进制数据如图片和其他文件
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {
                Log.e("binaryData:", "共下载了：" + binaryData.length);
                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
                        binaryData.length);
                File file = new File(tempPath);
                // 压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                // 压缩比例
                int quality = 100;
                try {
                    // 若存在则删除
                    if (file.exists())
                        file.delete();
                    // 创建文件
                    file.createNewFile();
                    //
                    OutputStream stream = new FileOutputStream(file);
                    // 压缩输出
                    bmp.compress(format, quality, stream);
                    // 关闭
                    stream.close();
                    //
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {

            }

            @Override
            public void onRetry(int retryNo) {
                // TODO Auto-generated method stub
                super.onRetry(retryNo);
                // 返回重试次数
            }
        });
    }
}

