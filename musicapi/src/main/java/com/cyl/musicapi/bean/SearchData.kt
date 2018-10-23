package com.cyl.musicapi.bean


import com.cyl.musicapi.playlist.MusicInfo
import com.google.gson.annotations.SerializedName

data class Qq(@SerializedName("total")
              val total: String = "",
              @SerializedName("songs")
              val songs: List<MusicInfo>?,
              @SerializedName("keyword")
              val keyword: String = "")

data class SearchResult(@SerializedName("total")
                        val total: String = "",
                        @SerializedName("songs")
                        val songs: List<MusicInfo>?)


data class SearchData(@SerializedName("data")
                      val data: Data,
                      @SerializedName("status")
                      val status: Boolean = false,
                      @SerializedName("msg")
                      val msg: String = "")

data class SearchSingleData(@SerializedName("data")
                            val data: SearchResult,
                            @SerializedName("status")
                            val status: Boolean = false,
                            @SerializedName("msg")
                            val msg: String = "")


data class Xiami(@SerializedName("total")
                 val total: String = "",
                 @SerializedName("songs")
                 val songs: List<MusicInfo>?)


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
                   val total: Int? = 0,
                   @SerializedName("songs")
                   val songs: List<MusicInfo>? = null)


data class Data(@SerializedName("qq")
                val qq: Qq,
                @SerializedName("xiami")
                val xiami: Xiami,
                @SerializedName("netease")
                val netease: Netease)


data class SongDetail(@SerializedName("data")
                      val data: MusicInfo,
                      @SerializedName("status")
                      val status: Boolean = false,
                      @SerializedName("msg")
                      val msg: String = "")

data class BatchSongDetail(@SerializedName("data")
                           val data: List<MusicInfo>,
                           @SerializedName("status")
                           val status: Boolean = false,
                           @SerializedName("msg")
                           val msg: String = "",
                           @SerializedName("log")
                           val log: LogDetail?)

data class AnyBatchSongDetail(@SerializedName("data")
                              val data: List<MusicInfo>,
                              @SerializedName("status")
                              val status: Boolean = false,
                              @SerializedName("msg")
                              val msg: String = "",
                              @SerializedName("log")
                              val log: LogDetail?)

data class LogDetail(@SerializedName("msg")
                     val msg: String)