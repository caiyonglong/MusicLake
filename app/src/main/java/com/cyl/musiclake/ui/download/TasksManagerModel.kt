package com.cyl.musiclake.ui.download

import org.litepal.crud.LitePalSupport

/**
 * Created by yonglong on 2018/1/23.
 */

class TasksManagerModel : LitePalSupport() {
    //LitePal自动递增ID
    var id: Int = 0
    var tid: Int = 0
    var mid: String? = null
    var name: String? = null
    var url: String? = null
    var path: String? = null
    var finish: Boolean = false
    var cache: Boolean = true
}