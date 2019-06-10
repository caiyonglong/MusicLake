package com.cyl.musiclake.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.cyl.musiclake.R
import com.cyl.musiclake.api.GlideApp
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.bean.Music


/**
 * 专辑封面图片加载器
 * Glide加载异常处理
 */
object CoverLoader {
    private val TAG = "CoverLoader"
    val coverUriByRandom: Int = R.drawable.default_cover

    fun getCoverUri(context: Context, albumId: String): String? {
        if (albumId == "-1") {
            return null
        }
        var uri: String? = null
        try {
            val cursor = context.contentResolver.query(
                    Uri.parse("content://media/external/audio/albums/$albumId"),
                    arrayOf("album_art"), null, null, null)
            if (cursor != null) {
                cursor.moveToNext()
                uri = cursor.getString(0)
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return uri
    }

    /**
     * 获取专辑图url，
     *
     * @param music 音乐
     * @param isBig 是否是大图
     * @return
     */
    private fun getCoverUriByMusic(music: Music, isBig: Boolean): String? {
        return if (music.coverBig != null && isBig) {
            music.coverBig
        } else if (music.coverUri != null) {
            music.coverUri
        } else {
            music.coverSmall
        }
    }


    /**
     * 显示小图
     *
     * @param mContext
     * @param music
     * @param callBack
     */
    fun loadImageViewByMusic(mContext: Context, music: Music?, callBack: ((Bitmap) -> Unit)?) {
        if (music == null) return
        val url = getCoverUriByMusic(music, false)
        loadBitmap(mContext, url, callBack)
    }

    /**
     * 显示播放页大图
     *
     * @param mContext
     */
    fun loadBigImageView(mContext: Context?, music: Music?, callBack: ((Bitmap) -> Unit)?) {
        if (music == null) return
        if (mContext == null) return
        val url = MusicUtils.getAlbumPic(music.coverUri, music.type, MusicUtils.PIC_SIZE_BIG)
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: R.drawable.default_cover)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        if (callBack != null && resource != null) {
                            callBack.invoke(resource)
                        }
                    }
                })
    }

    fun loadBigImageView(mContext: Context, music: Music?, imageView: ImageView?) {
        if (music == null || imageView == null) return
        val url = getCoverUriByMusic(music, true)
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: R.drawable.default_cover)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun loadBigImageView(mContext: Context, url: String?, vendor: String?, imageView: ImageView?) {
        if (imageView == null) return
        val newUrl = MusicUtils.getAlbumPic(url, vendor, MusicUtils.PIC_SIZE_BIG)
        GlideApp.with(mContext)
                .asBitmap()
                .load(newUrl)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    /**
     * 显示图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    fun loadImageView(mContext: Context?, url: String?, imageView: ImageView?) {
        if (mContext == null) return
        if (imageView == null) return
        GlideApp.with(mContext)
                .load(url)
                .error(R.drawable.holder_with_bg)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun loadImageView(mContext: Context?, url: String?, defaultUrl: Int, imageView: ImageView) {
        if (mContext == null) return
        GlideApp.with(mContext)
                .load(url)
                .error(defaultUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    /**
     * 根据id显示
     *
     * @param mContext
     * @param albumId
     * @param callBack
     */
    fun loadBitmapById(mContext: Context, albumId: String, callBack: ((Bitmap) -> Unit)?) {
        loadBitmap(mContext, getCoverUri(mContext, albumId), callBack)
    }

    /**
     * 返回bitmap
     *
     * @param mContext
     * @param url
     * @param callBack
     */
    fun loadBitmap(mContext: Context?, url: String?, callBack: ((Bitmap) -> Unit)?) {
        if (mContext == null) return
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: coverUriByRandom)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callBack?.invoke(resource)
                    }
                })
    }

    /**
     * 返回Drawable
     *
     * @param mContext
     * @param url
     * @param callBack
     */
    fun loadDrawable(mContext: Context?, url: String?, callBack: ((Drawable) -> Unit)?) {
        if (mContext == null) return
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: coverUriByRandom)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callBack?.invoke(BitmapDrawable(mContext.resources, resource))
                    }
                })
    }

    fun createBlurredImageFromBitmap(bitmap: Bitmap?): Drawable {
        return ImageUtils.createBlurredImageFromBitmap(bitmap, 4)
    }

}
