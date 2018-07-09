package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class ArtistSongsData(@SerializedName("data")
                           val data: ArtistSongs,
                           @SerializedName("status")
                           val status: Boolean = false)

data class ArtistSongs(@SerializedName("detail")
                       val detail: ArtistItem,
                       @SerializedName("songs")
                       val songs: List<SongsItem>)


data class ArtistItem(@SerializedName("name")
                      val name: String = "",
                      @SerializedName("id")
                      val id: String = "",
                      @SerializedName("cover")
                      val cover: String? = null,
                      @SerializedName("desc")
                      val desc: String? = null)
