package com.cyl.musiclake.api.net;

import com.cyl.musiclake.BuildConfig;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.gson.MyGsonConverterFactory;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.NetworkUtils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by yonglong on 2017/9/11.
 */

public class ApiManager {

    public ApiManagerService apiService;
    private static ApiManager sApiManager;

    private static long CONNECT_TIMEOUT = 60L;
    private static long READ_TIMEOUT = 60L;
    private static long WRITE_TIMEOUT = 60L;
    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    public static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=10";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    private static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    private static volatile OkHttpClient mOkHttpClient;

    /**
     * 日志拦截器
     */
    private static final Interceptor mLoggingInterceptor = chain -> {
        Request request = chain.request();
        if (chain.request().url().toString().contains("https://45.76.48.211")) {
            HttpUrl newUrl = request.url().newBuilder()
                    .host("netease2.api.zzsun.cc")
                    .build();
            request = request.newBuilder().url(newUrl).build();
            return chain.proceed(request);
        }
        return chain.proceed(request);
    };
    /**
     * 网络拦截器
     */
    private static final Interceptor mNetInterceptor = chain -> {
        Request request = chain.request().newBuilder()
                .addHeader("Connection", "close").build();
        return chain.proceed(request);
    };

    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            // Cookie 持久化
            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MusicApp.getAppContext()));
            synchronized (ApiManager.class) {
                Cache cache = new Cache(new File(MusicApp.getAppContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                if (mOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();

                    //设置http 日志拦截
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor();
                        httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
                        builder.addInterceptor(httpLogging);
                    }

                    mOkHttpClient = builder.cache(cache)
                            .addInterceptor(mNetInterceptor)
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .cookieJar(cookieJar)
                            .build();
                }

            }
        }
        return mOkHttpClient;
    }

    //获取ApiManager的单例
    public static ApiManager getInstance() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        return sApiManager;
    }

    private ApiManager() {
    }

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    /**
     * 获取Service
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(clazz);
    }


    /**
     * 发送网络请求
     */
    public static <T> void request(Observable<T> service, RequestCallBack<T> result) {
        if (!NetworkUtils.isNetworkAvailable(MusicApp.getInstance())) {
            result.error(MusicApp.getAppContext().getString(R.string.error_connection));
            return;
        }
        service.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(T t) {
                        if (result != null) {
                            result.success(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            String errorInfo = "";
                            try {
                                errorInfo = ((HttpException) e).response().errorBody().string();
                                if (errorInfo.startsWith("{")) {
                                    JSONObject jsonObject = new JSONObject(errorInfo);
                                    String error = jsonObject.getString("msg");
                                    result.error(error);
                                } else {
                                    result.error(errorInfo);
                                }
                            } catch (IOException | JSONException e1) {
                                e1.printStackTrace();
                                LogUtil.d("ApiManager", "errorInfo=" + errorInfo);
                                if (errorInfo.contains("LeanEngine")) {
                                    errorInfo = "默认LeanEngine服务器请求超出限制\n请稍后再试！";
                                }
                                if (e1 instanceof JSONException) {
                                    result.error(errorInfo);
                                } else {
                                    result.error(MusicApp.getAppContext().getString(R.string.error_connection));
                                }
                            }
                        } else {
                            if (result != null) {
                                if (e.getMessage() == null) {
                                    result.error(MusicApp.getAppContext().getString(R.string.error_connection));
                                } else {
                                    result.error(e.getMessage());
                                }
                            } else {
                                result.error(MusicApp.getAppContext().getString(R.string.error_connection));
                            }
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
