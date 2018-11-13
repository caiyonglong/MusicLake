package com.cyl.musicapi.baidu


import com.google.gson.annotations.SerializedName

data class Suggestion(@SerializedName("suggestion_list")
                      val suggestionList: List<String>?,
                      @SerializedName("query")
                      val query: String = "")


data class BaiduLyric(
        @SerializedName("lrcContent")
        val lrcContent: String,
        @SerializedName("title")
        val title: String
)
