package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class MvInfo(@SerializedName("code")
                  val code: Int = 0,
                  @SerializedName("data")
                  val data: MutableList<MvInfoDetail>?,
                  @SerializedName("hasMore")
                  val hasMore: Boolean = false,
                  @SerializedName("updateTime")
                  val updateTime: Long = 0)


data class MvInfoDetail(@SerializedName("lastRank")
                        val lastRank: Int = 0,
                        @SerializedName("artistId")
                        val artistId: Int = 0,
                        @SerializedName("cover")
                        val cover: String = "",
                        @SerializedName("duration")
                        val duration: Int = 0,
                        @SerializedName("playCount")
                        val playCount: Int = 0,
                        @SerializedName("score")
                        val score: Int = 0,
                        @SerializedName("subed")
                        val subed: Boolean = false,
                        @SerializedName("briefDesc")
                        val briefDesc: String = "",
                        @SerializedName("artists")
                        val artists: MutableList<ArtistsItem>?,
                        @SerializedName("name")
                        val name: String = "",
                        @SerializedName("artistName")
                        val artistName: String = "",
                        @SerializedName("id")
                        val id: Int = 0,
                        @SerializedName("mark")
                        val mark: Int = 0,
                        @SerializedName("desc")
                        val desc: String = "")

//
//data class ArtistsItem(@SerializedName("name")
//                       val name: String = "",
//                       @SerializedName("id")
//                       val id: Int = 0)


data class MvDetailInfo(@SerializedName("code")
                        val code: Int = 0,
                        @SerializedName("data")
                        val data: MvInfoDetailInfo)


data class SimilarMvInfo(@SerializedName("code")
                         val code: Int = 0,
                         @SerializedName("mvs")
                         val data: MutableList<MvInfoDetail>?)


data class MvUrlInfo(@SerializedName("240")
                     val p240: String?,
                     @SerializedName("480")
                     val p480: String?,
                     @SerializedName("720")
                     val p720: String?,
                     @SerializedName("1080")
                     val p1080: String?)

data class MvInfoDetailInfo(@SerializedName("publishTime")
                            val publishTime: String = "",
                            @SerializedName("brs")
                            val brs: MvUrlInfo,
                            @SerializedName("isReward")
                            val isReward: Boolean = false,
                            @SerializedName("commentThreadId")
                            val commentThreadId: String = "",
                            @SerializedName("artistId")
                            val artistId: Int = 0,
                            @SerializedName("likeCount")
                            val likeCount: Int = 0,
                            @SerializedName("commentCount")
                            val commentCount: Int = 0,
                            @SerializedName("cover")
                            val cover: String = "",
                            @SerializedName("subCount")
                            val subCount: Int = 0,
                            @SerializedName("duration")
                            val duration: Int = 0,
                            @SerializedName("playCount")
                            val playCount: Int = 0,
                            @SerializedName("shareCount")
                            val shareCount: Int = 0,
                            @SerializedName("coverId")
                            val coverId: Long = 0,
                            @SerializedName("briefDesc")
                            val briefDesc: String = "",
                            @SerializedName("artists")
                            val artists: MutableList<ArtistsItem>?,
                            @SerializedName("name")
                            val name: String = "",
                            @SerializedName("artistName")
                            val artistName: String = "",
                            @SerializedName("id")
                            val id: Int = 0,
                            @SerializedName("nType")
                            val nType: Int = 0,
                            @SerializedName("desc")
                            val desc: String = "")

