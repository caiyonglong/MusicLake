package com.cyl.musiclake.api.music.netease

import com.cyl.musicapi.netease.*
import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.api.net.ApiManager
import com.cyl.musiclake.bean.*
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by D22434 on 2018/1/5.
 */

object NeteaseApiServiceImpl {
    private val TAG = "NeteaseApiServiceImpl"

    val apiService by lazy { ApiManager.getInstance().create(NeteaseApiService::class.java, SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_API_URL, Constants.BASE_NETEASE_URL)) }

    /**
     * 获取歌单歌曲
     */
    fun getTopArtists(limit: Int, offset: Int): Observable<MutableList<Artist>> {
        return apiService.getTopArtists(offset, limit)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<Artist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Artist>()
                                it.list.artists?.forEach {
                                    val playlist = Artist()
                                    playlist.artistId = it.id.toString()
                                    playlist.name = it.name
                                    playlist.picUrl = it.picUrl
                                    playlist.score = it.score
                                    playlist.musicSize = it.musicSize
                                    playlist.albumSize = it.albumSize
                                    playlist.type = Constants.NETEASE
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取歌单数据
     */
    fun getTopPlaylists(cat: String, limit: Int): Observable<MutableList<Playlist>> {
        return apiService.getTopPlaylist(cat, limit)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list    = mutableListOf<Playlist>()
                                it.playlists?.forEach {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.coverUrl = it.coverImgUrl
                                    playlist.des = it.description
                                    playlist.date = it.createTime
                                    playlist.updateDate = it.updateTime
                                    playlist.playCount = it.playCount.toLong()
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取歌单歌曲数据
     */
    fun getTopPlaylistsHigh(tag: String, limit: Int, before: Long?): Observable<MutableList<Playlist>> {
        val map = mutableMapOf<String, Any>()
        map["cat"] = tag
        map["limit"] = limit
        before?.let {
            map["before"] = it
        }
        return apiService.getTopPlaylistHigh(map)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Playlist>()
                                it.playlists?.forEach {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.coverUrl = it.coverImgUrl
                                    playlist.des = it.description
                                    playlist.date = it.createTime
                                    playlist.updateDate = it.updateTime
                                    playlist.playCount = it.playCount.toLong()
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取精品歌单歌曲数据
     */
    fun getPlaylistDetail(id: String): Observable<Playlist> {
        return apiService.getPlaylistDetail(id)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<Playlist> { e ->
                        try {
                            if (it.code == 200) {
                                it.playlist?.let {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.coverUrl = it.coverImgUrl
                                    playlist.des = it.description
                                    playlist.date = it.createTime
                                    playlist.updateDate = it.updateTime
                                    playlist.playCount = it.playCount.toLong()
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    playlist.musicList = MusicUtils.getNeteaseMusicList(it.tracks)
                                    e.onNext(playlist)
                                }
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取最新mv
     */
    fun getNewestMv(limit: Int): Observable<MvInfo> {
        return apiService.getNewestMv(limit)
    }

    /**
     * 获取排行榜mv
     */
    fun getTopMv(limit: Int, offset: Int): Observable<MvInfo> {
        return apiService.getTopMv(offset, limit)
    }

    /**
     * 获取mv信息
     */
    fun getMvDetailInfo(mvid: String): Observable<VideoInfoBean> {
        return apiService.getMvDetailInfo(mvid)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<VideoInfoBean> { e ->
                        try {
                            if (it.code == 200) {
                                it.data.let {
                                    val videoInfoBean = VideoInfoBean(it.id.toString(), it.name)
                                    videoInfoBean.coverUrl = it.cover
                                    videoInfoBean.commentCount = it.commentCount
                                    videoInfoBean.shareCount = it.shareCount
                                    videoInfoBean.playCount = it.playCount
                                    videoInfoBean.durationms = it.duration
                                    videoInfoBean.description = it.desc
                                    val artists = mutableListOf<Artist>()
                                    it.artists?.forEach { item ->
                                        val artist = Artist()
                                        artist.name = item.name
                                        artist.picUrl = item.img1v1Url
                                        artist.followed = item.followed
                                        artist.id = item.id.toLong()
                                        artists.add(artist)
                                    }
                                    videoInfoBean.artist = artists
                                    e.onNext(videoInfoBean)
                                }
                                e.onComplete()
                            } else {
                                e.onError(Throwable("接口请求异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取相似mv
     */
    fun getSimilarMv(mvid: String): Observable<MutableList<VideoInfoBean>> {
        return apiService.getSimilarMv(mvid)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<MutableList<VideoInfoBean>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<VideoInfoBean>()
                                it.data?.forEach {
                                    val videoInfoBean = VideoInfoBean(it.id, it.name)
                                    videoInfoBean.coverUrl = it.cover
                                    videoInfoBean.playCount = it.playCount
                                    videoInfoBean.durationms = it.duration
                                    videoInfoBean.description = it.desc
                                    videoInfoBean.type = 2
                                    val artists = mutableListOf<Artist>()
                                    it.artists?.forEach { item ->
                                        val artist = Artist()
                                        artist.name = item.name
                                        artist.id = item.id
                                        artists.add(artist)
                                    }
                                    videoInfoBean.artist = artists
                                    list.add(videoInfoBean)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("接口请求异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取mv信息
     */
    fun getVideoDetailInfo(mvid: String): Observable<VideoInfoBean> {
        return apiService.getVideoDetailInfo(mvid)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<VideoInfoBean> { e ->
                        try {
                            if (it.code == 200) {
                                it.data?.let {
                                    val videoInfoBean = VideoInfoBean(it.id.toString(), it.title)
                                    videoInfoBean.coverUrl = it.coverUrl
                                    videoInfoBean.commentCount = it.commentCount
                                    videoInfoBean.shareCount = it.shareCount
                                    videoInfoBean.playCount = it.playTime
                                    videoInfoBean.durationms = it.durationms
                                    videoInfoBean.width = it.width
                                    videoInfoBean.height = it.height
                                    videoInfoBean.description = it.description
                                    val artists = mutableListOf<Artist>()
                                    it.creator?.let { item ->
                                        val artist = Artist()
                                        artist.name = item.nickname
                                        artist.picUrl = item.avatarUrl
                                        artist.followed = item.followed
                                        artist.id = item.userId.toLong()
                                        artists.add(artist)
                                    }
                                    videoInfoBean.artist = artists
                                    e.onNext(videoInfoBean)
                                }
                                e.onComplete()
                            } else {
                                e.onError(Throwable("接口请求异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取相关Video
     */
    fun getRelatedVideoList(mvid: String): Observable<MutableList<VideoInfoBean>> {
        return apiService.getRelatedVideoList(mvid)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<MutableList<VideoInfoBean>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<VideoInfoBean>()
                                it.data?.forEach {
                                    it.vid?.let { vid ->
                                        val videoInfoBean = VideoInfoBean(vid, it.title)
                                        videoInfoBean.coverUrl = it.coverUrl
                                        videoInfoBean.commentCount = it.commentCount
                                        videoInfoBean.shareCount = it.shareCount
                                        videoInfoBean.playCount = it.playCount
                                        videoInfoBean.durationms = it.durationms
                                        videoInfoBean.description = it.description
                                        videoInfoBean.type = 1
                                        val artists = mutableListOf<Artist>()
                                        it.creator?.forEach { item ->
                                            val artist = Artist()
                                            artist.name = item.userName
                                            artist.id = item.userId.toLong()
                                            artists.add(artist)
                                        }
                                        videoInfoBean.artist = artists
                                        list.add(videoInfoBean)
                                    }
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("接口请求异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取mv评论
     */
    fun getMvComment(mvid: String, type: String, offset: Int = 0): Observable<MvComment> {
        return apiService.getMvComment(type, mvid, offset)
    }

    /**
     * 获取热搜
     */
    fun getHotSearchInfo(): Observable<MutableList<HotSearchBean>> {
        return apiService.getHotSearchInfo()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<HotSearchBean>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<HotSearchBean>()
                                it.result.hots?.forEach {
                                    list.add(HotSearchBean(it.first))
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 搜索
     */
    fun searchMoreInfo(keywords: String, limit: Int, offset: Int, type: Int): Observable<SearchInfo> {
        val url = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_API_URL, Constants.BASE_NETEASE_URL) + "/search?keywords= $keywords&limit=$limit&offset=$offset&type=$type"
//        return apiService.searchNetease(url)
//        @Query("keywords") keywords: String, @Query("limit") limit: Int, @Query("offset") offset: Int, @Query("type") type: Int
        return apiService.searchNetease(url)
    }

    /**
     * 获取风格
     */
    fun getCatList(): Observable<CatListBean> {
        return apiService.getCatList()
    }

    /**
     * 获取banner
     */
    fun getBanners(): Observable<BannerResult> {
        return apiService.getBanner()
    }

    /**
     *登录
     */
    fun loginPhone(username: String, pwd: String, isEmail: Boolean): Observable<LoginInfo> {
        return if (isEmail)
            apiService.loginEmail(username, pwd)
        else
            apiService.loginPhone(username, pwd)
    }

    /**
     * 获取登录状态
     */
    fun getLoginStatus(): Observable<LoginInfo> {
        return apiService.getLoginStatus()
    }

    /**
     * 注销绑定
     */
    fun logout(): Observable<Any> {
        return apiService.logout()
    }

    /**
     *推荐歌曲
     */
    fun recommendSongs(): Observable<MutableList<Music>> {
        return apiService.recommendSongs()
                .flatMap {
                    Observable.create(ObservableOnSubscribe<MutableList<Music>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Music>()
                                it.data?.dailySongs?.forEach {
                                    val music = Music()
                                    music.mid = it.id
                                    music.title = it.name
                                    music.type = Constants.NETEASE
                                    music.album = it.al?.name
                                    music.isOnline = true
                                    music.albumId = it.al?.id
                                    if (it.ar != null) {
                                        var artistIds = it.ar?.get(0)?.id.toString()
                                        var artistNames = it.ar?.get(0)?.name
                                        for (j in 1 until it.ar?.size!! - 1) {
                                            artistIds += ",${it.ar?.get(j)?.id}"
                                            artistNames += ",${it.ar?.get(j)?.name}"
                                        }
                                        music.artist = artistNames
                                        music.artistId = artistIds
                                    }
                                    music.coverUri = MusicUtils.getAlbumPic(it.al?.picUrl, Constants.NETEASE, MusicUtils.PIC_SIZE_NORMAL)
                                    music.coverBig = MusicUtils.getAlbumPic(it.al?.picUrl, Constants.NETEASE, MusicUtils.PIC_SIZE_BIG)
                                    music.coverSmall = MusicUtils.getAlbumPic(it.al?.picUrl, Constants.NETEASE, MusicUtils.PIC_SIZE_SMALL)
                                    list.add(music)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable(it.message))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }


    /**
     *每日推荐歌单
     */
    fun recommendPlaylist(): Observable<MutableList<Playlist>> {
        return apiService.recommendPlaylist()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Playlist>()
                                it.recommend?.forEach {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.coverUrl = if (it.coverImgUrl != null) it.coverImgUrl else it.creator.avatarUrl
                                    playlist.des = it.description
                                    playlist.date = it.createTime
                                    playlist.updateDate = it.updateTime
                                    playlist.playCount = it.playCount.toLong()
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable(it.msg))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     *推荐歌单
     */
    fun personalizedPlaylist(): Observable<MutableList<Playlist>> {
        return apiService.personalizedPlaylist()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Playlist>()
                                it.result?.forEach {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.coverUrl = it.picUrl
                                    playlist.des = it.copywriter
                                    playlist.playCount = it.playCount.toLong()
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable(""))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }


    /**
     *推荐mv
     */
    fun personalizedMv(): Observable<MvInfo> {
        return apiService.personalizedMv()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MvInfo> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<MvInfoDetail>()
                                it.result?.forEach {
                                    val data = MvInfoDetail(
                                            artistId = it.artistId,
                                            id = it.id.toString(),
                                            artistName = it.artistName,
                                            artists = it.artists,
                                            cover = it.picUrl,
                                            playCount = it.playCount,
                                            duration = it.duration,
                                            desc = it.copywriter,
                                            name = it.name

                                    )
                                    list.add(data)
                                }
                                val mvInfo = MvInfo(code = 200, hasMore = false, updateTime = 0, data = list)
                                e.onNext(mvInfo)
                                e.onComplete()
                            } else {
                                e.onError(Throwable(""))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     *获取用户歌单
     */
    fun getUserPlaylist(uid: String): Observable<MutableList<Playlist>> {
        return apiService.getUserPlaylist(uid)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Playlist>()
                                it.playlist?.forEach {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.coverUrl = it.coverImgUrl
                                    playlist.des = it.description
                                    playlist.date = it.createTime
                                    playlist.updateDate = it.updateTime
                                    playlist.total = it.trackCount.toLong()
                                    playlist.playCount = it.playCount.toLong()
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }


    /**
     *获取网易云排行榜歌单
     */
    fun getTopList(): Observable<MutableList<Playlist>> {
        return apiService.getTopList()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<Playlist>()
                                LogUtil.d(TAG, "playlist= ${it.list.size}")
                                it.list.forEach {
                                    val playlist = Playlist()
                                    playlist.pid = it.id.toString()
                                    playlist.name = it.name
                                    playlist.updateFrequency = it.updateFrequency
                                    playlist.coverUrl = it.coverImgUrl
                                    playlist.des = it.description
                                    playlist.date = it.createTime
                                    playlist.updateDate = it.updateTime
                                    playlist.total = it.trackCount
                                    playlist.playCount = it.playCount
                                    playlist.type = Constants.PLAYLIST_WY_ID
                                    if (it.ToplistType != null) {
                                        LogUtil.d(TAG, "type = ${it.ToplistType} ${it.tracks} ")
                                        val musicList = mutableListOf<Music>()
                                        it.tracks?.forEach { track ->
                                            val music = Music()
                                            music.title = track.first
                                            music.artist = track.second
                                            musicList.add(music)
                                        }
                                        playlist.musicList = musicList
                                    }
                                    LogUtil.d(TAG, "playlist = $playlist ")
                                    list.add(playlist)
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                LogUtil.d(TAG, "网络异常= ${it.list.size}")
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            LogUtil.d(TAG, "Exception= ${ep.message}")
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取私人FM
     */
//    fun getPersonalFm(): Observable<Playlist> {
//        return apiService.getPersonlFM()
//                .flatMap { it ->
//                    Observable.create(ObservableOnSubscribe<Playlist> { e ->
//                        try {
//                            if (it.code == 200) {
//                                val playlist = Playlist().apply {
//                                    name = "私人FM"
//                                    pid = "personal_fm"
//                                }
//                                val musicList = mutableListOf<Music>()
//                                it.data?.forEach {
//                                    val music = Music()
//                                    music.mid = it.id.toString()
//                                    music.title = it.name
//                                    it.artists?.forEach { item ->
//                                        music.artist += item.name
//                                        music.artistId += item.id
//                                    }
//                                    music.coverUri = it.album.picUrl
//                                    music.album = it.album.name
//                                    music.albumId = it.album.id
//                                    music.type = Constants.NETEASE
//                                    musicList.add(music)
//                                }
//                                playlist.musicList = musicList
//                                e.onNext(playlist)
//                                e.onComplete()
//                            } else {
//                                LogUtil.d(TAG, "getPersonalFm 网络异常= $it")
//                                e.onError(Throwable("网络异常"))
//                            }
//                        } catch (ep: Exception) {
//                            LogUtil.d(TAG, "Exception= ${ep.message}")
//                            e.onError(ep)
//                        }
//                    })
//                }
//    }

    /**
     * 获取风格
     */
    fun getVideoCatList(): Observable<MutableList<CategoryInfo>> {
        return apiService.getVideoGroupList()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<CategoryInfo>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<CategoryInfo>()
                                LogUtil.d(TAG, "getVideoGroupList= ${it.data}")
                                it.data?.forEach {
                                    val categoryInfo = CategoryInfo(it.id, it.name)
                                    LogUtil.d(TAG, "categoryInfo = $categoryInfo ")
                                    if ("MV" == it.name) {
                                        list.add(0, categoryInfo)
                                    } else {
                                        list.add(categoryInfo)
                                    }
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                LogUtil.d(TAG, "网络异常=")
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            LogUtil.d(TAG, "Exception= ${ep.message}")
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取视频列表
     */
    fun getVideoList(id: String, offset: Int = 0): Observable<MutableList<VideoInfoBean>> {
        return apiService.getVideoList(id, offset)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<VideoInfoBean>> { e ->
                        try {
                            if (it.code == 200 && it.data?.size ?: 0 > 0) {
                                val list = mutableListOf<VideoInfoBean>()
                                LogUtil.d(TAG, "getVideoGroupList= ${it.msg}")
                                it.data?.forEach {
                                    if (it.type == 1) {
                                        it.data.vid?.let { vid ->
                                            val bean = VideoInfoBean(vid, it.data.title)
                                            bean.commentCount = it.data.commentCount
                                            bean.coverUrl = it.data.coverUrl
                                            bean.description = it.data.description
                                            bean.durationms = it.data.durationms
                                            bean.praisedCount = it.data.praisedCount
                                            bean.playCount = it.data.playTime
                                            bean.shareCount = it.data.shareCount
                                            bean.type = it.type
                                            //解析creator
                                            val artist = Artist()
                                            artist.artistId = it.data.creator?.userId
                                            artist.name = it.data.creator?.nickname
                                            artist.picUrl = it.data.creator?.avatarUrl
                                            bean.artist = mutableListOf(artist)
                                            list.add(bean)
                                        }
                                    } else if (it.type == 2) {//mv
                                        it.data.id?.let { id ->
                                            val bean = VideoInfoBean(id, it.data.name)
                                            bean.commentCount = it.data.commentCount
                                            bean.coverUrl = it.data.coverUrl
                                            bean.durationms = it.data.duration
                                            bean.praisedCount = it.data.likeCount
                                            bean.playCount = it.data.playCount
                                            bean.shareCount = it.data.shareCount
                                            bean.type = it.type
                                            //解析歌手
                                            val artists = mutableListOf<Artist>()
                                            it.data.artists?.forEach { art ->
                                                val artist = Artist()
                                                artist.artistId = art.id
                                                artist.name = art.name
                                                artist.picUrl = art.img1v1Url
                                                artist.followed = art.followed
                                                artists.add(artist)
                                            }
                                            bean.artist = artists
                                            list.add(bean)
                                        }
                                    }
                                }
                                e.onNext(list)
                                e.onComplete()
                            } else {
                                val msg = it.msg
                                LogUtil.e(TAG, msg)
                                e.onError(Throwable(msg))
                            }
                        } catch (ep: Exception) {
                            LogUtil.e(TAG, "Exception= ${ep.message}")
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 获取视频、MV播放地址
     */
    fun getVideoUrlInfo(type: Int, id: String): Observable<String> {
        if (type == 2) {
            return apiService.getMvUrlInfo(id)
                    .flatMap { it ->
                        Observable.create(ObservableOnSubscribe<String> { e ->
                            try {
                                if (it.code == 200) {
                                    var url = ""
                                    it.data?.url?.let {
                                        LogUtil.d(TAG, "播放地址=$it")
                                        url = it
                                    }
                                    e.onNext(url)
                                    e.onComplete()
                                } else {
                                    LogUtil.d(TAG, "网络异常=")
                                    e.onError(Throwable("网络异常"))
                                }
                            } catch (ep: Exception) {
                                LogUtil.d(TAG, "Exception= ${ep.message}")
                                e.onError(ep)
                            }
                        })
                    }
        }
        return apiService.getVideoUrlInfo(id)
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<String> { e ->
                        try {
                            if (it.code == 200) {
                                var url = ""
                                it.urls.forEach {
                                    LogUtil.d(TAG, "播放地址=$it")
                                    url = it.url
                                    return@forEach
                                }
                                e.onNext(url)
                                e.onComplete()
                            } else {
                                LogUtil.d(TAG, "网络异常=")
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            LogUtil.d(TAG, "Exception= ${ep.message}")
                            e.onError(ep)
                        }
                    })
                }
    }

}