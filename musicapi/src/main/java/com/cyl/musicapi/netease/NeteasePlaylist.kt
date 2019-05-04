package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class Creator(@SerializedName("birthday")
                   val birthday: Long = 0,
                   @SerializedName("detailDescription")
                   val detailDescription: String = "",
                   @SerializedName("backgroundUrl")
                   val backgroundUrl: String = "",
                   @SerializedName("gender")
                   val gender: Int = 0,
                   @SerializedName("city")
                   val city: Int = 0,
                   @SerializedName("signature")
                   val signature: String = "",
                   @SerializedName("description")
                   val description: String = "",
                   @SerializedName("remarkName")
                   val remarkName: String? = null,
                   @SerializedName("accountStatus")
                   val accountStatus: Int = 0,
                   @SerializedName("avatarImgId")
                   val avatarImgId: Long = 0,
                   @SerializedName("defaultAvatar")
                   val defaultAvatar: Boolean = false,
                   @SerializedName("backgroundImgIdStr")
                   val backgroundImgIdStr: String = "",
                   @SerializedName("avatarImgIdStr")
                   val avatarImgIdstr: String = "",
                   @SerializedName("province")
                   val province: Int = 0,
                   @SerializedName("nickname")
                   val nickname: String = "",
                   @SerializedName("expertTags")
                   val expertTags: MutableList<String>?,
                   @SerializedName("djStatus")
                   val djStatus: Int = 0,
                   @SerializedName("avatarUrl")
                   val avatarUrl: String = "",
                   @SerializedName("authStatus")
                   val authStatus: Int = 0,
                   @SerializedName("vipType")
                   val vipType: Int = 0,
                   @SerializedName("followed")
                   val followed: Boolean = false,
                   @SerializedName("userId")
                   val userId: Int = 0,
                   @SerializedName("mutual")
                   val mutual: Boolean = false,
                   @SerializedName("avatarImgId_str")
                   val avatarImgIdStr: String = "",
                   @SerializedName("authority")
                   val authority: Int = 0,
                   @SerializedName("userType")
                   val userType: Int = 0,
                   @SerializedName("backgroundImgId")
                   val backgroundImgId: Long = 0)


data class SubscribersItem(@SerializedName("birthday")
                           val birthday: Long = 0,
                           @SerializedName("detailDescription")
                           val detailDescription: String = "",
                           @SerializedName("backgroundUrl")
                           val backgroundUrl: String = "",
                           @SerializedName("gender")
                           val gender: Int = 0,
                           @SerializedName("city")
                           val city: Int = 0,
                           @SerializedName("signature")
                           val signature: String = "",
                           @SerializedName("description")
                           val description: String = "",
                           @SerializedName("remarkName")
                           val remarkName: String = "",
                           @SerializedName("accountStatus")
                           val accountStatus: Int = 0,
                           @SerializedName("avatarImgId")
                           val avatarImgId: Long = 0,
                           @SerializedName("defaultAvatar")
                           val defaultAvatar: Boolean = false,
                           @SerializedName("backgroundImgIdStr")
                           val backgroundImgIdStr: String = "",
                           @SerializedName("avatarImgIdStr")
                           val avatarImgIdSt: String = "",
                           @SerializedName("province")
                           val province: Int = 0,
                           @SerializedName("nickname")
                           val nickname: String = "",
                           @SerializedName("expertTags")
                           val expertTags: Any? = null,
                           @SerializedName("djStatus")
                           val djStatus: Int = 0,
                           @SerializedName("avatarUrl")
                           val avatarUrl: String = "",
                           @SerializedName("authStatus")
                           val authStatus: Int = 0,
                           @SerializedName("vipType")
                           val vipType: Int = 0,
                           @SerializedName("followed")
                           val followed: Boolean = false,
                           @SerializedName("userId")
                           val userId: Int = 0,
                           @SerializedName("mutual")
                           val mutual: Boolean = false,
                           @SerializedName("avatarImgId_str")
                           val avatarImgIdStr: String = "",
                           @SerializedName("authority")
                           val authority: Int = 0,
                           @SerializedName("userType")
                           val userType: Int = 0,
                           @SerializedName("backgroundImgId")
                           val backgroundImgId: Long = 0,
                           @SerializedName("experts")
                           val experts: Any? = null)


data class NeteasePlaylist(@SerializedName("lasttime")
                           val lasttime: Long = 0,
                           @SerializedName("total")
                           val total: Int = 0,
                           @SerializedName("code")
                           val code: Int = 0,
                           @SerializedName("more")
                           val more: Boolean = false,
                           @SerializedName("playlists")
                           val playlists: MutableList<PlaylistsItem>?,
                           @SerializedName("playlist")
                           val playlist: MutableList<PlaylistsItem>?)


data class NeteasePlaylistDetail(
        @SerializedName("code")
        val code: Int = 0,
        @SerializedName("playlist")
        val playlist: PlaylistsItem?)

