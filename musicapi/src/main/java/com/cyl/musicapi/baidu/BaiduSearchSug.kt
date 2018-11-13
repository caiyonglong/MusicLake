package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class SongSug(@SerializedName("resource_type_ext")
                    val resourceTypeExt: String = "",
                    @SerializedName("artistname")
                    val artistname: String = "",
                    @SerializedName("resource_type")
                    val resourceType: String = "",
                    @SerializedName("weight")
                    val weight: String = "",
                    @SerializedName("bitrate_fee")
                    val bitrateFee: String = "",
                    @SerializedName("has_mv")
                    val hasMv: String = "",
                    @SerializedName("control")
                    val control: String = "",
                    @SerializedName("songname")
                    val songname: String = "",
                    @SerializedName("yyr_artist")
                    val yyrArtist: String = "",
                    @SerializedName("encrypted_songid")
                    val encryptedSongid: String = "",
                    @SerializedName("resource_provider")
                    val resourceProvider: String = "",
                    @SerializedName("songid")
                    val songid: String = "",
                    @SerializedName("info")
                    val info: String = "")


data class ArtistSug(@SerializedName("yyr_artist")
                      val yyrArtist: String = "",
                      @SerializedName("artistname")
                      val artistname: String = "",
                      @SerializedName("weight")
                      val weight: String = "",
                      @SerializedName("artistid")
                      val artistid: String = "",
                      @SerializedName("artistpic")
                      val artistpic: String = "")


data class BaiduSearchSug(@SerializedName("song")
                          val song: List<SongSug>?,
                          @SerializedName("artist")
                          val artist: List<ArtistSug>?,
                          @SerializedName("album")
                          val album: List<AlbumSug>?,
                          @SerializedName("error_code")
                          val errorCode: Int = 0,
                          @SerializedName("order")
                          val order: String = "")


data class AlbumSug(@SerializedName("albumname")
                     val albumname: String = "",
                     @SerializedName("resource_type_ext")
                     val resourceTypeExt: String = "",
                     @SerializedName("artistname")
                     val artistname: String = "",
                     @SerializedName("weight")
                     val weight: String = "",
                     @SerializedName("albumid")
                     val albumid: String = "",
                     @SerializedName("artistpic")
                     val artistpic: String = "")


