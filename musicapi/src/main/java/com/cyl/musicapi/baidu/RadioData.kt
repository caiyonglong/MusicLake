package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class RadioData(@SerializedName("result")
                     val result: List<ResultItem>?,
                     @SerializedName("error_code")
                     val errorCode: Int = 0)


data class ResultItem(@SerializedName("channellist")
                      val channellist: MutableList<RadioChannel>?,
                      @SerializedName("title")
                      val title: String = "",
                      @SerializedName("cid")
                      val cid: Int = 0)


data class RadioChannel(@SerializedName("thumb")
                        val thumb: String = "",
                        @SerializedName("name")
                        val name: String = "",
                        @SerializedName("cate_name")
                        val cateName: String = "",
                        @SerializedName("cate_sname")
                        val cateSname: String = "",
                        @SerializedName("ch_name")
                        val chName: String = "",
                        @SerializedName("value")
                        val value: Int = 0,
                        @SerializedName("channelid")
                        val channelid: String = "")


data class RadioChannelData(@SerializedName("result")
                            val result: ChannelInfo,
                            @SerializedName("error_code")
                            val errorCode: Int = 0)



data class CHSongInfo(@SerializedName("all_rate")
                      val allRate: String = "",
                      @SerializedName("charge")
                      val charge: Int = 0,
                      @SerializedName("method")
                      val method: Int = 0,
                      @SerializedName("artist")
                      val artist: String = "",
                      @SerializedName("thumb")
                      val thumb: String = "",
                      @SerializedName("all_artist_id")
                      val allArtistId: String = "",
                      @SerializedName("resource_type")
                      val resourceType: String = "",
                      @SerializedName("havehigh")
                      val havehigh: Int = 0,
                      @SerializedName("title")
                      val title: String = "",
                      @SerializedName("songid")
                      val songid: String?,
                      @SerializedName("artist_id")
                      val artistId: String = "",
                      @SerializedName("flow")
                      val flow: Int = 0)


data class ChannelInfo(@SerializedName("channel")
                       val channel: String = "",
                       @SerializedName("count")
                       val count: Any? = null,
                       @SerializedName("ch_name")
                       val chName: String = "",
                       @SerializedName("artistid")
                       val artistid: Any? = null,
                       @SerializedName("avatar")
                       val avatar: Any? = null,
                       @SerializedName("songlist")
                       val songlist: MutableList<CHSongInfo>?,
                       @SerializedName("channelid")
                       val channelid: Any? = null)
