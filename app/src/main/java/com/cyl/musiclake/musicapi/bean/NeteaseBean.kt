package com.cyl.musiclake.musicapi.bean


import com.google.gson.annotations.SerializedName

data class NeteaseBean(@SerializedName("data")
                       val data: Data,
                       @SerializedName("status")
                       val status: Boolean = false)


data class ArtistsItem(@SerializedName("name")
                       val name: String = "",
                       @SerializedName("id")
                       val id: Int = 0)


data class Album(@SerializedName("cover")
                 val cover: String = "",
                 @SerializedName("name")
                 val name: String = "",
                 @SerializedName("id")
                 val id: Int = 0)


data class CoverData(@SerializedName("cover")
                     val cover: String = "",
                     @SerializedName("playCount")
                     val playCount: Int = 0,
                     @SerializedName("name")
                     val name: String = "",
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


