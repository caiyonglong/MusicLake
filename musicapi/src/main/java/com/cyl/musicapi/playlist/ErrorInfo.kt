package com.cyl.musicapi.playlist

import com.google.gson.annotations.SerializedName

data class ErrorInfo(@SerializedName("msg")
                     val msg: String = "")