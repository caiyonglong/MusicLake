package com.cyl.musiclake.api;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.cyl.musiclake.api.net.ExternalCacheDiskFactory;

/**
 * Created by D22434 on 2017/11/16.
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    private int mDiskSize = 1024 * 1024 * 500;  //500M

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        // 定义缓存大小和位置
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, mDiskSize));  //内存中
        builder.setDiskCache(new ExternalCacheDiskFactory(context)); //sd卡中
//        builder.setLogLevel(Log.DEBUG);
        // 默认内存和图片池大小
//        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
//        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

        // 自定义内存和图片池大小
//        builder.setMemoryCache(new LruResourceCache(mMemorySize));
//        builder.setBitmapPool(new LruBitmapPool(mMemorySize));

        // 定义图片格式
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认

    }
}
