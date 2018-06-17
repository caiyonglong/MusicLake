package com.cyl.musiclake.api.netease


import com.google.gson.annotations.SerializedName

data class ArtistsItem(@SerializedName("lastRank")
                       val lastRank: Int = 0,
                       @SerializedName("img1v1Url")
                       val imgVUrl: String = "",
                       @SerializedName("musicSize")
                       val musicSize: Int = 0,
                       @SerializedName("img1v1Id")
                       val imgVId: Long = 0,
                       @SerializedName("albumSize")
                       val albumSize: Int = 0,
                       @SerializedName("picUrl")
                       val picUrl: String = "",
                       @SerializedName("score")
                       val score: Int = 0,
                       @SerializedName("topicPerson")
                       val topicPerson: Int = 0,
                       @SerializedName("briefDesc")
                       val briefDesc: String = "",
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("id")
                       val id: Int = 0,
                       @SerializedName("picId")
                       val picId: Long = 0,
                       @SerializedName("trans")
                       val trans: String = "")


data class List2(@SerializedName("artists")
                val artists: List<ArtistsItem>,
                @SerializedName("updateTime")
                val updateTime: Long = 0,
                @SerializedName("type")
                val type: Int = 0)


data class Artists(@SerializedName("code")
                   val code: Int = 0,
                   @SerializedName("list")
                   val list: List2)


