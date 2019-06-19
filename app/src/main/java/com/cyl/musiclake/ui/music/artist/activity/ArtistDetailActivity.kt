package com.cyl.musiclake.ui.music.artist.activity

import android.view.Menu
import android.view.MenuItem
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Extras
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.main.PageAdapter
import com.cyl.musiclake.ui.music.artist.contract.ArtistDetailContract
import com.cyl.musiclake.ui.music.artist.fragment.AlbumFragment
import com.cyl.musiclake.ui.music.artist.fragment.ArtistInfoFragment
import com.cyl.musiclake.ui.music.artist.fragment.ArtistSongsFragment
import com.cyl.musiclake.ui.music.artist.presenter.ArtistDetailPresenter
import com.cyl.musiclake.utils.CoverLoader
import kotlinx.android.synthetic.main.frag_artist_detail.*

/**
 * 作者：yonglong on 2016/8/15 19:54
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 歌单详情页
 */
class ArtistDetailActivity : BaseActivity<ArtistDetailPresenter>(), ArtistDetailContract.View {

    override fun showAllAlbum(albumList: MutableList<Album>) {
        albumFragment?.showAlbums(albumList)
    }

    override fun showArtistInfo(artist: Artist) {
        artistSongsFragment?.artistID = artist.artistId
        artistInfoFragment?.updateArtistDesc(artist.desc)
    }

    /**
     * 显示异常UI
     */
    override fun showErrorTips(msg: String, hasTry: Boolean) {
        showError(msg, hasTry)
    }

    override fun showEmptyView(msg: String) {

    }

    override fun showTitle(title: String) {
        updateTitle(title)
    }

    override fun showCover(cover: String) {
        CoverLoader.loadImageView(context, coverUrl, album_art)
    }

    override fun showDescInfo(title: String) {

    }

    private val musicList = mutableListOf<Music>()
    private var mArtist: Artist? = null
    private var pid: String? = null
    private var mAlbum: Album? = null
    private var title: String? = null
    private var coverUrl: String? = null
    private var type: String? = null
    private var artistInfoFragment: ArtistInfoFragment? = null
    private var artistSongsFragment: ArtistSongsFragment? = null
    private var albumFragment: AlbumFragment? = null

    override fun getLayoutResID(): Int {
        return R.layout.frag_artist_detail
    }

    override fun initData() {
        showLoading()
        mArtist?.let {
            mPresenter?.loadArtistSongs(it)
            mPresenter?.loadArtistAlbum(it)
        }
        mAlbum?.let {
            mPresenter?.loadAlbumSongs(it)
        }
    }

    /**
     * 歌单页标题
     */
    override fun setToolbarTitle(): String? {
        mArtist = intent.getParcelableExtra(Extras.ARTIST)
        mAlbum = intent.getSerializableExtra(Extras.ALBUM) as Album?
        mArtist?.let {
            title = it.name
            pid = it.artistId.toString()
            coverUrl = it.picUrl
            CoverLoader.loadBigImageView(context, coverUrl, it.type, album_art)
        }
        mAlbum?.let {
            title = it.name
            pid = it.albumId.toString()
            coverUrl = it.cover
            CoverLoader.loadBigImageView(context, coverUrl, it.type, album_art)
        }
        return title
    }

    override fun initView() {
        tabs?.setupWithViewPager(viewPager)
        setupViewPager()
        viewPager?.offscreenPageLimit = 3
    }


    private fun setupViewPager() {
        artistInfoFragment = ArtistInfoFragment.newInstance("")
        artistSongsFragment = ArtistSongsFragment.newInstance()
        albumFragment = AlbumFragment.newInstance("")

        val mAdapter = PageAdapter(supportFragmentManager)
        mAdapter.addFragment(artistSongsFragment, "歌曲")
        mAdapter.addFragment(albumFragment, "专辑")
        mAdapter.addFragment(artistInfoFragment, "详情")
        viewPager?.adapter = mAdapter
    }


    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
        }
        return super.onOptionsItemSelected(item)

    }

    private fun onBackPress() {
        this.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_artist_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun showPlaylistSongs(songList: MutableList<Music>?) {
        hideLoading()
        songList?.let {
            musicList.addAll(songList)
        }

        artistSongsFragment?.showSongs(musicList)

        if (coverUrl == null) {
            mArtist?.picUrl?.let {
                coverUrl = it
            }
            if (musicList.size > 0 && coverUrl == null) {
                coverUrl = musicList[0].coverUri
                type = musicList[0].type
            }
            CoverLoader.loadBigImageView(context, coverUrl, type, album_art)
        }
        if (musicList.size == 0) {
            showEmptyState()
        }
    }

    override fun removeMusic(position: Int) {
        musicList.removeAt(position)
        if (musicList.size == 0) {
            showEmptyState()
        }
    }

    override fun success(type: Int) {
        onBackPress()
    }

    /**
     * 点击重试按钮响应事件
     */
    override fun retryLoading() {
        super.retryLoading()
        showLoading()
        mArtist?.let {
            mPresenter?.loadArtistSongs(it)
            mPresenter?.loadArtistSongs(it)
        }
        mAlbum?.let {
            mPresenter?.loadAlbumSongs(it)
        }
    }
}
