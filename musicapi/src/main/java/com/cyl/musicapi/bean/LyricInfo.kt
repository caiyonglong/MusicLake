package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class LyricInfo(@SerializedName("data")
                     val data:  List<List<String>>,
                     @SerializedName("status")
                     val status: Boolean = false)


