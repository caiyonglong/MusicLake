package com.cyl.musiclake.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.api.MusicApi;
import com.cyl.musiclake.api.doupan.DoubanMusic;
import com.cyl.musiclake.bean.Music;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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

    public static String getCoverUriByMusic(Music music) {
        if (music.getCoverBig() != null)
            return music.getCoverBig();
        else if (music.getCoverUri() != null)
            return music.getCoverUri();
        else
            return music.getCoverSmall();
    }


    public static int getCoverUriByRandom() {
        int[] Bitmaps = {R.drawable.music_one, R.drawable.music_two, R.drawable.music_three,
                R.drawable.music_four, R.drawable.music_five, R.drawable.music_six,
                R.drawable.music_seven, R.drawable.music_eight, R.drawable.music_nine,
                R.drawable.music_ten, R.drawable.music_eleven, R.drawable.music_twelve};
        int random = (int) (Math.random() * 12);
        return R.drawable.default_cover_player;
    }

    public static void loadImageViewByDouban(Context mContext, String info, ImageView imageView, BitmapCallBack bitmapCallBack) {
        MusicApi.getMusicAlbumInfo(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DoubanMusic>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(DoubanMusic doubanMusic) {
                        Log.d(TAG, "picUrl =" + doubanMusic.getCount());
                        String url = null;
                        if (doubanMusic.getCount() >= 1) {
                            url = doubanMusic.getMusics().get(0).getImage();
                        }
                        if (imageView != null) {
                            loadImageView(mContext, url, imageView);
                        } else if (bitmapCallBack != null) {
                            loadBitmap(mContext, url, bitmapCallBack);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.d(TAG, "throwable =" + throwable.getMessage());
                        if (imageView != null) {
                            loadImageView(mContext, null, imageView);
                        } else if (bitmapCallBack != null) {
                            loadBitmap(mContext, null, bitmapCallBack);
                        }
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public static void loadImageViewByMusic(Context mContext, Music music, ImageView imageView) {
        String url = getCoverUriByMusic(music);
        loadImageView(mContext, url, imageView);
    }

    public static void loadImageViewByMusic(Context mContext, Music music, BitmapCallBack callBack) {
        if (music == null) return;
        String url = getCoverUriByMusic(music);
        if (url != null) {
            loadBitmap(mContext, url, callBack);
        } else {
            loadImageViewByDouban(mContext, music.getTitle(), null, callBack);
        }
    }

    public static void loadImageViewById(Context mContext, String albumId, ImageView imageView) {
        String url = getCoverUri(mContext, albumId);
        loadImageView(mContext, url, imageView);
    }

    /**
     * 显示图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        GlideApp.with(mContext)
                .load(url)
                .error(R.drawable.default_cover_player)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageView(Context mContext, String url, int defaultUrl, ImageView imageView) {
        GlideApp.with(mContext)
                .load(url)
                .error(defaultUrl)
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
        GlideApp.with(mContext)
                .asBitmap()
                .load(url == null ? getCoverUriByRandom() : url)
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (callBack != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }

    public static void loadBitmap(Context mContext, BitmapCallBack callBack) {
        GlideApp.with(mContext)
                .asBitmap()
                .load(getCoverUriByRandom())
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (callBack != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }

    public static void loadImageSync(Context mContext, BitmapCallBack callBack) {
        GlideApp.with(mContext)
                .asBitmap()
                .load(getCoverUriByRandom())
                .error(getCoverUriByRandom())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (callBack != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }


}
