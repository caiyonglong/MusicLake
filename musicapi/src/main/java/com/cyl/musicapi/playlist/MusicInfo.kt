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
                     var vendor: String? = "",
                     @SerializedName("commentId")
                     val commentId: String? = "",
                     @SerializedName("dl")
                     val dl: Boolean = false,
                     @SerializedName("cp")
                     val cp: Boolean = false)

data class CollectBatchBean(@SerializedName("ids")
                            val ids: List<String>? = null,
                            @SerializedName("vendor")
                            val vendor: String? = null)

data class CollectBatch2Bean(@SerializedName("collects")
                             val collects: MutableList<CollectDetail>? = null)

data class CollectDetail(@SerializedName("id")
                         val id: String,
                         @SerializedName("vendor")
                         val vendor: String)

data class CollectResult(@SerializedName("failedList")
                         val failedList: List<CollectFailed>?)

data class CollectFailed(@SerializedName("id")
                         val id: String?,
                         @SerializedName("msg")
                         val msg: String?)



