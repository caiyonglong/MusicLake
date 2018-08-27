package com.cyl.musicapi.bean


import com.google.gson.annotations.SerializedName

data class SongCommentData<T>(@SerializedName("data")
                              val data: CommentData<T>,
                              @SerializedName("status")
                              val status: Boolean = false)


data class User(@SerializedName("avatarUrl")
                val avatarUrl: String = "",
                @SerializedName("nickname")
                val nickname: String = "",
                @SerializedName("remarkName")
                val remarkName: String? = null,
                @SerializedName("userId")
                val userId: String = "")


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
                     val time: Int = 0,
                     @SerializedName("uin")
                     val uin: String = "")


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

class SongComment {
    var avatarUrl: String = ""
    var nickname: String = ""
    var commentId: String = ""
    var content: String = ""
    var type: String = ""
    var time: Int = 0
    var userId: String = ""
}


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


