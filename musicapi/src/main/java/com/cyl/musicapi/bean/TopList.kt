package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class NeteaseBean(@SerializedName("data")
                       val data: TopData,
                       @SerializedName("status")
                       val status: Boolean = false)

data class TopData(@SerializedName("cover")
                   val cover: String = "",
                   @SerializedName("playCount")
                   val playCount: Long = 0,
                   @SerializedName("name")
                   val name: String? = null,
                   @SerializedName("description")
                   val description: String = "",
                   @SerializedName("list")
                   val list: List<ListItem>?)


data class ListItem(@SerializedName("artists")
                    val artists: List<ArtistsItem>?,
                    @SerializedName("album")
                    val album: Album,
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("commentId")
                    val commentId: Int = 0,
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("cp")
                    val cp: Boolean = false)


