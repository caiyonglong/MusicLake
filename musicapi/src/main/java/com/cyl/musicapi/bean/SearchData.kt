package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class Qq(@SerializedName("total")
              val total: String = "",
              @SerializedName("songs")
              val songs: List<SongsItem>?,
              @SerializedName("keyword")
              val keyword: String = "")


data class SearchData(@SerializedName("data")
                      val data: Data,
                      @SerializedName("status")
                      val status: Boolean = false)


data class Xiami(@SerializedName("total")
                 val total: String = "",
                 @SerializedName("songs")
                 val songs: List<SongsItem>?)


data class ArtistsItem(@SerializedName("name")
                       val name: String = "",
                       @SerializedName("id")
                       val id: String = "")


data class Album(@SerializedName("cover")
                 val cover: String = "",
                 @SerializedName("name")
                 val name: String = "",
                 @SerializedName("id")
                 val id: String = "")


data class Netease(@SerializedName("total")
                   val total: String = "",
                   @SerializedName("songs")
                   val songs: List<SongsItem>?)


data class Data(@SerializedName("qq")
                val qq: Qq,
                @SerializedName("xiami")
                val xiami: Xiami,
                @SerializedName("netease")
                val netease: Netease)


data class SongsItem(@SerializedName("artists")
                     val artists: List<ArtistsItem>?,
                     @SerializedName("album")
                     val album: Album,
                     @SerializedName("name")
                     val name: String = "",
                     @SerializedName("commentId")
                     val commentId: String = "",
                     @SerializedName("id")
                     val id: String = "",
                     @SerializedName("cp")
                     val cp: Boolean = false)


data class SongDetail(@SerializedName("data")
                      val data: SongsItem,
                      @SerializedName("status")
                      val status: Boolean = false)

data class BatchSongDetail(@SerializedName("data")
                           val data: List<SongsItem>,
                           @SerializedName("status")
                           val status: Boolean = false,
                           @SerializedName("msg")
                           val msg: String = "",
                           @SerializedName("log")
                           val log: LogDetail?)

data class LogDetail(@SerializedName("msg")
                     val msg: String)