data class PlaylistsItem(
    @SerializedName("adType")
    val adType: Int,
    @SerializedName("cloudTrackCount")
    val cloudTrackCount: Int,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("commentThreadId")
    val commentThreadId: String,
    @SerializedName("coverImgId")
    val coverImgId: Long,
    @SerializedName("coverImgId_str")
    val coverImgIdStr: String,
    @SerializedName("coverImgUrl")
    val coverImgUrl: String,
    @SerializedName("createTime")
    val createTime: Long,
    @SerializedName("creator")
    val creator: Creator,
    @SerializedName("description")
    val description: String,
    @SerializedName("highQuality")
    val highQuality: Boolean,
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("newImported")
    val newImported: Boolean,
    @SerializedName("ordered")
    val ordered: Boolean,
    @SerializedName("playCount")
    val playCount: Int,
    @SerializedName("privacy")
    val privacy: Int,
    @SerializedName("shareCount")
    val shareCount: Int,
    @SerializedName("specialType")
    val specialType: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("subscribed")
    val subscribed: Boolean,
    @SerializedName("subscribedCount")
    val subscribedCount: Int,
    @SerializedName("subscribers")
    val subscribers: MutableList<Subscriber>,
    @SerializedName("tags")
    val tags: MutableList<String>,
    @SerializedName("trackCount")
    val trackCount: Int,
    @SerializedName("trackIds")
    val trackIds: MutableList<TrackId>,
    @SerializedName("trackNumberUpdateTime")
    val trackNumberUpdateTime: Long,
    @SerializedName("trackUpdateTime")
    val trackUpdateTime: Long,
    @SerializedName("tracks")
    val tracks: MutableList<TracksItem>?,
    @SerializedName("updateTime")
    val updateTime: Long,
    @SerializedName("userId")
    val userId: Int
)

data class TracksItem(
    @SerializedName("a")
    val a: Any,
    @SerializedName("al")
    val al: Al,
    @SerializedName("alia")
    val alia: MutableList<Any>,
    @SerializedName("ar")
    val ar: MutableList<Ar>,
    @SerializedName("cd")
    val cd: String,
    @SerializedName("cf")
    val cf: String,
    @SerializedName("copyright")
    val copyright: Int,
    @SerializedName("cp")
    val cp: Int,
    @SerializedName("crbt")
    val crbt: Any,
    @SerializedName("djId")
    val djId: Int,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("fee")
    val fee: Int,
    @SerializedName("ftype")
    val ftype: Int,
    @SerializedName("h")
    val h: H,
    @SerializedName("id")
    val id: Int,
    @SerializedName("l")
    val l: L,
    @SerializedName("m")
    val m: M,
    @SerializedName("mst")
    val mst: Int,
    @SerializedName("mv")
    val mv: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("no")
    val no: Int,
    @SerializedName("pop")
    val pop: Int,
    @SerializedName("pst")
    val pst: Int,
    @SerializedName("publishTime")
    val publishTime: Long,
    @SerializedName("rt")
    val rt: Any,
    @SerializedName("rtUrl")
    val rtUrl: Any,
    @SerializedName("rtUrls")
    val rtUrls: MutableList<Any>,
    @SerializedName("rtype")
    val rtype: Int,
    @SerializedName("rurl")
    val rurl: Any,
    @SerializedName("s_id")
    val sId: Int,
    @SerializedName("st")
    val st: Int,
    @SerializedName("t")
    val t: Int,
    @SerializedName("tns")
    val tns: MutableList<String>,
    @SerializedName("v")
    val v: Int
)

data class L(
    @SerializedName("br")
    val br: Int,
    @SerializedName("fid")
    val fid: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("vd")
    val vd: Any?
)

data class M(
    @SerializedName("br")
    val br: Int,
    @SerializedName("fid")
    val fid: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("vd")
    val vd: Any?
)

data class Ar(
    @SerializedName("alias")
    val alias: MutableList<String>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("tns")
    val tns: MutableList<String>
)

data class H(
    @SerializedName("br")
    val br: Int,
    @SerializedName("fid")
    val fid: Int,
    @SerializedName("size")
    val size: Long,
    @SerializedName("vd")
    val vd: Any?
)

data class Al(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pic")
    val pic: Long,
    @SerializedName("picUrl")
    val picUrl: String,
    @SerializedName("pic_str")
    val picStr: String,
    @SerializedName("tns")
    val tns: MutableList<String>
)

data class TrackId(
    @SerializedName("id")
    val id: Int,
    @SerializedName("v")
    val v: Int
)

data class Subscriber(
    @SerializedName("accountStatus")
    val accountStatus: Int,
    @SerializedName("authStatus")
    val authStatus: Int,
    @SerializedName("authority")
    val authority: Int,
    @SerializedName("avatarImgId")
    val avatarImgId: Long,
    @SerializedName("avatarImgIdStr")
    val avatarImgIdStr: String,
    @SerializedName("avatarImgId_str")
    val avatarImgIdStr1: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("backgroundImgId")
    val backgroundImgId: Long,
    @SerializedName("backgroundImgIdStr")
    val backgroundImgIdStr: String,
    @SerializedName("backgroundUrl")
    val backgroundUrl: String,
    @SerializedName("birthday")
    val birthday: Long,
    @SerializedName("city")
    val city: Int,
    @SerializedName("defaultAvatar")
    val defaultAvatar: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("detailDescription")
    val detailDescription: String,
    @SerializedName("djStatus")
    val djStatus: Int,
    @SerializedName("expertTags")
    val expertTags: Any,
    @SerializedName("experts")
    val experts: Any,
    @SerializedName("followed")
    val followed: Boolean,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("mutual")
    val mutual: Boolean,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("province")
    val province: Int,
    @SerializedName("remarkName")
    val remarkName: Any,
    @SerializedName("signature")
    val signature: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userType")
    val userType: Int,
    @SerializedName("vipType")
    val vipType: Int
)
