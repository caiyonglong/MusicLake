package com.cyl.musiclake;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;


public class RxBus {
    private static volatile RxBus sRxBus;
    // 主题
    private final FlowableProcessor<Object> mBus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    // 单例RxBus
    public static RxBus getInstance() {
        if (sRxBus == null) {
            synchronized (RxBus.class) {
                if (sRxBus == null) {
                    sRxBus = new RxBus();
                }
            }
        }
        return sRxBus;
    }

    public void post(@NonNull Object obj) {
        mBus.onNext(obj);
    }

    /**
     * 订阅事件
     *
     * @param clz
     * @param <T>
     * @return
     */
    public <T> Flowable<T> register(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public void unregister() {
        mBus.onComplete();
    }
}
