package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class SongItem(@SerializedName("songName")
                        val songName: String = "",
                        @SerializedName("albumName")
                        val albumName: String = "",
                        @SerializedName("linkCode")
                        val linkCode: Int = 0,
                        @SerializedName("format")
                        val format: String = "",
                        @SerializedName("albumId")
                        val albumId: Int = 0,
                        @SerializedName("artistId")
                        val artistId: String = "",
                        @SerializedName("source")
                        val source: String = "",
                        @SerializedName("songPicBig")
                        val songPicBig: String = "",
                        @SerializedName("version")
                        val version: String = "",
                        @SerializedName("queryId")
                        val queryId: String = "",
                        @SerializedName("songLink")
                        val songLink: String = "",
                        @SerializedName("size")
                        val size: Int = 0,
                        @SerializedName("rate")
                        val rate: Int = 0,
                        @SerializedName("lrcLink")
                        val lrcLink: String = "",
                        @SerializedName("copyType")
                        val copyType: Int = 0,
                        @SerializedName("artistName")
                        val artistName: String = "",
                        @SerializedName("time")
                        val time: Int = 0,
                        @SerializedName("relateStatus")
                        val relateStatus: String = "",
                        @SerializedName("songPicSmall")
                        val songPicSmall: String = "",
                        @SerializedName("songId")
                        val songId: Int = 0,
                        @SerializedName("songPicRadio")
                        val songPicRadio: String = "",
                        @SerializedName("showLink")
                        val showLink: String = "",
                        @SerializedName("resourceType")
                        val resourceType: String = "")


data class Data(@SerializedName("xcode")
                val xcode: String = "",
                @SerializedName("songList")
                val songList: List<SongItem>?)


data class BaiduSongInfo(@SerializedName("data")
                         val data: Data,
                         @SerializedName("errorCode")
                         val errorCode: Int = 0)


