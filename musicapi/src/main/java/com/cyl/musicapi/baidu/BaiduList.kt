package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class BaiduList(@SerializedName("error_code")
                     val errorCode: Int = 0,
                     @SerializedName("content")
                     val content: List<ContentItem>?)


data class ContentItem(@SerializedName("pic_s210")
                       val picS210: String = "",
                       @SerializedName("web_url")
                       val webUrl: String = "",
                       @SerializedName("pic_s444")
                       val picS444: String = "",
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("count")
                       val count: Int = 0,
                       @SerializedName("comment")
                       val comment: String = "",
                       @SerializedName("type")
                       val type: Int = 0,
                       @SerializedName("pic_s192")
                       val picS192: String = "",
                       @SerializedName("content")
                       val content: List<Item>?,
                       @SerializedName("pic_s260")
                       val picS260: String = "")

data class Item(@SerializedName("all_rate")
                val allRate: String = "",
                @SerializedName("song_id")
                val songId: String = "",
                @SerializedName("rank_change")
                val rankChange: String = "",
                @SerializedName("biaoshi")
                val biaoshi: String = "",
                @SerializedName("author")
                val author: String = "",
                @SerializedName("album_id")
                val albumId: String = "",
                @SerializedName("title")
                val title: String = "",
                @SerializedName("album_title")
                val albumTitle: String = "")


