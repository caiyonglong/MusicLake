package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class SongCommentData<T>(@SerializedName("data")
                              val data: CommentData<T>?,
                              @SerializedName("status")
                              val status: Boolean = false,
                              @SerializedName("msg")
                              val msg: String = "")

data class User(@SerializedName("avatarUrl")
                val avatarUrl: String = "",
                @SerializedName("nickname")
                val nickname: String = "",
                @SerializedName("remarkName")
                val remarkName: String? = null,
                @SerializedName("userId")
                val userId: String = "")

class SongComment() {
    var userId: String = ""
    var avatarUrl: String = ""
    var nick: String = ""
    var commentId: String = ""
    var content: String = ""
    var time: Long = 0
    var type: String = ""
}


data class QQComment(@SerializedName("avatarurl")
                     val avatarurl: String = "",
                     @SerializedName("nick")
                     val nick: String = "",
                     @SerializedName("commentid")
                     val commentid: String = "",
                     @SerializedName("rootcommentcontent")
                     val rootcommentcontent: String = "",
                     @SerializedName("rootcommentid")
                     val rootcommentid: String = "",
                     @SerializedName("middlecommentcontent")
                     val middlecommentcontent: Any? = null,
                     @SerializedName("rootcommentnick")
                     val rootcommentnick: String = "",
                     @SerializedName("time")
                     val time: Long = 0,
                     @SerializedName("uin")
                     val uin: String = "")


data class XiamiComment(@SerializedName("author")
                        val author: Boolean = false,
                        @SerializedName("isDelete")
                        val isDelete: Boolean = false,
                        @SerializedName("nickName")
                        val nickName: String = "",
                        @SerializedName("isReport")
                        val isReport: Boolean = false,
                        @SerializedName("isLiked")
                        val isLiked: Boolean = false,
                        @SerializedName("topFlag")
                        val topFlag: Int = 0,
                        @SerializedName("avatar")
                        val avatar: String = "",
                        @SerializedName("gmtCreate")
                        val gmtCreate: Long = 0,
                        @SerializedName("message")
                        val message: String = "",
                        @SerializedName("userId")
                        val userId: Int = 0,
                        @SerializedName("isOfficial")
                        val isOfficial: Int = 0,
                        @SerializedName("objectType")
                        val objectType: Int = 0,
                        @SerializedName("visits")
                        val visits: Int = 0,
                        @SerializedName("commentId")
                        val commentId: Int = 0,
                        @SerializedName("isHot")
                        val isHot: Boolean = false,
                        @SerializedName("objectId")
                        val objectId: Int = 0,
                        @SerializedName("likes")
                        val likes: Int = 0)


data class NeteaseComment(@SerializedName("beReplied")
                          val beReplied: List<BeRepliedItem>?,
                          @SerializedName("commentId")
                          val commentId: Int = 0,
                          @SerializedName("time")
                          val time: Long = 0,
                          @SerializedName("user")
                          val user: User,
                          @SerializedName("content")
                          val content: String = "")


data class CommentData<T>(@SerializedName("total")
                          val total: Int = 0,
                          @SerializedName("comments")
                          val comments: List<T>?)


data class BeRepliedItem(@SerializedName("user")
                         val user: User,
                         @SerializedName("content")
                         val content: String = "",
                         @SerializedName("status")
                         val status: Int = 0)


