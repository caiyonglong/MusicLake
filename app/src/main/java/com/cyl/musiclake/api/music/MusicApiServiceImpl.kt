package com.cyl.musiclake.api.music


import com.cyl.musicapi.BaseApiImpl
import com.cyl.musicapi.bean.*
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.music.doupan.DoubanApiServiceImpl
import com.cyl.musiclake.api.music.qq.QQMusicApiServiceImpl
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.ui.music.search.SearchEngine
import com.cyl.musiclake.utils.FileUtils
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.Observable.create

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

object MusicApiServiceImpl {
    private val TAG = "MusicApiServiceImpl"

    /**
     * 搜索音乐
     *
     * @param key
     * @param limit
     * @param page
     * @return
     */
    fun searchMusic(key: String, type: SearchEngine.Filter, limit: Int, page: Int): Observable<MutableList<Music>> {
        return create { result ->
            if (type == SearchEngine.Filter.ANY) {
                BaseApiImpl.searchSong(key, limit, page, success = {
                    val musicList = mutableListOf<Music>()
                    if (it.status) {
                        try {
                            val neteaseSize = it.data.netease.songs?.size ?: 0
                            val qqSize = it.data.netease.songs?.size ?: 0
                            val xiamiSize = it.data.netease.songs?.size ?: 0
                            val max = Math.max(Math.max(neteaseSize, qqSize), xiamiSize)
                            for (i in 0 until max) {
                                if (neteaseSize > i) {
                                    it.data.netease.songs?.get(i)?.let { music ->
                                        music.vendor = Constants.NETEASE
                                        musicList.add(MusicUtils.getMusic(music))
                                    }
                                }

                                if (qqSize > i) {
                                    it.data.qq.songs?.get(i)?.let { music ->
                                        music.vendor = Constants.QQ
                                        musicList.add(MusicUtils.getMusic(music))
                                    }
                                }

                                if (xiamiSize > i) {
                                    it.data.xiami.songs?.get(i)?.let { music ->
                                        music.vendor = Constants.XIAMI
                                        musicList.add(MusicUtils.getMusic(music))
                                    }
                                }
                            }
                        } catch (e: Throwable) {
                            LogUtil.e("search", e.message)
                        }
                        LogUtil.e("search", "结果：" + musicList.size)
                        result.onNext(musicList)
                        result.onComplete()
                    } else {
                        LogUtil.e("search", it.msg)
                        result.onError(Throwable(it.msg))
                    }
                }, fail = {
                    result.onError(Throwable(it
                            ?: MusicApp.getAppContext().getString(R.string.error_connection)))
                })
            } else {
                BaseApiImpl.searchSongSingle(key, type.toString(), limit, page, success = {
                    val musicList = mutableListOf<Music>()
                    if (it.status) {
                        try {
                            LogUtil.e("search type", type.toString().toLowerCase())
                            it.data.songs?.forEach { music ->
                                music.vendor = type.toString().toLowerCase()
                                musicList.add(MusicUtils.getMusic(music))
                            }
                        } catch (e: Throwable) {
                            LogUtil.e("search", e.message)
                        }
                        LogUtil.e("search", "结果：" + musicList.size)

                    } else {
                        LogUtil.e("search", it.msg)
//                        result.onError(Throwable(it.msg))
                    }
                    result.onNext(musicList)
                    result.onComplete()
                })
            }
        }
    }


    /**
     * 获取豆瓣背景图片
     */
    fun getAlbumUrl(info: String): Observable<String> {
        return DoubanApiServiceImpl.getDoubanPic(info)
    }


    /**
     * 批量獲取歌曲信息
     *
     */
    fun getBatchMusic(vendor: String, ids: Array<String>): Observable<MutableList<Music>> {
        return create { result ->
            BaseApiImpl.getBatchSongDetail(vendor, ids) {
                val musicList = mutableListOf<Music>()
                if (it.status) {
                    val songList = it.data
                    songList.forEach {
                        it.vendor = vendor
                        musicList.add(MusicUtils.getMusic(it))
                    }
                    result.onNext(musicList)
                    result.onComplete()
                } else {
                    result.onError(Throwable(it.msg))
                }
            }
        }
    }

