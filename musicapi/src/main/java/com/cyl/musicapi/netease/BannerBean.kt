package com.cyl.musicapi.netease

import com.google.gson.annotations.SerializedName

class BannerResult(@SerializedName("banners")
                 val banners: MutableList<BannerBean>,
                 @SerializedName("code")
                 val code: Int = 0)

//"picUrl": "http://p1.music.126.net/iYHFy0iwiiOHbNuMHYJBIQ==/109951163594094421.jpg",
//"url": "/album?id=73471010",
//"targetId": "73471010",
//"backgroundUrl": "http://p1.music.126.net/hT8TZVuVtumtTZ2JcqKgLg==/109951163594097306.jpg",
//"targetType": "10",
//"monitorType": "",
//"monitorImpress": "",
//"monitorClick": ""
class BannerBean(@SerializedName("imageUrl")
                   val picUrl: String,
                   @SerializedName("url")
                   val url: String,
                   @SerializedName("targetId")
                   val targetId: String,
                   @SerializedName("backgroundUrl")
                   val backgroundUrl: String,
                   @SerializedName("targetType")
                   val targetType: String,
                   @SerializedName("typeTitle")
                   val typeTitle: String,
                   @SerializedName("monitorType")
                   val monitorType: String,
                   @SerializedName("monitorImpress")
                   val monitorImpress: String,
                   @SerializedName("monitorClick")
                   val monitorClick: String)