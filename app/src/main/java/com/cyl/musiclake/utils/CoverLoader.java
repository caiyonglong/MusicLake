package com.cyl.musiclake.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.MyApplication;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.bean.Music;


/**
 * 专辑封面图片加载器
 * Glide加载异常处理
 */
public class CoverLoader {
    private static final String KEY_NULL = "null";
    private static final String TAG = "CoverLoader";
    /**
     * 缩略图缓存，用于音乐列表
     */
    private LruCache<String, Bitmap> mThumbnailCache;
    /**
     * 高斯模糊图缓存，用于播放页背景
     */
    private LruCache<String, Bitmap> mBlurCache;
    /**
     * 圆形图缓存，用于播放页CD
     */
    private LruCache<String, Bitmap> mRoundCache;

    private CoverLoader() {
        // 获取当前进程的可用内存（单位KB）
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 缓存大小为当前进程可用内存的1/8
        int cacheSize = maxMemory / 8;
        mThumbnailCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 转换为KB，以达到与cacheSize的单位统一
                return bitmap.getByteCount() / 1024;
            }
        };
        mBlurCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        mRoundCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static CoverLoader getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static CoverLoader instance = new CoverLoader();
    }

    private Bitmap bitmap = null;

    public Bitmap loadBitmapByGlide(Context context, String uri) {
        try {
            if (TextUtils.isEmpty(uri)) {
                bitmap = mThumbnailCache.get(KEY_NULL);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.drawable.default_cover);
                    mThumbnailCache.put(KEY_NULL, bitmap);
                }
            } else {
                GlideApp.with(context)
                        .asBitmap()
                        .load(uri)
                        .error(R.drawable.default_cover)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                bitmap = resource;
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap loadThumbnail(String uri) {
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(uri)) {
                bitmap = mThumbnailCache.get(KEY_NULL);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.drawable.default_cover);
                    mThumbnailCache.put(KEY_NULL, bitmap);
                }
            } else {
                bitmap = mThumbnailCache.get(uri);
                if (bitmap == null) {
                    bitmap = loadBitmap(uri, SizeUtils.getScreenWidth() / 10);
                    if (bitmap == null) {
                        bitmap = loadThumbnail(null);
                    }
                    mThumbnailCache.put(uri, bitmap);
                }
            }
        } catch (Exception e) {

        }
        return bitmap;
    }


    public Bitmap loadRound(String uri) {
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(uri)) {
                bitmap = mRoundCache.get(KEY_NULL);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.drawable.music_eight);
                    bitmap = ImageUtils.resizeImage(bitmap, SizeUtils.getScreenWidth() / 2, SizeUtils.getScreenWidth() / 2);
                    bitmap = ImageUtils.createCircleImage(bitmap);
                    mRoundCache.put(KEY_NULL, bitmap);
                }
            } else {
                bitmap = mRoundCache.get(uri);
                if (bitmap == null) {
                    bitmap = loadBitmap(uri, SizeUtils.getScreenWidth() / 2);
                    if (bitmap == null) {
                        bitmap = loadRound(null);
                    } else {
                        bitmap = ImageUtils.resizeImage(bitmap, SizeUtils.getScreenWidth() / 2, SizeUtils.getScreenWidth() / 2);
                        bitmap = ImageUtils.createCircleImage(bitmap);
                    }
                    mRoundCache.put(uri, bitmap);
                }
            }
        } catch (Exception e) {

        }
        return bitmap;
    }

    public Bitmap loadNormal(String uri) {
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(uri)) {
                bitmap = mRoundCache.get(KEY_NULL);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.drawable.default_cover);
                    bitmap = ImageUtils.resizeImage(bitmap, SizeUtils.getScreenWidth() / 2, SizeUtils.getScreenWidth() / 2);
                    mRoundCache.put(KEY_NULL, bitmap);
                }
            } else {
                if (bitmap == null) {
                    bitmap = loadBitmap(uri, SizeUtils.getScreenWidth() / 2);
                }
            }
        } catch (Exception e) {

        }
        return bitmap;
    }

    /**
     * 获得指定大小的bitmap
     */
    private Bitmap loadBitmap(String uri, int length) {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            // 仅获取大小
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(uri, options);
            int maxLength = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
            // 压缩尺寸，避免卡顿
            int inSampleSize = maxLength / length;
            if (inSampleSize < 1) {
                inSampleSize = 1;
            }
            options.inSampleSize = inSampleSize;
            // 获取bitmap
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(uri, options);
        } catch (Exception e) {
            return null;
        }
    }


    public String getCoverUri(Context context, long albumId) {
        if (albumId == -1) {
            return null;
        }
        String uri = null;
        try {
            Cursor cursor = context.getContentResolver().query(
                    Uri.parse("content://media/external/audio/albums/" + albumId),
                    new String[]{"album_art"}, null, null, null);
            if (cursor != null) {
                cursor.moveToNext();
                uri = cursor.getString(0);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    public String getCoverUriByMusic(Music music) {
        if (music.getCoverBig() != null)
            return music.getCoverBig();
        else if (music.getCoverUri() != null)
            return music.getCoverUri();
        else return music.getCoverSmall();
    }

    public int getCoverUriByRandom() {
        int[] Bitmaps = {R.drawable.music_one, R.drawable.music_two, R.drawable.music_three,
                R.drawable.music_four, R.drawable.music_five, R.drawable.music_six,
                R.drawable.music_seven, R.drawable.music_eight, R.drawable.music_nine,
                R.drawable.music_ten, R.drawable.music_eleven, R.drawable.music_twelve};
        int random = (int) (Math.random() * 12);
        return Bitmaps[random];
    }
}
