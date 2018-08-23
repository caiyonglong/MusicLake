//package com.cyl.musiclake.api.netease
//
//import java.io.Serializable
//
///**
// * Created by master on 2018/3/20.
// */
//
//class NeteaseMusic : Serializable {
//    /**
//     * name : 海阔天空
//     * id : 400162138
//     * position : 4
//     * alias : []
//     * status : 0
//     * fee : 8
//     * copyrightId : 7002
//     * disc : 1
//     * no : 4
//     * artists : [{"name":"Beyond","id":11127,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]
//     * album : {"name":"华纳23周年纪念精选系列","id":34430029,"type":"专辑","size":14,"picId":3273246124149810,"blurPicUrl":"http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg","companyId":0,"pic":3273246124149810,"picUrl":"http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg","publishTime":999273600007,"description":"","tags":"","company":"华纳唱片","briefDesc":"","artist":{"name":"","id":0,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0},"songs":[],"alias":[],"status":3,"copyrightId":7002,"commentThreadId":"R_AL_3_34430029","artists":[{"name":"Beyond","id":11127,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]}
//     * starred : false
//     * popularity : 100.0
//     * score : 100
//     * starredNum : 0
//     * duration : 323693
//     * playedNum : 0
//     * dayPlays : 0
//     * hearTime : 0
//     * ringtone : null
//     * crbt : null
//     * audition : null
//     * copyFrom :
//     * commentThreadId : R_SO_4_400162138
//     * rtUrl : null
//     * ftype : 0
//     * rtUrls : []
//     * copyright : 1
//     * hMusic : {"name":null,"id":1186538478,"size":12950508,"extension":"mp3","sr":44100,"dfsId":3274345635867873,"bitrate":320000,"playTime":323693,"volumeDelta":-1.11}
//     * mMusic : {"name":null,"id":1186538479,"size":6475276,"extension":"mp3","sr":44100,"dfsId":3274345635867874,"bitrate":160000,"playTime":323693,"volumeDelta":-0.69}
//     * lMusic : {"name":null,"id":1186538480,"size":3885183,"extension":"mp3","sr":44100,"dfsId":3274345635867875,"bitrate":96000,"playTime":323693,"volumeDelta":-0.72}
//     * bMusic : {"name":null,"id":1186538480,"size":3885183,"extension":"mp3","sr":44100,"dfsId":3274345635867875,"bitrate":96000,"playTime":323693,"volumeDelta":-0.72}
//     * mvid : 0
//     * rtype : 0
//     * rurl : null
//     * mp3Url : http://m2.music.126.net/RLt9aRW7UaHB77IPjeEUGw==/3274345635867875.mp3
//     */
//
//    var name: String? = null
//    var id: Int = 0
//    var position: Int = 0
//    var status: Int = 0
//    var fee: Int = 0
//    var copyrightId: Int = 0
//    var disc: String? = null
//    var no: Int = 0
//    var album: AlbumBean? = null
//    var isStarred: Boolean = false
//    var popularity: Double = 0.toDouble()
//    var score: Int = 0
//    var starredNum: Int = 0
//    var duration: Int = 0
//    var playedNum: Int = 0
//    var dayPlays: Int = 0
//    var hearTime: Int = 0
//    var ringtone: Any? = null
//    var crbt: Any? = null
//    var audition: Any? = null
//    var copyFrom: String? = null
//    var commentThreadId: String? = null
//    var rtUrl: Any? = null
//    var ftype: Int = 0
//    var copyright: Int = 0
//    var hMusic: HMusicBean? = null
//    var mMusic: MMusicBean? = null
//    var lMusic: LMusicBean? = null
//    var bMusic: BMusicBean? = null
//    var mvid: Int = 0
//    var rtype: Int = 0
//    var rurl: Any? = null
//    var mp3Url: String? = null
//    var alias: List<*>? = null
//    var artists: List<ArtistsBeanX>? = null
//    var rtUrls: List<*>? = null
//
//    val authors: String?
//        get() {
//            if (artists!!.size == 0) return null
//
//            val artist = StringBuilder(artists!![0].name)
//            for (i in 1 until artists!!.size) {
//                artist.append(",").append(artists!![i].name)
//            }
//            return artist.toString()
//        }
//
//    val authorIds: String?
//        get() {
//            if (artists!!.size == 0) return null
//
//            val artist = StringBuilder(artists!![0].id)
//            for (i in 1 until artists!!.size) {
//                artist.append(",").append(artists!![i].id)
//            }
//            return artist.toString()
//        }
//
//    class AlbumBean : Serializable {
//        /**
//         * name : 华纳23周年纪念精选系列
//         * id : 34430029
//         * type : 专辑
//         * size : 14
//         * picId : 3273246124149810
//         * blurPicUrl : http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg
//         * companyId : 0
//         * pic : 3273246124149810
//         * picUrl : http://p1.music.126.net/a9oLdcFPhqQyuouJzG2mAQ==/3273246124149810.jpg
//         * publishTime : 999273600007
//         * description :
//         * tags :
//         * company : 华纳唱片
//         * briefDesc :
//         * artist : {"name":"","id":0,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}
//         * songs : []
//         * alias : []
//         * status : 3
//         * copyrightId : 7002
//         * commentThreadId : R_AL_3_34430029
//         * artists : [{"name":"Beyond","id":11127,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]
//         */
//
//        var name: String? = null
//        var id: Int = 0
//        var type: String? = null
//        var size: Int = 0
//        var picId: Long = 0
//        var blurPicUrl: String? = null
//        var companyId: Int = 0
//        var pic: Long = 0
//        var picUrl: String? = null
//        var publishTime: Long = 0
//        var description: String? = null
//        var tags: String? = null
//        var company: String? = null
//        var briefDesc: String? = null
//        var artist: ArtistBean? = null
//        var status: Int = 0
//        var copyrightId: Int = 0
//        var commentThreadId: String? = null
//        var songs: List<*>? = null
//        var alias: List<*>? = null
//        var artists: List<ArtistsBean>? = null
//
//        class ArtistBean : Serializable {
//            /**
//             * name :
//             * id : 0
//             * picId : 0
//             * img1v1Id : 0
//             * briefDesc :
//             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
//             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
//             * albumSize : 0
//             * alias : []
//             * trans :
//             * musicSize : 0
//             */
//
//            var name: String? = null
//            var id: Int = 0
//            var picId: Int = 0
//            var img1v1Id: Int = 0
//            var briefDesc: String? = null
//            var picUrl: String? = null
//            var img1v1Url: String? = null
//            var albumSize: Int = 0
//            var trans: String? = null
//            var musicSize: Int = 0
//            var alias: List<*>? = null
//        }
//
//        class ArtistsBean : Serializable {
//            /**
//             * name : Beyond
//             * id : 11127
//             * picId : 0
//             * img1v1Id : 0
//             * briefDesc :
//             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
//             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
//             * albumSize : 0
//             * alias : []
//             * trans :
//             * musicSize : 0
//             */
//
//            var name: String? = null
//            var id: Int = 0
//            var picId: Int = 0
//            var img1v1Id: Int = 0
//            var briefDesc: String? = null
//            var picUrl: String? = null
//            var img1v1Url: String? = null
//            var albumSize: Int = 0
//            var trans: String? = null
//            var musicSize: Int = 0
//            var alias: List<*>? = null
//        }
//    }
//
//    class HMusicBean : Serializable {
//        /**
//         * name : null
//         * id : 1186538478
//         * size : 12950508
//         * extension : mp3
//         * sr : 44100
//         * dfsId : 3274345635867873
//         * bitrate : 320000
//         * playTime : 323693
//         * volumeDelta : -1.11
//         */
//
//        var name: Any? = null
//        var id: Int = 0
//        var size: Int = 0
//        var extension: String? = null
//        var sr: Int = 0
//        var dfsId: Long = 0
//        var bitrate: Int = 0
//        var playTime: Int = 0
//        var volumeDelta: Double = 0.toDouble()
//    }
//
//    class MMusicBean : Serializable {
//        /**
//         * name : null
//         * id : 1186538479
//         * size : 6475276
//         * extension : mp3
//         * sr : 44100
//         * dfsId : 3274345635867874
//         * bitrate : 160000
//         * playTime : 323693
//         * volumeDelta : -0.69
//         */
//
//        var name: Any? = null
//        var id: Int = 0
//        var size: Int = 0
//        var extension: String? = null
//        var sr: Int = 0
//        var dfsId: Long = 0
//        var bitrate: Int = 0
//        var playTime: Int = 0
//        var volumeDelta: Double = 0.toDouble()
//    }
//
//    class LMusicBean : Serializable {
//        /**
//         * name : null
//         * id : 1186538480
//         * size : 3885183
//         * extension : mp3
//         * sr : 44100
//         * dfsId : 3274345635867875
//         * bitrate : 96000
//         * playTime : 323693
//         * volumeDelta : -0.72
//         */
//
//        var name: Any? = null
//        var id: Int = 0
//        var size: Int = 0
//        var extension: String? = null
//        var sr: Int = 0
//        var dfsId: Long = 0
//        var bitrate: Int = 0
//        var playTime: Int = 0
//        var volumeDelta: Double = 0.toDouble()
//    }
//
//    class BMusicBean : Serializable {
//        /**
//         * name : null
//         * id : 1186538480
//         * size : 3885183
//         * extension : mp3
//         * sr : 44100
//         * dfsId : 3274345635867875
//         * bitrate : 96000
//         * playTime : 323693
//         * volumeDelta : -0.72
//         */
//
//        var name: Any? = null
//        var id: Int = 0
//        var size: Int = 0
//        var extension: String? = null
//        var sr: Int = 0
//        var dfsId: Long = 0
//        var bitrate: Int = 0
//        var playTime: Int = 0
//        var volumeDelta: Double = 0.toDouble()
//    }
//
//    class ArtistsBeanX : Serializable {
//        /**
//         * name : Beyond
//         * id : 11127
//         * picId : 0
//         * img1v1Id : 0
//         * briefDesc :
//         * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
//         * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
//         * albumSize : 0
//         * alias : []
//         * trans :
//         * musicSize : 0
//         */
//
//        var name: String? = null
//        var id: Int = 0
//        var picId: Int = 0
//        var img1v1Id: Int = 0
//        var briefDesc: String? = null
//        var picUrl: String? = null
//        var img1v1Url: String? = null
//        var albumSize: Int = 0
//        var trans: String? = null
//        var musicSize: Int = 0
//        var alias: List<*>? = null
//    }
//}
