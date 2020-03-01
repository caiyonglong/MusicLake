package com.cyl.musiclake.bean

import com.music.lake.musiclib.bean.BaseMusicInfo
import org.junit.Test

class BaseMusicInfoInfoInfoTest {
    @Test
    fun testOrder() {
        val musicList = mutableListOf(
                BaseMusicInfo().apply { title = "特殊" },
                BaseMusicInfo().apply { title = "演员" },
                BaseMusicInfo().apply { title = "a" },
                BaseMusicInfo().apply { title = "x" },
                BaseMusicInfo().apply { title = "t" },
                BaseMusicInfo().apply { title = "d" },
                BaseMusicInfo().apply { title = "历史" }
        )

        musicList.forEach {
            println(it.title)
        }

        println("排序后----")
        musicList.sortBy { it ->
            it.title
        }

        musicList.forEach {
            println(it.title)
        }
    }


    @Test
    fun testRondom() {
        val musicList = mutableListOf(
                0,1,2,3,4,5,6,7,8,9)

        musicList.forEach {
            println(it)
        }

        println("排序后----")
        musicList.shuffle()

        musicList.forEach {
            println(it)
        }
    }
}