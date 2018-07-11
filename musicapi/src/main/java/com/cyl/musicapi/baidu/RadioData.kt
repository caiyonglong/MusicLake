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


