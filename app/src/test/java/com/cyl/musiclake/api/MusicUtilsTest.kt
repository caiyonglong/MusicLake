package com.cyl.musiclake.api

import com.cyl.musiclake.api.music.MusicUtils
import com.cyl.musiclake.common.Constants
import org.junit.Test

/**
 * Created by cyl on 2018/9/29.
 */
class MusicUtilsTest {
    @Test
    fun testPicUrl() {
        val test1 = "http://p2.music.126.net/Q1Zr0gEI3vhCTdESp4Lzlg==/109951163432953075.jpg?param=150y150"
        val test2 = "http://p1.music.126.net/0YelkMg43burWt8EowEl4Q==/18751071301805518.jpg?param=150y150"
        val test3 = "http://qukufile2.qianqian.com/data2/pic/70a9db3a83861e29664c4bcc39b117ca/278726145/278726145.jpg@s_1,w_150,h_150"
        val test5 = "https://y.gtimg.cn/music/photo_new/T002R150x150M000002gnOZC4T8gXF.jpg"
        val test4 = "https://pic.xiami.net/images/album/img76/80576/2905071531290507.jpg@1e_1c_100Q_55w_55h"
        println(getAlbumPic(test1, Constants.NETEASE, MusicUtils.PIC_SIZE_SMALL))
        println(getAlbumPic(test2, Constants.NETEASE, MusicUtils.PIC_SIZE_SMALL))
        println(getAlbumPic(test3, Constants.BAIDU, MusicUtils.PIC_SIZE_SMALL))
        println(getAlbumPic(test4, Constants.XIAMI, MusicUtils.PIC_SIZE_SMALL))
        println(getAlbumPic(test5, Constants.QQ, MusicUtils.PIC_SIZE_SMALL))
    }

    /**
     * 根据不同的歌曲类型生成不同的图片
     * @param size
     */
    fun getAlbumPic(url: String?, type: String?, size: Int): String? {
        println(url)
        return when (type) {
            Constants.QQ -> {
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> {
                        url?.replace("150x150", "90x90")
                    }
                    MusicUtils.PIC_SIZE_NORMAL -> {
                        url?.replace("150x150", "150x150")
                    }
                    else -> {
                        url?.replace("150x150", "300x300")
                    }
                }
            }
            Constants.XIAMI -> {
                val tmp = url?.split("@")?.get(0) ?: url
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> "$tmp@1e_1c_100Q_90w_90h"
                    MusicUtils.PIC_SIZE_NORMAL -> "$tmp@1e_1c_100Q_150w_150h"
                    else -> "$tmp@1e_1c_100Q_450w_450h"
                }
            }
            Constants.NETEASE -> {
                val temp = url?.substring(0, url.indexOf("?"))
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> "$temp?param=90y90"
                    MusicUtils.PIC_SIZE_NORMAL -> "$temp?param=150y150"
                    else -> "$temp?param=450y450"
                }
            }
            Constants.BAIDU -> {
                val tmp = url?.split("@")?.get(0) ?: url
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> "$tmp@s_1,w_90,h_90"
                    MusicUtils.PIC_SIZE_NORMAL -> "$tmp@s_1,w_150,h_150"
                    else -> "$tmp@s_1,w_300,h_300"
                }
            }
            else -> {
                url
            }
        }
    }


}