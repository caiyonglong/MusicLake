package com.cyl.music_hnust.utils;

import android.content.ContentUris;
import android.net.Uri;

import com.cyl.music_hnust.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 作者：yonglong on 2016/8/24 20:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class ImageUtils {
    public static DisplayImageOptions getCoverDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_cover) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_cover) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_cover) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 构建完成;
    }
    public static DisplayImageOptions getAlbumDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.ic_empty_music2)
                .resetViewBeforeLoading(true)
                .build();
    }

    public static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt);
    }

}
