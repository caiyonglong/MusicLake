package com.cyl.musiclake.musicapi.bean


import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class LyricInfo(@SerializedName("data")
                     val data: List<JSONArray>?,
                     @SerializedName("status")
                     val status: Boolean = false)


