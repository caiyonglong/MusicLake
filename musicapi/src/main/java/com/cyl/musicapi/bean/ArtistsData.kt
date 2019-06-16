package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class ArtistsData(@SerializedName("data")
                       val data: Artists,
                       @SerializedName("status")
                       val status: Boolean = false,
                       @SerializedName("msg")
                       val msg: String = "")


data class SingerList(@SerializedName("data")
                      val data: Artists,
                      @SerializedName("code")
                      val code: Int = 0)

data class ArtistsDataInfo(@SerializedName("singerList")
                           val singerList: SingerList,
                           @SerializedName("code")
                           val code: Int = 0)

data class Artists(@SerializedName("area")
                   val area: Int,
                   @SerializedName("genre")
                   val genre: Int,
                   @SerializedName("index")
                   val index: Int,
                   @SerializedName("sex")
                   val sex: Int,
                   @SerializedName("singerlist")
                   val singerList: MutableList<SingerItem>,
                   @SerializedName("tags")
                   val tags: SingerTag,
                   @SerializedName("total")
                   val total: Int)

data class SingerItem(@SerializedName("country")
                      val country: String = "",
                      @SerializedName("singer_id")
                      val singer_id: String = "",
                      @SerializedName("singer_mid")
                      val singer_mid: String? = null,
                      @SerializedName("singer_pic")
                      val singer_pic: String? = null,
                      @SerializedName("singer_name")
                      val singer_name: String? = null)

data class SingerTag(@SerializedName("area")
                     val area: MutableList<SingerCate>,
                     @SerializedName("genre")
                     val genre: MutableList<SingerCate>,
                     @SerializedName("index")
                     val index: MutableList<SingerCate>,
                     @SerializedName("sex")
                     val sex: MutableList<SingerCate>)

data class SingerCate(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String)
