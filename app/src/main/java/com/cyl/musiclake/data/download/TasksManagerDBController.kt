package com.cyl.musiclake.data.download

import android.text.TextUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils
import org.litepal.LitePal

/**
 * Created by yonglong on 2018/1/23.
 */

class TasksManagerDBController {


    fun addTask(mid: String, name: String, url: String, path: String): TasksManagerModel? {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
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

    fun finishTask(id: Int) {
        val model = TasksManagerModel()
        model.finish = true
        model.update(id.toLong())
    }
}
