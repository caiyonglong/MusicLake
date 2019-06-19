package com.cyl.musiclake.ui.music.artist.contract

import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.ui.base.BaseContract

interface ArtistDetailContract {

    interface View : BaseContract.BaseView {

        //显示歌曲列表
        fun showPlaylistSongs(songList: MutableList<Music>?)

        //显示歌手信息
        fun showArtistInfo(artist: Artist)

        //显示歌手的所有专辑
        fun showAllAlbum(albumList: MutableList<Album>)

        fun showTitle(title: String) {}

        fun showCover(cover: String) {}

        fun showDescInfo(title: String) {}

        fun removeMusic(position: Int) {}

        fun success(type: Int)

        fun showEmptyView(msg: String)
        //显示歌单异常，提示
        fun showErrorTips(msg: String, hasTry: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadPlaylistSongs(playlist: Playlist)

        fun loadArtistSongs(artist: Artist)

        fun loadArtistAlbum(artist: Artist)

        fun loadAlbumSongs(album: Album)

        fun deletePlaylist(playlist: Playlist)

        fun renamePlaylist(playlist: Playlist, title: String)
    }
}
