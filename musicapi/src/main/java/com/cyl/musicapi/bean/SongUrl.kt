package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class SongBean(@SerializedName("data")
                    val data: UrlData,
                    @SerializedName("status")
                    val status: Boolean = false,
                    @SerializedName("msg")
                    val msg: String = "")


data class UrlData(@SerializedName("url")
                   val url: String = "")


