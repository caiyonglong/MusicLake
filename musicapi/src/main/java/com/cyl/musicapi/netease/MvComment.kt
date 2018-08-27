package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class User(@SerializedName("locationInfo")
                val locationInfo: Any? = null,
                @SerializedName("avatarUrl")
                val avatarUrl: String = "",
                @SerializedName("authStatus")
                val authStatus: Int = 0,
                @SerializedName("nickname")
                val nickname: String = "",
                @SerializedName("vipType")
                val vipType: Int = 0,
                @SerializedName("expertTags")
                val expertTags: Any? = null,
                @SerializedName("remarkName")
                val remarkName: Any? = null,
                @SerializedName("userType")
                val userType: Int = 0,
                @SerializedName("userId")
                val userId: Int = 0,
                @SerializedName("experts")
                val experts: Any? = null)


data class MvComment(@SerializedName("total")
                     val total: Int = 0,
                     @SerializedName("code")
                     val code: Int = 0,
                     @SerializedName("comments")
                     val comments: MutableList<CommentsItemInfo>?,
                     @SerializedName("hotComments")
                     val hotComments: MutableList<CommentsItemInfo>?,
                     @SerializedName("more")
                     val more: Boolean = false,
                     @SerializedName("userId")
                     val userId: Int = 0,
                     @SerializedName("moreHot")
                     val moreHot: Boolean = false,
                     @SerializedName("isMusician")
                     val isMusician: Boolean = false)


data class CommentsItemInfo(@SerializedName("isRemoveHotComment")
                            val isRemoveHotComment: Boolean = false,
                            @SerializedName("commentId")
                            val commentId: Int = 0,
                            @SerializedName("likedCount")
                            val likedCount: Int = 0,
                            @SerializedName("time")
                            val time: Long = 0,
                            @SerializedName("user")
                            val user: User,
                            @SerializedName("liked")
                            val liked: Boolean = false,
                            @SerializedName("content")
                            val content: String = "",
                            @SerializedName("pendantData")
                            val pendantData: Any? = null)


