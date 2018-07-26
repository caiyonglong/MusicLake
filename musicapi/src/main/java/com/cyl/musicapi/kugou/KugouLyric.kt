package com.cyl.musicapi.kugou

import com.google.gson.annotations.SerializedName

data class KugouLyric(@SerializedName("ugc")
                      val ugc: Int = 0,
                      @SerializedName("proposal")
                      val proposal: String = "",
                      @SerializedName("candidates")
                      val candidates: MutableList<Candidates>?,
                      @SerializedName("ugccount")
                      val ugccount: Int = 0,
                      @SerializedName("keyword")
                      val keyword: String = "",
                      @SerializedName("info")
                      val info: String = "",
                      @SerializedName("status")
                      val status: Int = 0)

data class KugouLyricInfo(@SerializedName("charset")
                          val charset: String = "",
                          @SerializedName("fmt")
                          val fmt: String = "",
                          @SerializedName("content")
                          val content: String = "",
                          @SerializedName("info")
                          val info: String = "",
                          @SerializedName("status")
                          val status: Int = 0)

data class Candidates(@SerializedName("song")
                      val song: String = "",
                      @SerializedName("hitlayer")
                      val hitlayer: Int = 0,
                      @SerializedName("singer")
                      val singer: String = "",
                      @SerializedName("language")
                      val language: String = "",
                      @SerializedName("originame")
                      val originame: String = "",
                      @SerializedName("duration")
                      val duration: Int = 0,
                      @SerializedName("transuid")
                      val transuid: String = "",
                      @SerializedName("score")
                      val score: Int = 0,
                      @SerializedName("uid")
                      val uid: String = "",
                      @SerializedName("transname")
                      val transname: String = "",
                      @SerializedName("accesskey")
                      val accesskey: String = "",
                      @SerializedName("adjust")
                      val adjust: Int = 0,
                      @SerializedName("nickname")
                      val nickname: String = "",
                      @SerializedName("soundname")
                      val soundname: String = "",
                      @SerializedName("krctype")
                      val krctype: Int = 0,
                      @SerializedName("origiuid")
                      val origiuid: String = "",
                      @SerializedName("id")
                      val id: String = "",
                      @SerializedName("sounduid")
                      val sounduid: String = "")


