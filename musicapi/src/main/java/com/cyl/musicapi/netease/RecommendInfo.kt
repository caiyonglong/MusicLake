package com.cyl.musicapi.netease

import com.google.gson.annotations.SerializedName

/**
 * Created by cyl on 2018/11/6.
 */

data class RecommendSongsInfo(@SerializedName("code")
                              val code: Int = 0,
                              @SerializedName("msg")
                              val msg: String = "",
                              @SerializedName("recommend")
                              val recommend: MutableList<RecommendItem>?)

data class RecommendPlaylist(@SerializedName("recommend")
                             val recommend: MutableList<PlaylistsItem>?,
                             @SerializedName("code")
                             val code: Int = 0,
                             @SerializedName("msg")
                             val msg: String = "",
                             @SerializedName("featureFirst")
                             val featureFirst: Boolean = false,
                             @SerializedName("haveRcmdSongs")
                             val haveRcmdSongs: Boolean = false)

data class BMusic(@SerializedName("extension")
                  val extension: String = "",
                  @SerializedName("size")
                  val size: Int = 0,
                  @SerializedName("volumeDelta")
                  val volumeDelta: Double = 0.0,
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("bitrate")
                  val bitrate: Int = 0,
                  @SerializedName("playTime")
                  val playTime: Int = 0,
                  @SerializedName("id")
                  val id: String,
                  @SerializedName("dfsId")
                  val dfsId: Int = 0,
                  @SerializedName("sr")
                  val sr: Int = 0)


data class Artist(@SerializedName("picUrl")
                  val picUrl: String = "",
                  @SerializedName("img1v1Url")
                  val imgVUrl: String = "",
                  @SerializedName("briefDesc")
                  val briefDesc: String = "",
                  @SerializedName("musicSize")
                  val musicSize: Int = 0,
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("img1v1Id")
                  val imgVId: Int = 0,
                  @SerializedName("id")
                  val id: String,
                  @SerializedName("picId")
                  val picId: Int = 0,
                  @SerializedName("albumSize")
                  val albumSize: Int = 0,
                  @SerializedName("trans")
                  val trans: String = "")


data class MMusic(@SerializedName("extension")
                  val extension: String = "",
                  @SerializedName("size")
                  val size: Int = 0,
                  @SerializedName("volumeDelta")
                  val volumeDelta: Double = 0.0,
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("bitrate")
                  val bitrate: Int = 0,
                  @SerializedName("playTime")
                  val playTime: Int = 0,
                  @SerializedName("id")
                  val id: String,
                  @SerializedName("dfsId")
                  val dfsId: Int = 0,
                  @SerializedName("sr")
                  val sr: Int = 0)


data class ArtistsItem(@SerializedName("picUrl")
                       val picUrl: String = "",
                       @SerializedName("img1v1Url")
                       val imgVUrl: String = "",
                       @SerializedName("briefDesc")
                       val briefDesc: String = "",
                       @SerializedName("musicSize")
                       val musicSize: Int = 0,
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("img1v1Id")
                       val imgVId: Int = 0,
                       @SerializedName("id")
                       val id: String,
                       @SerializedName("picId")
                       val picId: Int = 0,
                       @SerializedName("albumSize")
                       val albumSize: Int = 0,
                       @SerializedName("trans")
                       val trans: String = "")


data class RecommendItem(@SerializedName("no")
                         val no: Int = 0,
                         @SerializedName("reason")
                         val reason: String = "",
                         @SerializedName("copyright")
                         val copyright: Int = 0,
                         @SerializedName("dayPlays")
                         val dayPlays: Int = 0,
                         @SerializedName("fee")
                         val fee: Int = 0,
                         @SerializedName("privilege")
                         val privilege: Privilege,
                         @SerializedName("mMusic")
                         val mMusic: MMusic,
                         @SerializedName("bMusic")
                         val bMusic: BMusic,
                         @SerializedName("duration")
                         val duration: Int = 0,
                         @SerializedName("score")
                         val score: Int = 0,
                         @SerializedName("rtype")
                         val rtype: Int = 0,
                         @SerializedName("starred")
                         val starred: Boolean = false,
                         @SerializedName("artists")
                         val artists: MutableList<ArtistsItem>?,
                         @SerializedName("popularity")
                         val popularity: Int = 0,
                         @SerializedName("playedNum")
                         val playedNum: Int = 0,
                         @SerializedName("hearTime")
                         val hearTime: Int = 0,
                         @SerializedName("starredNum")
                         val starredNum: Int = 0,
                         @SerializedName("id")
                         val id: String,
                         @SerializedName("alg")
                         val alg: String = "",
                         @SerializedName("album")
                         val album: Album,
                         @SerializedName("lMusic")
                         val lMusic: LMusic,
                         @SerializedName("commentThreadId")
                         val commentThreadId: String = "",
                         @SerializedName("copyFrom")
                         val copyFrom: String = "",
                         @SerializedName("ftype")
                         val ftype: Int = 0,
                         @SerializedName("copyrightId")
                         val copyrightId: Int = 0,
                         @SerializedName("hMusic")
                         val hMusic: HMusic,
                         @SerializedName("mvid")
                         val mvid: Int = 0,
                         @SerializedName("name")
                         val name: String = "",
                         @SerializedName("disc")
                         val disc: String = "",
                         @SerializedName("position")
                         val position: Int = 0,
                         @SerializedName("status")
                         val status: Int = 0)


