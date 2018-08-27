package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class All(@SerializedName("imgUrl")
               val imgUrl: String = "",
               @SerializedName("imgId")
               val imgId: Long = 0,
               @SerializedName("resourceCount")
               val resourceCount: Int = 0,
               @SerializedName("name")
               val name: String = "",
               @SerializedName("type")
               val type: Int = 0,
               @SerializedName("category")
               val category: Int = 0,
               @SerializedName("hot")
               val hot: Boolean = false,
               @SerializedName("resourceType")
               val resourceType: Int = 0)

data class Categories(@SerializedName("0")
                      val c0: String = "",
                      @SerializedName("1")
                      val c1: String = "",
                      @SerializedName("2")
                      val c2: String = "",
                      @SerializedName("3")
                      val c3: String = "",
                      @SerializedName("4")
                      val c4: String = "")

data class SubItem(@SerializedName("imgUrl")
                   val imgUrl: String = "",
                   @SerializedName("imgId")
                   val imgId: Int = 0,
                   @SerializedName("resourceCount")
                   val resourceCount: Int = 0,
                   @SerializedName("name")
                   val name: String = "",
                   @SerializedName("type")
                   val type: Int = 0,
                   @SerializedName("category")
                   val category: Int = 0,
                   @SerializedName("hot")
                   val hot: Boolean = false,
                   @SerializedName("resourceType")
                   val resourceType: Int = 0)


data class CatListBean(@SerializedName("all")
                       val all: All,
                       @SerializedName("sub")
                       val sub: MutableList<SubItem>?,
                       @SerializedName("code")
                       val code: Int = 0,
                       @SerializedName("categories")
                       val categories: Categories)


