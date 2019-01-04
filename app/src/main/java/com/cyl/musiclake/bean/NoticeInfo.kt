package com.cyl.musiclake.bean

import com.google.gson.annotations.SerializedName

class NoticeInfo {
    @SerializedName("id")
    var id = 0
    @SerializedName("message")
    var message = ""
    @SerializedName("title")
    var title = ""
    @SerializedName("dismiss")
    var dismiss = true
}