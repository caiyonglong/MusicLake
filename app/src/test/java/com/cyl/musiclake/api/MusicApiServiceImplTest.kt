package com.cyl.musiclake.api

import org.junit.Test

import org.junit.Assert.*

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/26 .
 */
class MusicApiServiceImplTest {

    @Test
    fun getPlaylist() {
        MusicApiServiceImpl.getPlaylist()
                .subscribe {
                    print(it.toString())
                }
    }

    @Test
    fun getMusicList() {
    }

    @Test
    fun createPlaylist() {
    }

    @Test
    fun deletePlaylist() {
    }

    @Test
    fun renamePlaylist() {
    }

    @Test
    fun collectMusic() {
    }

    @Test
    fun disCollectMusic() {
    }

    @Test
    fun login() {
        MusicApiServiceImpl.login("9D6F0084618AACDE881FDCA267F5CFDD", "35300E15E9E245DF0B04031EF6032CD6")
                .subscribe {
                    print(it.toString())
                }
    }

    @Test
    fun getMusicInfo() {
    }
}