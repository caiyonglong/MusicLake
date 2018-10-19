package com.cyl.musiclake.bean

import org.junit.Test

class MusicTest {
    @Test
    fun testOrder() {
        val musicList = mutableListOf(
                Music().apply { title = "特殊" },
                Music().apply { title = "演员" },
                Music().apply { title = "a" },
                Music().apply { title = "x" },
                Music().apply { title = "t" },
                Music().apply { title = "d" },
                Music().apply { title = "历史" }
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