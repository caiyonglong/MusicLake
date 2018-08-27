package com.cyl.musicapi.playlist


import com.google.gson.annotations.SerializedName

data class PlaylistInfo(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("cover")
        val cover: String? = null,
        @SerializedName("playCount")
        val playCount: Long = 0,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("list")
        val list: MutableList<MusicInfo>? = null)


