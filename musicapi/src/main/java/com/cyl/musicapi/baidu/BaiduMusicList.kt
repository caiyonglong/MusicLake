package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class SongListItem(@SerializedName("piao_id")
                        val piaoId: String = "",
                        @SerializedName("resource_type_ext")
                        val resourceTypeExt: String = "",
                        @SerializedName("mv_provider")
                        val mvProvider: String = "",
                        @SerializedName("artist_name")
                        val artistName: String = "",
                        @SerializedName("biaoshi")
                        val biaoshi: String = "",
                        @SerializedName("is_first_publish")
                        val isFirstPublish: Int = 0,
                        @SerializedName("del_status")
                        val delStatus: String = "",
                        @SerializedName("album_1000_1000")
                        val pic1000: String = "",
                        @SerializedName("korean_bb_song")
                        val koreanBbSong: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("pic_big")
                        val picBig: String = "",
                        @SerializedName("pic_huge")
                        val picHuge: String = "",
                        @SerializedName("all_rate")
                        val allRate: String = "",
                        @SerializedName("song_source")
                        val songSource: String = "",
                        @SerializedName("song_id")
                        val songId: String = "",
                        @SerializedName("album_500_500")
                        val album500: String = "",
                        @SerializedName("havehigh")
                        val havehigh: Int = 0,
                        @SerializedName("rank")
                        val rank: String = "",
                        @SerializedName("pic_premium")
                        val picPremium: String = "",
                        @SerializedName("album_800_800")
                        val album800: String = "",
                        @SerializedName("info")
                        val info: String = "",
                        @SerializedName("si_proxycompany")
                        val siProxycompany: String = "",
                        @SerializedName("has_mv_mobile")
                        val hasMvMobile: Int = 0,
                        @SerializedName("charge")
                        val charge: Int = 0,
                        @SerializedName("pic_radio")
                        val picRadio: String = "",
                        @SerializedName("learn")
                        val learn: Int = 0,
                        @SerializedName("author")
                        val author: String = "",
                        @SerializedName("pic_s500")
                        val picS: String = "",
                        @SerializedName("all_artist_id")
                        val allArtistId: String = "",
                        @SerializedName("resource_type")
                        val resourceType: String = "",
                        @SerializedName("has_filmtv")
                        val hasFilmtv: String = "",
                        @SerializedName("pic_small")
                        val picSmall: String = "",
                        @SerializedName("has_mv")
                        val hasMv: Int = 0,
                        @SerializedName("bitrate_fee")
                        val bitrateFee: String = "",
                        @SerializedName("all_artist_ting_uid")
                        val allArtistTingUid: String = "",
                        @SerializedName("artist_id")
                        val artistId: String = "",
                        @SerializedName("high_rate")
                        val highRate: String = "",
                        @SerializedName("versions")
                        val versions: String = "",
                        @SerializedName("album_id")
                        val albumId: String = "",
                        @SerializedName("copy_type")
                        val copyType: String = "",
                        @SerializedName("ting_uid")
                        val tingUid: String = "",
                        @SerializedName("album_title")
                        val albumTitle: String = "")


data class Billboard(@SerializedName("pic_s210")
                     val picS: String = "",
                     @SerializedName("billboard_type")
                     val billboardType: Int = 0,
                     @SerializedName("web_url")
                     val webUrl: String = "",
                     @SerializedName("pic_s640")
                     val picS640: String = "",
                     @SerializedName("pic_s444")
                     val picS444: String = "",
                     @SerializedName("havemore")
                     val havemore: Int = 0,
                     @SerializedName("name")
                     val name: String = "",
                     @SerializedName("billboard_no")
                     val billboardNo: String = "",
                     @SerializedName("comment")
                     val comment: String = "",
                     @SerializedName("pic_s192")
                     val picS192: String = "",
                     @SerializedName("update_date")
                     val updateDate: String = "",
                     @SerializedName("pic_s260")
                     val picS260: String = "")


data class BaiduMusicList(@SerializedName("song_list")
                          val songList: List<SongListItem>?,
                          @SerializedName("error_code")
                          val errorCode: Int = 0,
                          @SerializedName("billboard")
                          val billboard: Billboard)


data class ArtistMusicList(@SerializedName("songlist")
                           val songList: List<SongListItem>?,
                           @SerializedName("error_code")
                           val errorCode: Int = 0,
                           @SerializedName("havemore")
                           val haveMore: Int = 0,
                           @SerializedName("songnums")
                           val songNums: Int = 0)


data class AlbumSongList(@SerializedName("is_collect")
                         val isCollect: Int = 0,
                         @SerializedName("share_pic")
                         val sharePic: String = "",
                         @SerializedName("albumInfo")
                         val albumInfo: AlbumDetailInfo,
                         @SerializedName("share_title")
                         val shareTitle: String = "",
                         @SerializedName("songlist")
                         val songlist: List<SongListItem>?)


data class AlbumDetailInfo(@SerializedName("comment_num")
                           val commentNum: Int = 0,
                           @SerializedName("country")
                           val country: String = "",
                           @SerializedName("pic_s1000")
                           val picS: String = "",
                           @SerializedName("resource_type_ext")
                           val resourceTypeExt: String = "",
                           @SerializedName("gender")
                           val gender: String = "",
                           @SerializedName("language")
                           val language: String = "",
                           @SerializedName("collect_num")
                           val collectNum: Int = 0,
                           @SerializedName("title")
                           val title: String = "",
                           @SerializedName("hot")
                           val hot: String = "",
                           @SerializedName("pic_big")
                           val picBig: String = "",
                           @SerializedName("listen_num")
                           val listenNum: String = "",
                           @SerializedName("price")
                           val price: String = "",
                           @SerializedName("favorites_num")
                           val favoritesNum: Int = 0,
                           @SerializedName("info")
                           val info: String = "",
                           @SerializedName("share_num")
                           val shareNum: Int = 0,
                           @SerializedName("area")
                           val area: String = "",
                           @SerializedName("ai_presale_flag")
                           val aiPresaleFlag: String = "",
                           @SerializedName("pic_radio")
                           val picRadio: String = "",
                           @SerializedName("my_num")
                           val myNum: Int = 0,
                           @SerializedName("author")
                           val author: String = "",
                           @SerializedName("pic_s500")
                           val picS5: String = "",
                           @SerializedName("all_artist_id")
                           val allArtistId: String = "",
                           @SerializedName("buy_url")
                           val buyUrl: String = "",
                           @SerializedName("pic_small")
                           val picSmall: String = "",
                           @SerializedName("publishcompany")
                           val publishcompany: String = "",
                           @SerializedName("all_artist_ting_uid")
                           val allArtistTingUid: String = "",
                           @SerializedName("artist_id")
                           val artistId: String = "",
                           @SerializedName("song_sale")
                           val songSale: Int = 0,
                           @SerializedName("songs_total")
                           val songsTotal: String = "",
                           @SerializedName("publishtime")
                           val publishtime: String = "",
                           @SerializedName("recommend_num")
                           val recommendNum: Int = 0,
                           @SerializedName("artist_ting_uid")
                           val artistTingUid: String = "",
                           @SerializedName("album_id")
                           val albumId: String = "")