    /**
     * 获取歌曲url信息
     * @param br 音乐品质
     *
     */
    fun getMusicUrl(vendor: String, mid: String, br: Int = 128000): Observable<String> {
        LogUtil.d("getMusicUrl $vendor $mid $br")
        return create { result ->
            BaseApiImpl.getSongUrl(vendor, mid, br, {
                if (it.status) {
                    val url =
                            if (vendor == Constants.XIAMI) {
                                if (it.data.url.startsWith("http")) it.data.url
                                else "http:${it.data.url}"
                            } else it.data.url
                    result.onNext(url)
                    result.onComplete()
                } else {
                    result.onError(Throwable(it.msg))
                }
            }, {})
        }
    }

    /**
     * 获取歌曲详细信息
     *
     */
    fun getSongDetail(vendor: String, mid: String): Observable<Music> {
        return create { result ->
            BaseApiImpl.getSongDetail(vendor, mid, {
                if (it.status) {
                    val music = it.data
                    music.vendor = vendor
                    result.onNext(MusicUtils.getMusic(music))
                    result.onComplete()
                } else {
                    result.onError(Throwable(it.msg))
                }
            }, {})
        }
    }

    /**
     * 获取歌曲url信息
     *
     */
    fun getMusicComment(vendor: String, mid: String): Observable<MutableList<SongComment>>? {
        return when (vendor) {
            Constants.NETEASE -> create { result ->
                BaseApiImpl.getComment(vendor, mid, {
                    it as SongCommentData<NeteaseComment>
                    if (it.status) {
                        val comments = mutableListOf<SongComment>()
                        it.data?.comments?.forEach {
                            val songComment = SongComment().apply {
                                avatarUrl = it.user.avatarUrl
                                nick = it.user.nickname
                                commentId = it.commentId.toString()
                                time = it.time
                                userId = it.user.userId
                                content = it.content
                                type = Constants.NETEASE
                            }
                            comments.add(songComment)
                        }
                        result.onNext(comments)
                        result.onComplete()
                    } else {
                        result.onError(Throwable(it.msg))
                    }
                }, {
                    ToastUtils.show(it)
                })
            }
            Constants.QQ -> create { result ->
                BaseApiImpl
                        .getComment(vendor, mid, {
                            it as SongCommentData<QQComment>
                            if (it.status) {
                                val comments = mutableListOf<SongComment>()
                                it.data?.comments?.forEach {
                                    val songComment = SongComment().apply {
                                        avatarUrl = it.avatarurl
                                        nick = it.nick
                                        commentId = it.rootcommentid
                                        time = it.time * 1000
                                        userId = it.uin
                                        content = it.rootcommentcontent
                                        type = Constants.QQ
                                    }
                                    comments.add(songComment)
                                }
                                result.onNext(comments)
                                result.onComplete()
                            } else {
                                result.onError(Throwable(it.msg))
                            }
                        }, {
                            ToastUtils.show(it)
                        })
            }
            Constants.XIAMI -> create { result ->
                BaseApiImpl
                        .getComment(vendor, mid, {
                            it as SongCommentData<XiamiComment>
                            if (it.status) {
                                val comments = mutableListOf<SongComment>()
                                it.data?.comments?.forEach {
                                    val songComment = SongComment().apply {
                                        avatarUrl = it.avatar
                                        nick = it.nickName
                                        commentId = it.commentId.toString()
                                        time = it.gmtCreate
                                        userId = it.userId.toString()
                                        content = it.message
                                        type = Constants.XIAMI
                                    }
                                    comments.add(songComment)
                                }
                                result.onNext(comments)
                                result.onComplete()
                            } else {
                                result.onError(Throwable(it.msg))
                            }
                        }, {
                            ToastUtils.show(it)
                        })
            }
            else -> null
        }
    }


    /**
     * 获取歌手单曲
     *
     */
    fun getArtistSongs(vendor: String, id: String, offset: Int = 0, limit: Int = 50): Observable<Artist> {
        return create { result ->
            BaseApiImpl
                    .getArtistSongs(vendor, id, offset, limit, {
                        if (it.status) {
                            LogUtil.d(TAG, it.toString())
                            val musicList = arrayListOf<Music>()
                            it.data.songs.forEach {
                                if (!it.cp) {
                                    it.vendor = vendor
                                    musicList.add(MusicUtils.getMusic(it))
                                }
                            }
                            val artist = Artist()
                            artist.songs = musicList
                            artist.name = it.data.detail.name
                            artist.picUrl = it.data.detail.cover
                            artist.desc = it.data.detail.desc
                            artist.artistId = it.data.detail.id
                            result.onNext(artist)
                            result.onComplete()
                        } else {
                            result.onError(Throwable(it.msg))
                        }
                    }, {})
        }
    }


