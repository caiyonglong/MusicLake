package com.cyl.musiclake.ui.music.my

import com.cyl.musiclake.ui.base.BaseContract
import com.music.lake.musiclib.bean.BaseMusicInfo
import com.cyl.musiclake.bean.NoticeInfo
import com.cyl.musiclake.bean.Playlist

interface MyMusicContract {

    interface View : BaseContract.BaseView {

        fun showSongs(songList: MutableList<BaseMusicInfo>)

        fun showVideoList(videoList: MutableList<BaseMusicInfo>)

        fun showLocalPlaylist(playlists: MutableList<Playlist>)

        fun showPlaylist(playlists: MutableList<Playlist>)

        fun showWyPlaylist(playlists: MutableList<Playlist>)

        fun showHistory(baseMusicInfoInfoList: MutableList<BaseMusicInfo>)

        fun showLoveList(baseMusicInfoInfoList: MutableList<BaseMusicInfo>)

        fun showDownloadList(baseMusicInfoInfoList: MutableList<BaseMusicInfo>)

        fun showNoticeInfo(notice: NoticeInfo)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSongs()

        fun loadPlaylist(playlist: Playlist? = null)
    }
}
