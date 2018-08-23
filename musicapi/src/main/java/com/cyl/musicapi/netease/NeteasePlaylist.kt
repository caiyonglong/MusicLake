package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class PlaylistsItem(@SerializedName("description")
                         val description: String = "",
                         @SerializedName("privacy")
                         val privacy: Int = 0,
                         @SerializedName("trackNumberUpdateTime")
                         val trackNumberUpdateTime: Long = 0,
                         @SerializedName("subscribed")
                         val subscribed: Any? = null,
                         @SerializedName("shareCount")
                         val shareCount: Int = 0,
                         @SerializedName("trackCount")
                         val trackCount: Int = 0,
                         @SerializedName("adType")
                         val adType: Int = 0,
                         @SerializedName("coverImgId_str")
                         val coverImgIdStr: String = "",
                         @SerializedName("specialType")
                         val specialType: Int = 0,
                         @SerializedName("copywriter")
                         val copywriter: String = "",
                         @SerializedName("id")
                         val id: Long = 0,
                         @SerializedName("tag")
                         val tag: String = "",
                         @SerializedName("totalDuration")
                         val totalDuration: Int = 0,
                         @SerializedName("ordered")
                         val ordered: Boolean = false,
                         @SerializedName("creator")
                         val creator: Creator,
                         @SerializedName("subscribers")
                         val subscribers: MutableList<SubscribersItem>?,
                         @SerializedName("commentThreadId")
                         val commentThreadId: String = "",
                         @SerializedName("highQuality")
                         val highQuality: Boolean = false,
                         @SerializedName("updateTime")
                         val updateTime: Long = 0,
                         @SerializedName("trackUpdateTime")
                         val trackUpdateTime: Long = 0,
                         @SerializedName("userId")
                         val userId: Int = 0,
                         @SerializedName("tracks")
                         val tracks: MutableList<TracksItem>? = null,
                         @SerializedName("tags")
                         val tags: MutableList<String>?,
                         @SerializedName("anonimous")
                         val anonimous: Boolean = false,
                         @SerializedName("commentCount")
                         val commentCount: Int = 0,
                         @SerializedName("cloudTrackCount")
                         val cloudTrackCount: Int = 0,
                         @SerializedName("coverImgUrl")
                         val coverImgUrl: String = "",
                         @SerializedName("playCount")
                         val playCount: Int = 0,
                         @SerializedName("coverImgId")
                         val coverImgId: Long = 0,
                         @SerializedName("createTime")
                         val createTime: Long = 0,
                         @SerializedName("name")
                         val name: String = "",
                         @SerializedName("subscribedCount")
                         val subscribedCount: Int = 0,
                         @SerializedName("status")
                         val status: Int = 0,
                         @SerializedName("newImported")
                         val newImported: Boolean = false)


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
                           val playlists: MutableList<PlaylistsItem>?)

data class NeteasePlaylistDetail(
        @SerializedName("code")
        val code: Int = 0,
        @SerializedName("playlist")
        val playlist: PlaylistsItem?)


data class TracksItem(@SerializedName("id")
                      val id: String?,
                      @SerializedName("name")
                      val name: String? = "",
                      @SerializedName("ar")
                      val artists: MutableList<ArtistsItem>?,
                      @SerializedName("al")
                      val album: AlbumItem,
                      @SerializedName("publishTime")
                      val publishTime: Long = 0,
                      @SerializedName("cp")
                      val cp: Int = 0)

data class AlbumItem(@SerializedName("picUrl")
                     val picUrl: String = "",
                     @SerializedName("name")
                     val name: String = "",
                     @SerializedName("id")
                     val id: Int = 0)

