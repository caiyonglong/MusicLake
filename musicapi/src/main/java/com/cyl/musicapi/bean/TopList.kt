package com.cyl.musicapi.bean

import com.cyl.musicapi.playlist.MusicInfo
import com.google.gson.annotations.SerializedName

data class NeteaseBean(@SerializedName("data")
                       val data: TopData,
                       @SerializedName("status")
                       val status: Boolean = false,
                       @SerializedName("msg")
                       val msg: String = "")

data class TopData(@SerializedName("cover")
                   val cover: String = "",
                   @SerializedName("playCount")
                   val playCount: Long = 0,
                   @SerializedName("name")
                   val name: String? = null,
                   @SerializedName("description")
                   val description: String = "",
                   @SerializedName("list")
                   val list: List<MusicInfo>?)


