package com.cyl.musiclake.ui.music.online.base

import android.content.Context

import com.cyl.musicapi.BaseApiImpl
import com.cyl.musicapi.bean.ListItem
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.db.Music
import com.cyl.musiclake.data.db.Playlist

import java.util.ArrayList

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class PlaylistPresenter @Inject
constructor() : BasePresenter<PlaylistContract.View>(), PlaylistContract.Presenter {

    override fun getPlaylist(idx: String, context: Context) {
        BaseApiImpl.getInstance(context).getTopList(idx) { (data) ->
            val playlist = Playlist()
            playlist.pid = idx
            playlist.name = data.name
            playlist.count = data.playCount
            playlist.coverUrl = data.cover
            playlist.des = data.description
            val musicList = ArrayList<Music>()
            if (data.list!!.isNotEmpty()) {
                for (item in data.list!!) {
                    val music = MusicUtils.getMusic(item)
                    if (music.isCp) continue
                    musicList.add(music)
                }
            }
            playlist.musicList = musicList
            mView?.showPlayList(playlist)
        }
    }

}
