package com.cyl.musiclake.common;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 通用的Rx线程转换类
 * 参考:http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0819/3327.html
 */
public class RxSchedulers {

    private static final ObservableTransformer schedulersTransformer = upstream -> (upstream).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return (FlowableTransformer<T, T>) schedulersTransformer;
    }
}
