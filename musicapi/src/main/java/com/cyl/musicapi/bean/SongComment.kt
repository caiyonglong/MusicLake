package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class SongComment(@SerializedName("data")
                       val data: CommentData,
                       @SerializedName("status")
                       val status: Boolean = false)


data class User(@SerializedName("locationInfo")
                val locationInfo: String? = null,
                @SerializedName("avatarUrl")
                val avatarUrl: String = "",
                @SerializedName("authStatus")
                val authStatus: Int = 0,
                @SerializedName("nickname")
                val nickname: String = "",
                @SerializedName("vipType")
                val vipType: Int = 0,
                @SerializedName("remarkName")
                val remarkName: String? = null,
                @SerializedName("expertTags")
                val expertTags: String? = null,
                @SerializedName("userType")
                val userType: Int = 0,
                @SerializedName("userId")
                val userId: String = "",
                @SerializedName("experts")
                val experts: String?)


data class CommentsItem(@SerializedName("isRemoveHotComment")
                        val isRemoveHotComment: Boolean = false,
                        @SerializedName("beReplied")
                        val beReplied: List<BeRepliedItem>?,
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
                        val pendantData: String? = null)


data class CommentData(@SerializedName("total")
                val total: Int = 0,
                @SerializedName("comments")
                val comments: List<CommentsItem>?)


data class BeRepliedItem(@SerializedName("user")
                         val user: User,
                         @SerializedName("content")
                         val content: String = "",
                         @SerializedName("status")
                         val status: Int = 0)


