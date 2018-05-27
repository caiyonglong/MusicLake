package com.cyl.musiclake.api.baidu

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
    private const val Base_Url = "http://musicapi.qianqian.com/"
    private val apiService: BaiduApiService
        get() = ApiManager.getInstance().create(BaiduApiService::class.java, Base_Url)

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
            for (item in it.content) {
                val playlist = Playlist()
                playlist.name = item.getName()
                playlist.id = item.getType()
                playlist.coverUrl = item.getPic_s192() ?: item.getPic_s260() ?: item.getPic_s210() ?: item.getPic_s192()
                val musicList = mutableListOf<Music>()
                for (itemMusic in item.content) {
                    val music = Music()
                    music.title = itemMusic.getTitle()
                    music.album = itemMusic.getAlbum_title()
                    music.artist = itemMusic.getAuthor()
                    music.albumId = itemMusic.getAlbum_id()
                    music.id = itemMusic.getSong_id()
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

    fun getOnlineSongs(type: String, limit: Int, mOffset: Int): Observable<List<Music>> {
        val params = HashMap<String, String>()

        params[Constants.PARAM_METHOD] = Constants.METHOD_GET_MUSIC_LIST
        params[Constants.PARAM_TYPE] = type
        params[Constants.PARAM_SIZE] = limit.toString()
        params[Constants.PARAM_OFFSET] = mOffset.toString()

        return apiService.getOnlineSongs(params)
                .flatMap { baiduSongList ->
                    val musicList = ArrayList<Music>()
                    for (songInfo in baiduSongList.song_list) {
                        val music = Music()
                        music.type = Music.Type.BAIDU
                        music.isOnline = true
                        music.id = songInfo.song_id
                        music.album = songInfo.album_title
                        music.albumId = songInfo.album_id.toString()
                        music.artist = songInfo.artist_name
                        music.artistId = songInfo.ting_uid
                        music.title = songInfo.title
                        music.lrcPath = songInfo.lrclink
                        music.coverSmall = songInfo.pic_small
                        music.coverUri = songInfo.pic_big
                        music.coverBig = songInfo.pic_radio
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

    //{"errorCode":22232,"data":{"xcode":"","songList":""}}
    fun getTingSongInfo(mid: String): Observable<Music> {
        val params = HashMap<String, String>()
        val Url = "http://music.baidu.com/data/music/links?songIds=$mid"
        return apiService.getTingSongInfo(Url, params)
                .flatMap { baiduSongInfo ->
                    val music = Music()
                    val songInfo = baiduSongInfo.data.songList[0]
                    music.type = Music.Type.BAIDU
                    music.isOnline = true
                    music.id = songInfo.songId
                    music.album = songInfo.albumName
                    music.albumId = songInfo.albumId.toString()
                    music.artistId = songInfo.artistId
                    music.artist = songInfo.artistName
                    music.title = songInfo.songName
                    music.uri = songInfo.songLink
                    music.fileSize = songInfo.size
                    music.lrcPath = songInfo.lrcLink
                    music.coverSmall = songInfo.songPicSmall
                    music.coverUri = songInfo.songPicBig
                    music.coverBig = songInfo.songPicRadio

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
