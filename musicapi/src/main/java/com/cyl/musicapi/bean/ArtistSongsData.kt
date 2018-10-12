package com.cyl.musicapi.bean


import com.cyl.musicapi.playlist.MusicInfo
import com.google.gson.annotations.SerializedName

data class ArtistSongsData(@SerializedName("data")
                           val data: ArtistSongs,
                           @SerializedName("status")
                           val status: Boolean = false,
                           @SerializedName("msg")
                           val msg: String = "")

data class AlbumData(@SerializedName("data")
                     val data: AlbumBean,
                     @SerializedName("status")
                     val status: Boolean = false,
                     @SerializedName("msg")
                     val msg: String = "")

data class AlbumBean(@SerializedName("name")
                     val name: String = "",
                     @SerializedName("cover")
                     val cover: String? = null,
                     @SerializedName("desc")
                     val desc: String? = null,
                     @SerializedName("publishTime")
                     val publishTime: Long,
                     @SerializedName("artist")
                     val artist: ArtistItem,
                     @SerializedName("songs")
                     val songs: List<MusicInfo>)

data class ArtistSongs(@SerializedName("detail")
                       val detail: ArtistItem,
                       @SerializedName("songs")
                       val songs: List<MusicInfo>)


data class ArtistItem(@SerializedName("name")
                      val name: String = "",
                      @SerializedName("id")
                      val id: String = "",
                      @SerializedName("cover")
                      val cover: String? = null,
                      @SerializedName("desc")
                      val desc: String? = null)


