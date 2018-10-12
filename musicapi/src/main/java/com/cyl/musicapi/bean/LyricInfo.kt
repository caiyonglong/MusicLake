package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class LyricData(@SerializedName("data")
                     val data: LyricInfo,
                     @SerializedName("status")
                     val status: Boolean = false,
                     @SerializedName("msg")
                     val msg: String = "")

data class LyricInfo(@SerializedName("translate")
                     val translate: List<List<String>>,
                     @SerializedName("lyric")
                     val lyric: List<List<String>>)

