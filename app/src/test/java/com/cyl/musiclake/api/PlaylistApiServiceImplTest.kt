package com.cyl.musiclake.api

import com.cyl.musiclake.api.playlist.PlaylistApiServiceImpl
import org.junit.Test

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/26 .
 */
class PlaylistApiServiceImplTest {

    @Test
    fun getPlaylist() {
        PlaylistApiServiceImpl.getPlaylist()
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
    }

    @Test
    fun getMusicInfo() {
    }
}