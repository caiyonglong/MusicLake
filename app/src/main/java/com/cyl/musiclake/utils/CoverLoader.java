package com.cyl.musiclake.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.music.MusicUtils;
import com.cyl.musiclake.bean.Music;


/**
 * 专辑封面图片加载器
 * Glide加载异常处理
 */
public class CoverLoader {
    private static final String TAG = "CoverLoader";

    public interface BitmapCallBack {
        void showBitmap(Bitmap bitmap);
    }

    public static String getCoverUri(Context context, String albumId) {
        if (albumId.equals("-1")) {
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

    /**
     * 获取专辑图url，
     *
     * @param music 音乐
     * @param isBig 是否是大图
     * @return
     */
    private static String getCoverUriByMusic(Music music, boolean isBig) {
        if (music.getCoverBig() != null && isBig) {
            return music.getCoverBig();
        } else if (music.getCoverUri() != null) {
            return music.getCoverUri();
        } else {
            return music.getCoverSmall();
        }
    }


    public static int getCoverUriByRandom() {
        int[] Bitmaps = {R.drawable.music_one, R.drawable.music_two, R.drawable.music_three,
                R.drawable.music_four, R.drawable.music_five, R.drawable.music_six,
                R.drawable.music_seven, R.drawable.music_eight, R.drawable.music_nine,
                R.drawable.music_ten, R.drawable.music_eleven, R.drawable.music_twelve};
        int random = (int) (Math.random() * 12);
        return R.drawable.default_cover;
    }


    /**
     * 显示小图
     *
     * @param mContext
     * @param music
     * @param callBack
     */
    public static void loadImageViewByMusic(Context mContext, Music music, BitmapCallBack callBack) {
        if (music == null) return;
        String url = getCoverUriByMusic(music, false);
//        if (url != null) {
        loadBitmap(mContext, url, callBack);
//        } else {
//            loadImageViewByDouban(mContext, music.getTitle(), null, callBack);
//        }
    }

    /**
     * 显示播放页大图
     *
     * @param mContext
     */
    public static void loadBigImageView(Context mContext, Music music, BitmapCallBack callBack) {
        if (music == null) return;
        String url = MusicUtils.INSTANCE.getAlbumPic(music.getCoverUri(), music.getType(), MusicUtils.INSTANCE.getPIC_SIZE_BIG());
        GlideApp.with(mContext)
                .asBitmap()
                .load(url == null ? R.drawable.music_five : url)
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (callBack != null && resource != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }

    public static void loadBigImageView(Context mContext, Music music, ImageView imageView) {
        if (music == null || imageView == null) return;
        String url = getCoverUriByMusic(music, true);
        GlideApp.with(mContext)
                .asBitmap()
                .load(url == null ? R.drawable.music_five : url)
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadBigImageView(Context mContext, String url,String vendor, ImageView imageView) {
        if (imageView == null) return;
        String newUrl = MusicUtils.INSTANCE.getAlbumPic(url,vendor, MusicUtils.INSTANCE.getPIC_SIZE_BIG());
        GlideApp.with(mContext)
                .asBitmap()
                .load(newUrl)
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 显示图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        if (mContext == null) return;
        GlideApp.with(mContext)
                .load(url)
                .error(R.drawable.ic_account_circle)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageView(Context mContext, String url, int defaultUrl, ImageView imageView) {
        if (mContext == null) return;
        GlideApp.with(mContext)
                .load(url)
                .error(defaultUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 根据id显示
     *
     * @param mContext
     * @param albumId
     * @param callBack
     */
    public static void loadBitmapById(Context mContext, String albumId, BitmapCallBack callBack) {
        loadBitmap(mContext, getCoverUri(mContext, albumId), callBack);
    }

    /**
     * 返回bitmap
     *
     * @param mContext
     * @param url
     * @param callBack
     */
    public static void loadBitmap(Context mContext, String url, BitmapCallBack callBack) {
        if (mContext == null) return;
        GlideApp.with(mContext)
                .asBitmap()
                .load(url == null ? getCoverUriByRandom() : url)
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        if (callBack != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }


    public static Drawable createBlurredImageFromBitmap(Bitmap bitmap) {
        return ImageUtils.createBlurredImageFromBitmap(bitmap, 4);
    }

}
