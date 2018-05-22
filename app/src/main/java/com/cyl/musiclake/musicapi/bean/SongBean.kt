package com.cyl.musiclake.musicapi.bean


import com.google.gson.annotations.SerializedName

data class SongBean(@SerializedName("data")
                    val data: Data,
                    @SerializedName("status")
                    val status: Boolean = false)


data class UrlData(@SerializedName("url")
                   val url: String = "")


