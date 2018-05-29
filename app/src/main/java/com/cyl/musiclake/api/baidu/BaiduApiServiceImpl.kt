package com.cyl.musiclake.api.baidu

import com.cyl.musicapi.baidu.BaiduApiService
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.LogUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.*

/**
 * Created by yonglong on 2018/1/21.
 */

object BaiduApiServiceImpl {
    private val TAG = "BaiduApiServiceImpl"
    private val apiService: BaiduApiService
        get() = ApiManager.getInstance().create(BaiduApiService::class.java, Constants.BASE_URL_BAIDU_MUSIC)

    //    http://musicapi.qianqian.com/v1/restserver/ting?from=android&version=6.0.7.1&channel=huwei&operator=1&method=baidu.ting.billboard.billCategory&format=json&kflag=2

    //获取榜单
    fun getOnlinePlaylist(): Observable<List<Playlist>> {
        val params = HashMap<String, String>()
        params[Constants.PARAM_METHOD] = Constants.METHOD_CATEGORY
        params["operator"] = "1"
        params["kflag"] = "2"
        params["format"] = "json"
        return apiService.getOnlinePlaylist(params).flatMap { it ->
            val playlists = mutableListOf<Playlist>()
            for (item in it.content!!) {
                val playlist = Playlist()
                playlist.name = item.name
                playlist.id = item.type.toString()
                playlist.coverUrl = item.picS192
                val musicList = mutableListOf<Music>()
                for (itemMusic in item.content!!) {
                    val music = Music()
                    music.title = itemMusic.title
                    music.album = itemMusic.albumTitle
                    music.artist = itemMusic.author
                    music.albumId = itemMusic.albumId
                    music.id = itemMusic.songId
                    musicList.add(music)
                }
                playlist.musicList = musicList
                playlists.add(playlist)
            }
            Observable.create(ObservableOnSubscribe<List<Playlist>> {
                it.onNext(playlists)
                it.onComplete()
            })
        }
    }

    /**
     * 获取歌单歌曲
     */
    fun getOnlineSongs(type: String, limit: Int, mOffset: Int): Observable<List<Music>> {
        val params = HashMap<String, String>()

        params[Constants.PARAM_METHOD] = Constants.METHOD_GET_MUSIC_LIST
        params[Constants.PARAM_TYPE] = type
        params[Constants.PARAM_SIZE] = limit.toString()
        params[Constants.PARAM_OFFSET] = mOffset.toString()

        return apiService.getOnlineSongs(params)
                .flatMap { baiduSongList ->
                    val musicList = ArrayList<Music>()
                    for (songInfo in baiduSongList.songList!!) {
                        val music = Music()
                        music.type = Music.Type.BAIDU
                        music.isOnline = true
                        music.id = songInfo.songId
                        music.album = songInfo.albumTitle
                        music.albumId = songInfo.albumId
                        music.artist = songInfo.artistName
                        music.artistId = songInfo.tingUid
                        music.title = songInfo.title
                        music.coverSmall = songInfo.picSmall
                        music.coverUri = songInfo.picBig
                        music.coverBig = songInfo.picRadio
                        musicList.add(music)
                    }
                    Observable.create(ObservableOnSubscribe<List<Music>> { e ->
                        try {
                            e.onNext(musicList)
                            e.onComplete()
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 搜索建议
     */
    fun getSearchSuggestion(query: String): Observable<List<String>> {
        val params = HashMap<String, String>()
        params[Constants.PARAM_METHOD] = Constants.METHOD_SEARCH_SUGGESTION
        params[Constants.PARAM_QUERY] = query
        return apiService.getSearchSuggestion(params)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<List<String>> { e ->
                        try {
                            e.onNext(it.suggestionList!!)
                            e.onComplete()
                        } catch (error: Exception) {
                            e.onError(Throwable(error.message))
                        }
                    })
                }
    }

    /**
     * 获取歌单详情
     * "http://music.baidu.com/data/music/links?songIds=$mid"
     */
    fun getTingSongInfo(mid: String): Observable<Music> {
        val url = Constants.URL_GET_SONG_INFO + mid
        return apiService.getTingSongInfo(url)
                .flatMap { data ->
                    val music = Music()
                    val songInfo = data.data.songList?.get(0)
                    songInfo?.let {
                        music.type = Music.Type.BAIDU
                        music.isOnline = true
                        music.id = songInfo.songId.toString()
                        music.album = songInfo.albumName
                        music.albumId = songInfo.albumId.toString()
                        music.artistId = songInfo.artistId
                        music.artist = songInfo.artistName
                        music.title = songInfo.songName
                        music.uri = songInfo.songLink
                        music.fileSize = songInfo.size.toLong()
                        music.lrcPath = songInfo.lrcLink
                        music.coverSmall = songInfo.songPicSmall
                        music.coverUri = songInfo.songPicBig
                        music.coverBig = songInfo.songPicRadio
                    }
                    Observable.create(ObservableOnSubscribe<Music> { e ->
                        if (music.uri != null) {
                            e.onNext(music)
                            e.onComplete()
                        } else {
                            e.onError(Throwable())
                        }
                    })
                }
    }

    /**
     * 获取歌词
     */
    fun getBaiduLyric(music: Music): Observable<String> {
        //本地歌词路径
        val mLyricPath = FileUtils.getLrcDir() + music.title + "-" + music.artist + ".lrc"
        //网络歌词
        val mLyricUrl = music.lrcPath
        return if (FileUtils.exists(mLyricPath)) {
            Observable.create { emitter ->
                try {
                    val lyric = FileUtils.readFile(mLyricPath)
                    emitter.onNext(lyric)
                    emitter.onComplete()
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
        } else apiService.getBaiduLyric(mLyricUrl)
                .flatMap { baiDuLyricInfo ->
                    val lyric = baiDuLyricInfo.string()
                    //保存文件
                    val save = FileUtils.writeText(mLyricPath, lyric)
                    LogUtil.e("保存网络歌词：$save")
                    Observable.fromArray(lyric)
                }
    }

}
