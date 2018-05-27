package com.cyl.musicapi.playlist


import com.google.gson.annotations.SerializedName

data class PlaylistInfo(@SerializedName("name")
                        val name: String = "",
                        @SerializedName("id")
                        val id: String = "")


