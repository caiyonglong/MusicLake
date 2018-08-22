package com.cyl.musicapi.playlist


import com.google.gson.annotations.SerializedName

/**
 * 数据结构
 * http://rap2.taobao.org/repository/editor?id=9460&itf=52687
 */

data class ArtistsItem(@SerializedName("id")
                       val id: String = "",
                       @SerializedName("name")
                       val name: String = "")

data class Album(@SerializedName("id")
                 val id: String? = "",
                 @SerializedName("name")
                 val name: String? = "",
                 @SerializedName("cover")
                 val cover: String? = "")


data class MusicInfo(@SerializedName("id")
                     val id: String?,
                     @SerializedName("songId")
                     val songId: String?,
                     @SerializedName("name")
                     val name: String? = "",
                     @SerializedName("artists")
                     val artists: List<ArtistsItem>?,
                     @SerializedName("album")
                     val album: Album,
                     @SerializedName("vendor")
                     val vendor: String? = "",
                     @SerializedName("commentId")
                     val commentId: String? = "",
                     @SerializedName("cp")
                     val cp: Boolean = false)

