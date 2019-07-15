package com.cyl.musiclake.api.youtube

import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.VideoListResponse
import java.io.IOException

object YoutubeDataApi {
    private val limit: Long = 25
    /**
     * 定义HTTP传输的全局实例。
     */
    private val HTTP_TRANSPORT = NetHttpTransport()

    /**
     * 定义JSON工厂的全局实例。
     */
    val JSON_FACTORY: JsonFactory = JacksonFactory()
    /**
     * 定义一个Youtube对象的全局实例，该对象将被使用。
     * 制作YouTube数据API请求。
     */
    private var youtube: YouTube? = null

    /**
     * 初始化一个YouTube对象，在YouTube上搜索视频。然后
     * 在结果集中显示每个视频的名称和缩略图。
     */
    fun search(queryTerm: String, pageToken: String, hasCaption: Boolean = false): SearchListResponse? {
        try {
            // 获取YouTube数据API请求对象
            youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpRequestInitializer { }).setApplicationName("com.aoe.music.MusicApp").build()
            // Define the API request for retrieving search results.
            val search = youtube!!.search().list("id,snippet")
            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            search.key = Constants.GOOGLE_DEVELOPER_KEY
            search.q = queryTerm
            search.pageToken = pageToken
            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.type = "video"
            if (!hasCaption) {
                search.videoCaption = "any"
            } else {
                search.videoCaption = "closedCaption"
            }
            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.fields = "nextPageToken,items(id,snippet)"
            search.maxResults = limit
            // Call the API and print results.
            return search.execute()
        } catch (e: IOException) {
            System.err.println("There was an IO error: " + e.cause + " : " + e.message)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return null
    }

    private fun getSongInfo(ids: String): VideoListResponse? {
        try {
            // 获取YouTube数据API请求对象
            youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpRequestInitializer { }).setApplicationName("com.aoe.music.MusicApp").build()
            // Define the API request for retrieving search results.
            val search = youtube!!.videos().list("id,snippet")
            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            search.key = Constants.GOOGLE_DEVELOPER_KEY
            search.id = ids
            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.fields = "items(id,snippet)"
            search.maxResults = limit
            // Call the API and print results.
            return search.execute()
        } catch (e: IOException) {
            System.err.println("There was an IO error: " + e.cause + " : " + e.message)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return null
    }

    /**
     * 根据id 获取Youtube歌曲信息
     */
    fun getYoutubeSongInfo(ids: List<String>, result: (resultList: MutableList<Music>) -> Unit) {

        val count = if (ids.size % 50 == 0) {
            ids.size / 50
        } else {
            (ids.size / 50) + 1
        }
        val songlist = mutableListOf<Music>()
        val map = mutableMapOf<Int, List<String>>()
        repeat(count) {
            if (it == count - 1) {
                map[it] = ids.subList(it * 50, ids.size)
            } else {
                map[it] = ids.subList(it * 50, (it + 1) * 50)
            }
        }
        for ((_, idList) in map) {
            val id = idList.toString().replace("[", "").replace("]", "")
            val list = getSongInfo(id)
            list?.let {
                list.items.mapTo(songlist) {
                    val song = Music()
                    song.title = it.snippet.title
                    song.artist = it.snippet.channelTitle
                    song.artistId = it.snippet.channelId
                    song.mid = it.id
                    song.coverUri = it.snippet.thumbnails.high.url
                    song.type = Constants.YOUTUBE
                    song
                }
            }
        }
        result.invoke(songlist)
    }

    /**
     * 获取歌手的歌单
     * 通过 snippet.type (upload/subscription/like)
     * 获取他上传的视频（部分获取不到，猜测版权问题）
     * 获取他的订阅频道（目前发现当获取他上传的视频数量较少的时候可以获取到此数据）
     * 获取相似推荐（当获取的他上传的视频数量为0的时候可以获取到此数据）
     */
    fun getUpListBySinger(singerId: String, pageToken: String?,
                          resultList: (String?, MutableList<Music>) -> Unit) {
        try {
            // 获取YouTube数据API请求对象
            val youtube = YouTube.Builder(NetHttpTransport(), JSON_FACTORY, HttpRequestInitializer { })
                    .setApplicationName("com.aoe.music.MusicApp").build()
            val list = youtube.Activities().list("snippet,contentDetails")
            list.channelId = singerId
            list.pageToken = pageToken
            list.maxResults = 10
            list.key = Constants.GOOGLE_DEVELOPER_KEY
            val result = list.execute()
            val songList = mutableListOf<Music>()
            result.items?.forEach {
                it.contentDetails?.let { detail ->
                    val id: String? = when {
                        detail.upload?.videoId != null -> {
                            detail.upload?.videoId
                        }
                        detail.like?.resourceId?.videoId != null -> {
                            detail.like?.resourceId?.videoId
                        }
                        else -> {
                            null
                        }
                    }
                    val snippet = it.snippet
                    id?.let {
                        val song = Music()
                        song.title = snippet.title
                        song.artist = snippet.channelTitle
                        song.artistId = snippet.channelId
                        song.mid = it
                        song.coverUri = snippet.thumbnails?.standard?.url
                                ?: snippet.thumbnails?.high?.url
                                        ?: snippet.thumbnails?.default?.url
                                        ?: snippet.thumbnails?.standard?.url
                        song.type = Constants.YOUTUBE
                        songList.add(song)
                        println(song)
                    }
                }
            }
            resultList.invoke(result.nextPageToken, songList)
        } catch (t: Throwable) {
            t.printStackTrace()
            resultList.invoke(null, mutableListOf())
        }
    }
}