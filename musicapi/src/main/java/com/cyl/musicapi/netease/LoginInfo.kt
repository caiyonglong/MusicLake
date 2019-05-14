package com.cyl.musicapi.netease


import com.google.gson.annotations.SerializedName

data class Account(@SerializedName("salt")
                   val salt: String = "",
                   @SerializedName("vipType")
                   val vipType: Int = 0,
                   @SerializedName("userName")
                   val userName: String = "",
                   @SerializedName("type")
                   val type: Int = 0,
                   @SerializedName("ban")
                   val ban: Int = 0,
                   @SerializedName("anonimousUser")
                   val anonimousUser: Boolean = false,
                   @SerializedName("createTime")
                   val createTime: Int = 0,
                   @SerializedName("tokenVersion")
                   val tokenVersion: Int = 0,
                   @SerializedName("id")
                   val id: Int = 0,
                   @SerializedName("whitelistAuthority")
                   val whitelistAuthority: Int = 0,
                   @SerializedName("baoyueVersion")
                   val baoyueVersion: Int = 0,
                   @SerializedName("viptypeVersion")
                   val viptypeVersion: Int = 0,
                   @SerializedName("donateVersion")
                   val donateVersion: Int = 0,
                   @SerializedName("status")
                   val status: Int = 0)


data class LoginInfo(@SerializedName("clientId")
                     val clientId: String = "",
                     @SerializedName("code")
                     val code: Int = 0,
                     @SerializedName("msg")
                     val msg: String = "",
                     @SerializedName("loginType")
                     val loginType: Int = 0,
                     @SerializedName("profile")
                     val profile: Profile,
                     @SerializedName("bindings")
                     val bindings: MutableList<BindingsItem>?,
                     @SerializedName("effectTime")
                     val effectTime: Int = 0,
                     @SerializedName("account")
                     val account: Account)


data class BindingsItem(@SerializedName("expiresIn")
                        val expiresIn: Int = 0,
                        @SerializedName("expired")
                        val expired: Boolean = false,
                        @SerializedName("tokenJsonStr")
                        val tokenJsonStr: String = "",
                        @SerializedName("refreshTime")
                        val refreshTime: Int = 0,
                        @SerializedName("id")
                        val id: Long = 0,
                        @SerializedName("type")
                        val type: Int = 0,
                        @SerializedName("userId")
                        val userId: Int = 0,
                        @SerializedName("url")
                        val url: String = "")


data class Profile(@SerializedName("detailDescription")
                   val detailDescription: String = "",
                   @SerializedName("birthday")
                   val birthday: Long = 0,
                   @SerializedName("backgroundUrl")
                   val backgroundUrl: String = "",
                   @SerializedName("gender")
                   val gender: Int = 0,
                   @SerializedName("city")
                   val city: Int = 0,
                   @SerializedName("signature")
                   val signature: String = "",
                   @SerializedName("description")
                   val description: String = "",
                   @SerializedName("remarkName")
                   val remarkName: String? = null,
                   @SerializedName("accountStatus")
                   val accountStatus: Int = 0,
                   @SerializedName("avatarImgId")
                   val avatarImgId: Long = 0,
                   @SerializedName("defaultAvatar")
                   val defaultAvatar: Boolean = false,
                   @SerializedName("avatarImgIdStr")
                   val avatarImgIdStr: String = "",
                   @SerializedName("backgroundImgIdStr")
                   val backgroundImgIdStr: String = "",
                   @SerializedName("province")
                   val province: Int = 0,
                   @SerializedName("nickname")
                   val nickname: String = "",
                   @SerializedName("djStatus")
                   val djStatus: Int = 0,
                   @SerializedName("avatarUrl")
                   val avatarUrl: String = "",
                   @SerializedName("authStatus")
                   val authStatus: Int = 0,
                   @SerializedName("vipType")
                   val vipType: Int = 0,
                   @SerializedName("followed")
                   val followed: Boolean = false,
                   @SerializedName("userId")
                   val userId: Int = 0,
                   @SerializedName("mutual")
                   val mutual: Boolean = false,
                   @SerializedName("authority")
                   val authority: Int = 0,
                   @SerializedName("backgroundImgId")
                   val backgroundImgId: Long = 0,
                   @SerializedName("userType")
                   val userType: Int = 0)


