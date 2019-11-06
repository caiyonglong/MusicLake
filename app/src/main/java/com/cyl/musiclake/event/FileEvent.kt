package com.cyl.musiclake.event

/**
 * Des    : 文件操作事件，增加或删除
 * Author : master.
 * Date   : 2018/8/25 .
 */
data class FileEvent(val isCache: Boolean = false)