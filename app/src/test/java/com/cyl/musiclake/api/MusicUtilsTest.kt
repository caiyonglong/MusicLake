package com.cyl.musiclake.api

import android.net.Uri
import com.cyl.musiclake.common.Constants
import org.junit.Test

/**
 * Created by cyl on 2018/9/29.
 */
class MusicUtilsTest {
    @Test
    fun testPicUrl() {
        val test1 = "http://p2.music.126.net/Q1Zr0gEI3vhCTdESp4Lzlg==/109951163432953075.jpg?param=150y150"
        println(test1)
        println(getAlbumPic(test1, Constants.NETEASE, MusicUtils.PIC_SIZE_SMALL))
    }

    /**
     * 根据不同的歌曲类型生成不同的图片
     * @param size
     */
    fun getAlbumPic(url: String?, type: String?, size: Int): String? {
        var originUrl = url
        return when (type) {
            Constants.QQ -> {
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> {
                        url?.replace("300x300", "90x90")
                    }
                    MusicUtils.PIC_SIZE_NORMAL -> {
                        url?.replace("300x300", "150x150")
                    }
                    else -> {
                        url
                    }
                }
            }
            Constants.XIAMI -> {
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> "$url@1e_1c_100Q_90w_90h"
                    MusicUtils.PIC_SIZE_NORMAL -> "$url@1e_1c_100Q_150w_150h"
                    else -> "$url@1e_1c_100Q_300w_300h"
                }
            }
            Constants.NETEASE -> {
                val uri = Uri.parse(url)
                val type = uri.getQueryParameter("param")
                if (type == null) {
                    when (size) {
                        MusicUtils.PIC_SIZE_SMALL -> {
                            "${originUrl}param=90y90"
                        }
                        MusicUtils.PIC_SIZE_NORMAL -> "$originUrl?param=150y150"
                        else -> "$originUrl?param=450y450"
                    }
                } else {
                    when (size) {
                        MusicUtils.PIC_SIZE_SMALL -> {
                            "${originUrl}param=90y90"
                        }
                        MusicUtils.PIC_SIZE_NORMAL -> "$originUrl?param=150y150"
                        else -> "$originUrl?param=450y450"
                    }
                }
            }
            Constants.BAIDU -> {
                when (size) {
                    MusicUtils.PIC_SIZE_SMALL -> "$url?@s_1,w_90,h_90"
                    MusicUtils.PIC_SIZE_NORMAL -> "$url?@s_1,w_150,h_150"
                    else -> "$url?@s_1,w_300,h_300"
                }
            }
            else -> {
                url
            }
        }
    }


}