package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class HotSearchInfo(@SerializedName("result")
                         val result: Result,
                         @SerializedName("code")
                         val code: Int = 0)

data class HotsItem(@SerializedName("first")
                    val first: String = "",
                    @SerializedName("second")
                    val second: Int = 0)


data class Result(@SerializedName("hots")
                  val hots: MutableList<HotsItem>?)


