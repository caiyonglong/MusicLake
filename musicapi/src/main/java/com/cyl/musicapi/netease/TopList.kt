package com.cyl.musicapi.netease

import com.google.gson.annotations.SerializedName


data class TopList(
        @SerializedName("artistToplist")
        val artistToplist: ArtistToplist,
        @SerializedName("code")
        val code: Int,
        @SerializedName("list")
        val list: MutableList<PlaylistsItem>
)

data class ArtistToplist(
        @SerializedName("coverUrl")
        val coverUrl: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("position")
        val position: Int,
        @SerializedName("upateFrequency")
        val upateFrequency: String,
        @SerializedName("updateFrequency")
        val updateFrequency: String
)


