package com.cyl.musiclake.data

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.common.Constants
import org.litepal.LitePal


object VideoLoader {

    /**
     * Android 扫描获取到的数据
     *
     * @param context
     * @param cursor
     * @return
     */
    private fun getVideosForMedia(context: Context, cursor: Cursor?): MutableList<Music> {
        val results = mutableListOf<Music>()
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(0)
                    val title = cursor.getString(1)
                    val artist = cursor.getString(2)
                    val album = cursor.getString(3)
                    val duration = cursor.getInt(4)
                    val size = cursor.getLong(5)
                    val path = cursor.getString(6)
                    val music = Music()
                    music.type = Constants.VIDEO
                    music.isOnline = false
                    music.mid = id.toString()
                    music.album = album
                    music.artist = if (artist == "<unknown>") "未知" else artist
                    music.uri = path
                    music.duration = duration.toLong()
                    music.title = title
                    music.fileSize = size
                    music.date = System.currentTimeMillis()
                    DaoLitepal.saveOrUpdateMusic(music)
                    results.add(music)
                } while (cursor.moveToNext())
            }
            cursor?.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return results
    }


    fun getAllLocalVideos(context: Context): MutableList<Music> {
        return getVideosForMedia(context, makeVideoCursor(context, null, null))
    }

    fun searchVideos(context: Context, searchString: String): MutableList<Music> {
        return getVideosForMedia(context, makeVideoCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                arrayOf("%$searchString%", "%$searchString%", "%$searchString%")))
    }

    fun getVideoListInFolder(context: Context, path: String): MutableList<Music> {
        val whereArgs = arrayOf("$path%")
        return getVideosForMedia(context, makeVideoCursor(context, MediaStore.Audio.Media.DATA + " LIKE ?", whereArgs, null))
    }

    fun makeVideoCursor(context: Context, selection: String?, paramArrayOfString: Array<String>?): Cursor? {
        val videoSortOrder = MediaStore.Video.Media.DEFAULT_SORT_ORDER
        return makeVideoCursor(context, selection, paramArrayOfString, videoSortOrder)
    }

    /**
     * arrayOf("_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", MediaStore.Audio.Media.DATA, "is_music")
     * 搜素本地音乐
     */
    private fun makeVideoCursor(context: Context, selection: String?, paramArrayOfString: Array<String>?, sortOrder: String?): Cursor? {
        val obj = arrayOf(
                MediaStore.Video.Media.BUCKET_ID,//视频文件在sdcard的名称
                MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                MediaStore.Video.Media.ALBUM,//歌曲的演唱者
                MediaStore.Video.Media.DURATION,//视频总时长
                MediaStore.Video.Media.SIZE,//视频的文件大小
                MediaStore.Video.Media.DATA//视频的绝对地址
        )
        return context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                obj, null, paramArrayOfString, sortOrder)
    }

}
