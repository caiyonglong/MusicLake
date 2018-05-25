package com.cyl.musiclake.data.db

import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.SearchHistoryBean
import com.cyl.musiclake.utils.ToastUtils

import org.litepal.crud.DataSupport

/**
 * 数据库操作类
 * Created by master on 2018/4/26.
 */

/**
 * 获取搜索历史
 */
fun getAllSearchInfo(): List<SearchHistoryBean> {
    return DataSupport.findAll(SearchHistoryBean::class.java)
}

/**
 * 删除歌单
 */
fun deletePlaylist(pId: Long) {
    val result = DataSupport.delete(Playlist::class.java, pId)
    if (result != 0) {
        ToastUtils.show(MusicApp.getAppContext(), "删除成功")
    }
}

/**
 * 增加搜索
 */
fun addSearchInfo(info: String) {
    val id = System.currentTimeMillis()
    val queryInfo = SearchHistoryBean(id, info)
    DataSupport.isExist(SearchHistoryBean::class.java, "title = $queryInfo")
}
