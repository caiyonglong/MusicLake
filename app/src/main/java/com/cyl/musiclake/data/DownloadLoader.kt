package com.cyl.musiclake.data

import android.text.TextUtils
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.data.download.TasksManagerModel
import com.cyl.musiclake.utils.ToastUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils
import org.litepal.LitePal

object DownloadLoader {
    private val TAG = "PlayQueueLoader"

    /**
     * 获取已下载列表
     */
    fun getDownloadList(): MutableList<Music> {
        val musicList = mutableListOf<Music>()
        val data = LitePal.where("finish = 1").find(TasksManagerModel::class.java)
        data.forEach {
            val music = it.mid?.let { it1 ->
                DaoLitepal.getMusicInfo(it1)
            }
            music?.forEach { origin ->
                if (origin.uri == null || origin.uri?.startsWith("http:")!!) {
                    origin.uri = it.path
                }
                musicList.add(origin)
            }
        }
        return musicList
    }

    /**
     * 获取已下载列表
     */
    fun getDownloadingList(): MutableList<TasksManagerModel> {
        return LitePal.where("finish = 0").find(TasksManagerModel::class.java)
    }

    /**
     * 是否已在下载列表
     */
    fun isHasMusic(mid: String): Boolean {
        return LitePal.isExist(TasksManagerModel::class.java, "mid = ?", mid)
    }

    fun addTask(mid: String, name: String, url: String, path: String): TasksManagerModel? {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null
        }
        //判断是否已下载过
        if (isHasMusic(mid)) {
            ToastUtils.show("下载列表已存在 $name")
            return null
        }
        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        val id = FileDownloadUtils.generateId(url, path)
        val model = TasksManagerModel()
        model.id = id
        model.mid = mid
        model.name = name
        model.url = url
        model.path = path
        model.finish = false
        model.saveOrUpdate("id = ?", id.toString())
        return model
    }

    fun updateTask(id: Int) {
        val model = TasksManagerModel()
        model.finish = true
        model.update(id.toLong())
        model.saveOrUpdate("id = ?", id.toString())
    }

}
