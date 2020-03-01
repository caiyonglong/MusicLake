package com.cyl.musiclake.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cyl.musiclake.R
import com.cyl.musiclake.api.GlideApp
import com.cyl.musiclake.api.music.MusicUtils
import com.music.lake.musiclib.bean.BaseMusicInfo


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
     * @param baseMusicInfo 音乐
     * @param isBig 是否是大图
     * @return
     */
    private fun getCoverUriByMusic(baseMusicInfo: com.music.lake.musiclib.bean.BaseMusicInfo, isBig: Boolean): String? {
        return if (baseMusicInfo.coverBig != null && isBig) {
            baseMusicInfo.coverBig
        } else if (baseMusicInfo.coverUri != null) {
            baseMusicInfo.coverUri
        } else {
            baseMusicInfo.coverSmall
        }
    }


    /**
     * 显示小图
     *
     * @param mContext
     * @param baseMusicInfoInfo
     * @param callBack
     */
    fun loadImageViewByMusic(mContext: Context, baseMusicInfoInfo: BaseMusicInfo?, callBack: ((Bitmap) -> Unit)?) {
        if (baseMusicInfoInfo == null) return
        val url = getCoverUriByMusic(baseMusicInfoInfo, false)
        loadBitmap(mContext, url, callBack)
    }

    /**
     * 显示播放页大图
     *
     * @param mContext
     */
    fun loadBigImageView(mContext: Context?, baseMusicInfo: com.music.lake.musiclib.bean.BaseMusicInfo?, callBack: ((Bitmap) -> Unit)?) {
        if (baseMusicInfo == null) return
        if (mContext == null) return
        val url = MusicUtils.getAlbumPic(baseMusicInfo.coverUri, baseMusicInfo.type, MusicUtils.PIC_SIZE_BIG)
        GlideApp.with(mContext)
                .asBitmap()
                .load(url ?: R.drawable.default_cover)
                .error(coverUriByRandom)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        if (callBack != null && resource != null) {
                            callBack.invoke(resource)
                        }
                    }
                })
    }

    fun loadBigImageView(mContext: Context, baseMusicInfo: com.music.lake.musiclib.bean.BaseMusicInfo?, imageView: ImageView?) {
        if (baseMusicInfo == null || imageView == null) return
        val url = getCoverUriByMusic(baseMusicInfo, true)
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
                .placeholder(R.drawable.holder_with_bg)
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
                .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

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
                .into<CustomTarget<Bitmap>>(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callBack?.invoke(BitmapDrawable(mContext.resources, resource))
                    }
                })
    }

    fun createBlurredImageFromBitmap(bitmap: Bitmap?): Drawable {
        return ImageUtils.createBlurredImageFromBitmap(bitmap, 4)
    }

}