data class Album(@SerializedName("transName")
                 val transName: String = "",
                 @SerializedName("publishTime")
                 val publishTime: Long = 0,
                 @SerializedName("picId_str")
                 val picIdStr: String = "",
                 @SerializedName("artist")
                 val artist: Artist,
                 @SerializedName("blurPicUrl")
                 val blurPicUrl: String = "",
                 @SerializedName("description")
                 val description: String = "",
                 @SerializedName("commentThreadId")
                 val commentThreadId: String = "",
                 @SerializedName("pic")
                 val pic: Long = 0,
                 @SerializedName("type")
                 val type: String = "",
                 @SerializedName("tags")
                 val tags: String = "",
                 @SerializedName("picUrl")
                 val picUrl: String = "",
                 @SerializedName("companyId")
                 val companyId: Int = 0,
                 @SerializedName("size")
                 val size: Int = 0,
                 @SerializedName("briefDesc")
                 val briefDesc: String = "",
                 @SerializedName("copyrightId")
                 val copyrightId: Int = 0,
                 @SerializedName("artists")
                 val artists: MutableList<ArtistsItem>?,
                 @SerializedName("name")
                 val name: String = "",
                 @SerializedName("company")
                 val company: String = "",
                 @SerializedName("subType")
                 val subType: String = "",
                 @SerializedName("id")
                 val id: String,
                 @SerializedName("picId")
                 val picId: Long = 0,
                 @SerializedName("status")
                 val status: Int = 0)


data class Privilege(@SerializedName("st")
                     val st: Int = 0,
                     @SerializedName("flag")
                     val flag: Int = 0,
                     @SerializedName("subp")
                     val subp: Int = 0,
                     @SerializedName("fl")
                     val fl: Int = 0,
                     @SerializedName("fee")
                     val fee: Int = 0,
                     @SerializedName("dl")
                     val dl: Int = 0,
                     @SerializedName("cp")
                     val cp: Int = 0,
                     @SerializedName("preSell")
                     val preSell: Boolean = false,
                     @SerializedName("cs")
                     val cs: Boolean = false,
                     @SerializedName("toast")
                     val toast: Boolean = false,
                     @SerializedName("maxbr")
                     val maxbr: Int = 0,
                     @SerializedName("id")
                     val id: String,
                     @SerializedName("pl")
                     val pl: Int = 0,
                     @SerializedName("sp")
                     val sp: Int = 0,
                     @SerializedName("payed")
                     val payed: Int = 0)


data class LMusic(@SerializedName("extension")
                  val extension: String = "",
                  @SerializedName("size")
                  val size: Int = 0,
                  @SerializedName("volumeDelta")
                  val volumeDelta: Double = 0.0,
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("bitrate")
                  val bitrate: Int = 0,
                  @SerializedName("playTime")
                  val playTime: Int = 0,
                  @SerializedName("id")
                  val id: String,
                  @SerializedName("dfsId")
                  val dfsId: Int = 0,
                  @SerializedName("sr")
                  val sr: Int = 0)


data class HMusic(@SerializedName("extension")
                  val extension: String = "",
                  @SerializedName("size")
                  val size: Int = 0,
                  @SerializedName("volumeDelta")
                  val volumeDelta: Double = 0.0,
                  @SerializedName("name")
                  val name: String = "",
                  @SerializedName("bitrate")
                  val bitrate: Int = 0,
                  @SerializedName("playTime")
                  val playTime: Int = 0,
                  @SerializedName("id")
                  val id: String,
                  @SerializedName("dfsId")
                  val dfsId: Int = 0,
                  @SerializedName("sr")
                  val sr: Int = 0)


