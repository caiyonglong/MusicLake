package com.cyl.musiclake.bean


import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport

class UserInfoBean : LitePalSupport() {
    @SerializedName("nickname")
    var nickname: String = ""
    @SerializedName("avatar")
    var avatar: String = ""
    @SerializedName("platform")
    var platform: String = "android"
    @SerializedName("id")
    var id: Int = 0
}