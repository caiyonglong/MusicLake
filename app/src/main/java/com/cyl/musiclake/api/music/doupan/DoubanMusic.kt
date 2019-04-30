package com.cyl.musiclake.api.music.doupan

import com.google.gson.annotations.SerializedName

data class TagsItem(@SerializedName("count")
                    val count: Int = 0,
                    @SerializedName("name")
                    val name: String = "")


data class DoubanMusic(@SerializedName("total")
                       val total: Int = 0,
                       @SerializedName("count")
                       val count: Int = 0,
                       @SerializedName("start")
                       val start: Int = 0,
                       @SerializedName("musics")
                       val musics: List<MusicsItem>?)


data class Rating(@SerializedName("average")
                  val average: String = "",
                  @SerializedName("min")
                  val min: Int = 0,
                  @SerializedName("max")
                  val max: Int = 0,
                  @SerializedName("numRaters")
                  val numRaters: Int = 0)


data class Attrs(@SerializedName("singer")
                 val singer: List<String>?,
                 @SerializedName("publisher")
                 val publisher: List<String>?,
                 @SerializedName("media")
                 val media: List<String>?,
                 @SerializedName("title")
                 val title: List<String>?,
                 @SerializedName("discs")
                 val discs: List<String>?,
                 @SerializedName("version")
                 val version: List<String>?,
                 @SerializedName("tracks")
                 val tracks: List<String>?,
                 @SerializedName("pubdate")
                 val pubdate: List<String>?)


data class MusicsItem(@SerializedName("image")
                      val image: String = "",
                      @SerializedName("alt_title")
                      val altTitle: String = "",
                      @SerializedName("author")
                      val author: List<AuthorItem>?,
                      @SerializedName("rating")
                      val rating: Rating,
                      @SerializedName("alt")
                      val alt: String = "",
                      @SerializedName("mobile_link")
                      val mobileLink: String = "",
                      @SerializedName("id")
                      val id: String = "",
                      @SerializedName("title")
                      val title: String = "",
                      @SerializedName("tags")
                      val tags: List<TagsItem>?,
                      @SerializedName("attrs")
                      val attrs: Attrs)


data class AuthorItem(@SerializedName("name")
                      val name: String = "")


