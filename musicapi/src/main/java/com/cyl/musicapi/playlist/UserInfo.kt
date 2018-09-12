package com.cyl.musicapi.playlist


import com.google.gson.annotations.SerializedName

data class UserInfo(@SerializedName("nickname")
                    val nickname: String = "",
                    @SerializedName("avatar")
                    val avatar: String = "",
                    @SerializedName("token")
                    val token: String = "",
                    @SerializedName("id")
                    val id: Int = 0)