    /**
     * 获取专辑信息单曲
     * @param vendor 专辑类型
     * @param id 专辑id
     * @return
     */
    fun getAlbumSongs(vendor: String, id: String): Observable<Album> {
        return create { result ->
            BaseApiImpl.getAlbumDetail(vendor, id, {
                if (it.status) {
                    val album = Album()
                    val musicList = arrayListOf<Music>()
                    it.data.songs.forEach {
                        it.vendor = vendor
                        musicList.add(MusicUtils.getMusic(it))
                    }
                    album.info = it.data.desc
                    album.songs = musicList
                    album.name = it.data.name
                    album.albumId = id
                    album.cover = it.data.cover
                    album.type = vendor
                    album.artistId = it.data.artist.id
                    album.artistName = it.data.artist.name
                    result.onNext(album)
                    result.onComplete()
                } else {
                    result.onError(Throwable(it.msg))
                }
            }, {})
        }
    }

    /**
     * 获取专辑单曲
     *
     */
    fun getPlaylistSongs(vendor: String, id: String): Observable<Playlist> {
        return create { result ->
            BaseApiImpl
                    .getAlbumSongs(vendor, id, {
                        if (it.status) {
                            val playlist = Playlist()
                            playlist.type = Constants.PLAYLIST_CUSTOM_ID
                            playlist.name = it.data.detail.name
                            playlist.des = it.data.detail.desc
                            playlist.coverUrl = it.data.detail.cover
                            playlist.pid = it.data.detail.id
                            it.data.songs.forEach {
                                it.vendor = vendor
                                playlist.musicList.add(MusicUtils.getMusic(it))
                            }
                            result.onNext(playlist)
                            result.onComplete()
                        } else {
                            result.onError(Throwable(it.msg))
                        }
                    }, {})
        }
    }


    /**
     * 获取详细信息
     */
    fun getAnyVendorSongDetail(list: MutableList<Music>): Observable<MutableList<Music>> {
        return create { result ->
            val array = mutableListOf<Map<String, String?>>()
            list.forEach {
                val t = mutableMapOf<String, String?>()
                t["vendor"] = it.type
                t["id"] = it.mid
                array.add(t)
            }
            BaseApiImpl.getAnyVendorSongDetail(array, { data ->
                val musicList = mutableListOf<Music>()
                for (i in 0 until data.size) {
                    data[i].vendor = array[i]["vendor"]
                    musicList.add(MusicUtils.getMusic(data[i]))
                }
                result.onNext(musicList)
            }, fail = {
                result.onError(Throwable(it))
            })
        }

    }

    /**
     * 获取歌手列表
     *
     */
    fun getArtists(offset: Int, params: Map<String, Int>): Observable<Artists> {
        return QQMusicApiServiceImpl.getArtists(offset, params)
    }


    /**
     * 获取歌词
     *
     */
    fun getLyricInfo(music: Music): Observable<String>? {
        try {
            val mLyricPath = FileUtils.getLrcDir() + "${music.title}-${music.artist}" + ".lrc"
            val vendor = music.type!!
            val mid = music.mid!!
            //网络歌词
            return if (FileUtils.exists(mLyricPath)) {
                MusicApi.getLocalLyricInfo(mLyricPath)
            } else create { result ->
                BaseApiImpl.getLyricInfo(vendor, mid) {
                    if (it.status) {
                        val lyricInfo = it.data.lyric
                        val lyric = StringBuilder()
                        lyricInfo.forEach {
                            lyric.append(it)
                            lyric.append("\n")
                        }
                        it.data.translate.forEach {
                            lyric.append(it)
                            lyric.append("\n")
                        }
                        //保存文件
                        val save = FileUtils.writeText(mLyricPath, lyric.toString())
                        LogUtil.e("保存网络歌词：$save")
                        Observable.fromArray(lyric)
                        result.onNext(lyric.toString())
                        result.onComplete()
                    } else {
                        result.onError(Throwable(it.msg))
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 获取本地歌词
     */
    fun getLocalLyricInfo(music: Music): Observable<String> {
        val mLyricPath = FileUtils.getLrcDir() + music.title + "-" + music.artist + ".lrc"
        //网络歌词
        return MusicApi.getLocalLyricInfo(mLyricPath)
    }


    /**
     * 保存歌词
     */
    fun saveLyricInfo(name: String, artist: String, lyricInfo: String) {
        val mLyricPath = FileUtils.getLrcDir() + "$name-$artist.lrc"
        val save = FileUtils.writeText(mLyricPath, lyricInfo)
        LogUtil.e("保存网络歌词：$save")
    }

}
