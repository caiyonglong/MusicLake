package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class BaiduArtistInfo(@SerializedName("comment_num")
                           val commentNum: Int = 0,
                           @SerializedName("country")
                           val country: String = "",
                           @SerializedName("piao_id")
                           val piaoId: String = "",
                           @SerializedName("gender")
                           val gender: String = "",
                           @SerializedName("albums_total")
                           val albumsTotal: String = "",
                           @SerializedName("collect_num")
                           val collectNum: Int = 0,
                           @SerializedName("source")
                           val source: String = "",
                           @SerializedName("hot")
                           val hot: String = "",
                           @SerializedName("avatar_s180")
                           val avatarS180: String = "",
                           @SerializedName("is_collect")
                           val isCollect: Int = 0,
                           @SerializedName("bloodtype")
                           val bloodtype: String = "",
                           @SerializedName("constellation")
                           val constellation: String = "",
                           @SerializedName("listen_num")
                           val listenNum: String = "",
                           @SerializedName("avatar_mini")
                           val avatarMini: String = "",
                           @SerializedName("intro")
                           val intro: String = "",
                           @SerializedName("nickname")
                           val nickname: String = "",
                           @SerializedName("company")
                           val company: String = "",
                           @SerializedName("mv_total")
                           val mvTotal: Int = 0,
                           @SerializedName("avatar_s1000")
                           val avatarS1000: String = "",
                           @SerializedName("info")
                           val info: String = "",
                           @SerializedName("share_num")
                           val shareNum: Int = 0,
                           @SerializedName("area")
                           val area: String = "",
                           @SerializedName("avatar_s500")
                           val avatarS: String = "",
                           @SerializedName("avatar_big")
                           val avatarBig: String = "",
                           @SerializedName("weight")
                           val weight: String = "",
                           @SerializedName("birth")
                           val birth: String = "",
                           @SerializedName("avatar_middle")
                           val avatarMiddle: String = "",
                           @SerializedName("avatar_small")
                           val avatarSmall: String = "",
                           @SerializedName("artist_id")
                           val artistId: String = "",
                           @SerializedName("url")
                           val url: String = "",
                           @SerializedName("firstchar")
                           val firstchar: String = "",
                           @SerializedName("aliasname")
                           val aliasname: String = "",
                           @SerializedName("songs_total")
                           val songsTotal: String = "",
                           @SerializedName("stature")
                           val stature: String = "",
                           @SerializedName("name")
                           val name: String = "",
                           @SerializedName("ting_uid")
                           val tingUid: String = "")

data class ArtistAlbumList(
    @SerializedName("albumlist")
    val albumlist: List<Albumlist>,
    @SerializedName("albumnums")
    val albumnums: String,
    @SerializedName("havemore")
    val havemore: Int
)

data class Albumlist(
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("all_artist_id")
    val allArtistId: String,
    @SerializedName("all_artist_ting_uid")
    val allArtistTingUid: Any,
    @SerializedName("area")
    val area: Any,
    @SerializedName("artist_id")
    val artistId: String,
    @SerializedName("artist_ting_uid")
    val artistTingUid: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("favorites_num")
    val favoritesNum: Any,
    @SerializedName("gender")
    val gender: Any,
    @SerializedName("hot")
    val hot: String,
    @SerializedName("info")
    val info: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("pic_big")
    val picBig: String,
    @SerializedName("pic_radio")
    val picRadio: String,
    @SerializedName("pic_s180")
    val picS180: String,
    @SerializedName("pic_small")
    val picSmall: String,
    @SerializedName("prodcompany")
    val prodcompany: Any,
    @SerializedName("publishcompany")
    val publishcompany: String,
    @SerializedName("publishtime")
    val publishtime: String,
    @SerializedName("recommend_num")
    val recommendNum: Any,
    @SerializedName("songs_total")
    val songsTotal: String,
    @SerializedName("style_id")
    val styleId: Any,
    @SerializedName("styles")
    val styles: String,
    @SerializedName("title")
    val title: String
)

