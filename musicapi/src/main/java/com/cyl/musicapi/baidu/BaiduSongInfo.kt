package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

//"songList": [{
//			"queryId": "265715650",
//			"songId": 265715650,
//			"songName": "\u5927\u9c7c",
//			"artistId": "164528737",
//			"artistName": "\u5468\u6df1",
//			"albumId": 265715651,
//			"albumName": "\u5927\u9c7c",
//			"songPicSmall": "http:\/\/qukufile2.qianqian.com\/data2\/pic\/0d359ec1be6f5365f92d4c83d3eeb022\/603758238\/603758238.jpg@s_2,w_90,h_90",
//			"songPicBig": "http:\/\/qukufile2.qianqian.com\/data2\/pic\/0d359ec1be6f5365f92d4c83d3eeb022\/603758238\/603758238.jpg@s_2,w_150,h_150",
//			"songPicRadio": "http:\/\/qukufile2.qianqian.com\/data2\/pic\/0d359ec1be6f5365f92d4c83d3eeb022\/603758238\/603758238.jpg@s_2,w_300,h_300",
//			"lrcLink": "http:\/\/qukufile2.qianqian.com\/data2\/lrc\/7a07a153b8a9a9d918bbee676b7d4118\/603149730\/603149730.lrc",
//			"version": "",
//			"copyType": 0,
//			"time": 313,
//			"linkCode": 22000,
//			"songLink": "http:\/\/zhangmenshiting.qianqian.com\/data2\/music\/a612909cdafecf20933bd2942c43421c\/596603939\/26571565036000128.mp3?xcode=ee75606752833ea05fa4084c7da1aa86",
//			"showLink": "http:\/\/zhangmenshiting.qianqian.com\/data2\/music\/a612909cdafecf20933bd2942c43421c\/596603939\/26571565036000128.mp3?xcode=ee75606752833ea05fa4084c7da1aa86",
//			"format": "mp3",
//			"rate": 128,
//			"size": 5021639,
//			"relateStatus": "0",
//			"resourceType": "0",
//			"source": "web"
//		}]
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
                        @SerializedName("ting_uid")
                        val tingUid: String = "",
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



data class SongPlayRes(
    @SerializedName("bitrate")
    val bitrate: Bitrate,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("songinfo")
    val songinfo: SongInfo
)

data class Bitrate(
    @SerializedName("file_bitrate")
    val fileBitrate: Int,
    @SerializedName("file_duration")
    val fileDuration: Int,
    @SerializedName("file_extension")
    val fileExtension: String,
    @SerializedName("file_link")
    val fileLink: String,
    @SerializedName("file_size")
    val fileSize: Int,
    @SerializedName("free")
    val free: Int,
    @SerializedName("hash")
    val hash: String,
    @SerializedName("show_link")
    val showLink: String,
    @SerializedName("song_file_id")
    val songFileId: Int
)

data class SongInfo(
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("album_no")
    val albumNo: String,
    @SerializedName("album_title")
    val albumTitle: String,
    @SerializedName("all_artist_id")
    val allArtistId: String,
    @SerializedName("all_artist_ting_uid")
    val allArtistTingUid: String,
    @SerializedName("all_rate")
    val allRate: String,
    @SerializedName("artist_id")
    val artistId: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("bitrate_fee")
    val bitrateFee: String,
    @SerializedName("charge")
    val charge: Int,
    @SerializedName("copy_type")
    val copyType: String,
    @SerializedName("has_mv")
    val hasMv: Int,
    @SerializedName("has_mv_mobile")
    val hasMvMobile: Int,
    @SerializedName("havehigh")
    val havehigh: Int,
    @SerializedName("is_first_publish")
    val isFirstPublish: Int,
    @SerializedName("korean_bb_song")
    val koreanBbSong: String,
    @SerializedName("learn")
    val learn: Int,
    @SerializedName("lrclink")
    val lrclink: String,
    @SerializedName("piao_id")
    val piaoId: String,
    @SerializedName("pic_big")
    val picBig: String,
    @SerializedName("pic_huge")
    val picHuge: String,
    @SerializedName("pic_premium")
    val picPremium: String,
    @SerializedName("pic_radio")
    val picRadio: String,
    @SerializedName("pic_small")
    val picSmall: String,
    @SerializedName("play_type")
    val playType: Int,
    @SerializedName("relate_status")
    val relateStatus: String,
    @SerializedName("resource_type")
    val resourceType: String,
    @SerializedName("resource_type_ext")
    val resourceTypeExt: String,
    @SerializedName("si_proxycompany")
    val siProxycompany: String,
    @SerializedName("song_id")
    val songId: String,
    @SerializedName("song_source")
    val songSource: String,
    @SerializedName("special_type")
    val specialType: Int,
    @SerializedName("ting_uid")
    val tingUid: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("toneid")
    val toneid: String
)