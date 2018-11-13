package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class AlbumListItem(@SerializedName("resource_type_ext")
                         val resourceTypeExt: String = "",
                         @SerializedName("author")
                         val author: String = "",
                         @SerializedName("all_artist_id")
                         val allArtistId: String = "",
                         @SerializedName("publishtime")
                         val publishtime: String = "",
                         @SerializedName("album_desc")
                         val albumDesc: String = "",
                         @SerializedName("company")
                         val company: String = "",
                         @SerializedName("album_id")
                         val albumId: String = "",
                         @SerializedName("pic_small")
                         val picSmall: String = "",
                         @SerializedName("title")
                         val title: String = "",
                         @SerializedName("hot")
                         val hot: Int = 0,
                         @SerializedName("artist_id")
                         val artistId: String = "")


data class ArtistListItem(@SerializedName("country")
                          val country: String = "",
                          @SerializedName("song_num")
                          val songNum: Int = 0,
                          @SerializedName("album_num")
                          val albumNum: Int = 0,
                          @SerializedName("author")
                          val author: String = "",
                          @SerializedName("avatar_middle")
                          val avatarMiddle: String = "",
                          @SerializedName("ting_uid")
                          val tingUid: String = "",
                          @SerializedName("artist_desc")
                          val artistDesc: String = "",
                          @SerializedName("artist_source")
                          val artistSource: String = "",
                          @SerializedName("artist_id")
                          val artistId: String = "")


data class VideoInfoData(@SerializedName("total")
                     val total: Int = 0)


data class UserInfo(@SerializedName("total")
                    val total: Int = 0)


data class ArtistInfo(@SerializedName("total")
                      val total: Int = 0,
                      @SerializedName("artist_list")
                      val artistList: List<ArtistListItem>?)


data class BaiduSearchMergeInfo(@SerializedName("result")
                           val result: Result,
                           @SerializedName("error_code")
                           val errorCode: Int = 0)


data class TopicInfo(@SerializedName("total")
                     val total: Int = 0)


data class Result(@SerializedName("artist_info")
                  val artistInfo: ArtistInfo,
                  @SerializedName("syn_words")
                  val synWords: String = "",
                  @SerializedName("user_info")
                  val userInfo: UserInfo,
                  @SerializedName("album_info")
                  val albumInfo: AlbumInfo,
                  @SerializedName("song_info")
                  val songInfo: SongInfoRes,
                  @SerializedName("tag_info")
                  val tagInfo: TagInfo,
                  @SerializedName("query")
                  val query: String = "",
                  @SerializedName("playlist_info")
                  val playlistInfo: PlaylistInfo,
                  @SerializedName("rqt_type")
                  val rqtType: Int = 0,
                  @SerializedName("video_info")
                  val videoInfo: VideoInfoData,
                  @SerializedName("topic_info")
                  val topicInfo: TopicInfo)


data class AlbumInfo(@SerializedName("album_list")
                     val albumList: List<AlbumListItem>?,
                     @SerializedName("total")
                     val total: Int = 0)


data class TagInfo(@SerializedName("total")
                   val total: Int = 0)


data class SongInfoRes(@SerializedName("total")
                    val total: Int = 0,
                    @SerializedName("song_list")
                    val songList: List<SongListItem>?)


data class PlaylistInfo(@SerializedName("total")
                        val total: Int = 0)


