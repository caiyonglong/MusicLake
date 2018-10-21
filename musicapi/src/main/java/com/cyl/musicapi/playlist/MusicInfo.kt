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
                     var id: String?,
                     @SerializedName("songId")
                     val songId: String? = null,
                     @SerializedName("name")
                     val name: String? = "",
                     @SerializedName("artists")
                     val artists: List<ArtistsItem>?,
                     @SerializedName("album")
                     val album: Album,
                     @SerializedName("vendor")
                     var vendor: String? = "",
                     @SerializedName("dl")
                     val dl: Boolean = false,
                     @SerializedName("cp")
                     val cp: Boolean = false,
                     @SerializedName("quality")
                     val quality: QualityBean?)

data class QualityBean(@SerializedName("192")
                       val high: Boolean = false,
                       @SerializedName("320")
                       val hq: Boolean = false,
                       @SerializedName("999")
                       val sq: Boolean = false)

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



