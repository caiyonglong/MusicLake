package com.cyl.musiclake.data

import android.content.Context
import android.provider.MediaStore
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.bean.FolderInfo
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.utils.LogUtil
import io.reactivex.Observable
import java.io.File
import java.util.*

object FolderLoader {
    /**
     * 检索包含音频文件的文件夹, 并统计该文件夹下的歌曲数目
     *
     * @return
     */
    fun getFoldersWithSong(context: Context): Observable<List<FolderInfo>> {
        val mapFolders = mutableMapOf<String, FolderInfo>()
        val num_of_songs = "num_of_songs"
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.MIME_TYPE)

        val selection = "duration>60000 AND is_music=1 AND title != '' "
        return Observable.create { subscriber ->
            val cursor = context.contentResolver.query(
                    MediaStore.Files.getContentUri("external"), projection, selection, null, null)

            if (cursor != null) {
                val index_data = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val index_mini_type = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
//                val index_num_of_songs = cursor.getColumnIndex(num_of_songs)
                while (cursor.moveToNext()) {

                    // 获取每个目录下的歌曲数量
//                    val songCount = cursor.getInt(index_num_of_songs)
                    // 获取文件的路径，如/storage/sdcard0/MIUI/music/Baby.mp3
                    val filepath = cursor.getString(index_data)
                    val mini_type = cursor.getString(index_mini_type)
                    // 获取文件所属文件夹的路径，如/storage/sdcard0/MIUI/music
                    val folderpath = filepath.substring(0, filepath.lastIndexOf(File.separator))
                    //刷新文件夹
//                    NavigationHelper.scanFileAsync(MusicApp.mContext, folderpath)
                    // 获取文件所属文件夹的名称，如music
                    val foldername = folderpath.substring(folderpath.lastIndexOf(File.separator) + 1)
                    LogUtil.d("FolderLoader", "文件名：$foldername 文件路径：$folderpath - 文件类型：$mini_type")
                    if (mapFolders.containsKey(foldername)) {
                        mapFolders[foldername]?.songCount = (mapFolders[foldername]?.songCount
                                ?: 0) + 1
                    } else {
                        mapFolders[foldername] = FolderInfo(foldername, folderpath, 1)
                    }
                }
            }
            val folderList = mapFolders.values.toList();
            Collections.sort(folderList, object : Comparator<FolderInfo> {
                override fun compare(o1: FolderInfo?, o2: FolderInfo?): Int {
                    return when {
                        o1?.folderName.toString() > o2?.folderName.toString() -> return 1
                        o1?.folderName.toString() == o2?.folderName.toString() -> 0
                        else -> -1
                    }
                }
            })
            cursor?.close()
            subscriber.onNext(folderList)
        }
    }
}
