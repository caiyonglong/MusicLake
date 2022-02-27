package com.cyl.musiclake.ui.music.discover

import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyl.musicapi.netease.BannerBean
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.base.BaseLazyFragment
import com.cyl.musiclake.ui.music.discover.artist.QQArtistListFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cyl.musiclake.ui.music.playlist.square.PlaylistSquareActivity
import com.cyl.musiclake.utils.LogUtil
import kotlinx.android.synthetic.main.frag_discover.*
import org.jetbrains.anko.support.v4.startActivity


/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class NewDiscoverFragment : BaseLazyFragment<DiscoverPresenter>(), DiscoverContract.View, View.OnClickListener {

    private val TAG = "DiscoverFragment"

    private var mAdapter: DiscoverAdapter? = null

    /**
     * 适配器
     */
    private var mNeteaseAdapter: TopPlaylistAdapter? = null
    private var mArtistListAdapter: TopArtistListAdapter? = null
    private var mRadioAdapter: BaiduRadioAdapter? = null
    private var mMusicAdapter: SongAdapter? = null
    private var mPlaylistAdapter: TopPlaylistAdapter? = null

    /**
     * 数据集合
     */
    private var playlist = mutableListOf<Playlist>()
    private var artists = mutableListOf<Artist>()
    private var channels = mutableListOf<Playlist>()
    private var recommend = mutableListOf<Music>()
    private var recommendPlaylist = mutableListOf<Playlist>()

    /**
     * 更新分类标签
     */
    private fun updateCate(name: String) {
        cateTagTv.text = name
        mPresenter?.loadNetease(name)
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_discover_new
    }

    /**
     * 初始化
     */
    override fun initViews() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.singerListTv -> {
                activity?.let { NavigationHelper.navigateFragment(it, QQArtistListFragment()) }
            }
            R.id.recommendSongsTv -> {
                val playlist = Playlist()
                playlist.pid = Constants.PLAYLIST_WY_RECOMMEND_ID
                playlist.type = Constants.PLAYLIST_WY_RECOMMEND_ID
                playlist.name = "每日推荐"
                activity?.let { NavigationHelper.navigateToPlaylist(it, playlist, null) }
            }
            R.id.seeAllArtistTv, R.id.hotSingerTv -> {
                activity?.let { NavigationHelper.navigateFragment(it, AllListFragment.newInstance(Constants.NETEASE_ARITIST_LIST, artists, channels)) }
            }
            R.id.seeAllRadioTv, R.id.radioTv -> {
                activity?.let { NavigationHelper.navigateFragment(it, AllListFragment.newInstance(Constants.BAIDU_RADIO_LIST, artists, channels)) }
            }
            R.id.personalFmTv -> {
                mPresenter?.loadPersonalFM()
            }
            R.id.catTag1Tv -> {
                updateCate("华语")
            }
            R.id.catTag2Tv -> {
                updateCate("流行")
            }
            R.id.catTag3Tv -> {
                updateCate("古风")
            }
            R.id.cateTagTv -> {
                startActivity<PlaylistSquareActivity>()
            }
        }
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun onLazyLoad() {
        mPresenter?.loadArtists()
        mPresenter?.loadRaios()
        mPresenter?.loadPersonalizedPlaylist()
    }

    override fun loadData() {
    }

    override fun listener() {
        catTag3Tv.setOnClickListener(this)
        cateTagTv.setOnClickListener(this)
        catTag1Tv.setOnClickListener(this)
        catTag2Tv.setOnClickListener(this)
        singerListTv.setOnClickListener(this)
        hotSingerTv.setOnClickListener(this)
        radioTv.setOnClickListener(this)
        personalFmTv.setOnClickListener(this)
        seeAllRadioTv.setOnClickListener(this)
        seeAllArtistTv.setOnClickListener(this)
        recommendSongsTv.setOnClickListener(this)
    }

    override fun showEmptyView(msg: String) {
        LogUtil.d(TAG, "errorMsg = $msg")
    }

    override fun retryLoading() {
        super.retryLoading()
        loadData()
    }

    /**
     * 显示百度排行榜
     */
    override fun showBaiduCharts(charts: MutableList<Playlist>) {
    }

    /**
     * 显示顶部横幅
     */
    override fun showBannerView(banners: MutableList<BannerBean>) {
        if (banners.size > 0) {
            mzBannerView.visibility = View.VISIBLE
            containerView.visibility = View.VISIBLE
            mzBannerView.setPages(banners as List<Nothing>) { activity?.let { BannerViewHolder(it) } }
        } else {
            mzBannerView.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        mzBannerView.start()
    }

    override fun onPause() {
        super.onPause()
        mzBannerView.pause()
    }

    /**
     * 显示网易云排行榜
     */
    override fun showNeteaseCharts(charts: MutableList<Playlist>) {
        this.playlist = charts
        if (mNeteaseAdapter == null) {
            //适配器
            mNeteaseAdapter = TopPlaylistAdapter(playlist)
            wangChartsRv?.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            wangChartsRv?.adapter = mNeteaseAdapter
            wangChartsRv?.isFocusable = false
            wangChartsRv?.isNestedScrollingEnabled = false
            mNeteaseAdapter?.setOnItemClickListener { adapter, view, position ->
                val playlist = adapter.data[position] as Playlist
                NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, playlist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
            }
        } else {
            mNeteaseAdapter?.setNewData(playlist)
        }
        playlistView.visibility = if (playlist.size > 0) View.VISIBLE else View.GONE
    }

    /**
     * 显示歌手榜单
     */
    override fun showArtistCharts(charts: MutableList<Artist>) {
        this.artists = charts
        if (mArtistListAdapter == null) {
            chartsArtistRcv?.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
            //适配器
            mArtistListAdapter = TopArtistListAdapter(artists)
            chartsArtistRcv?.adapter = mNeteaseAdapter
            chartsArtistRcv?.isFocusable = false
            chartsArtistRcv?.isNestedScrollingEnabled = false
            mArtistListAdapter?.setOnItemClickListener { adapter, view, position ->
                val artist = adapter.data[position] as Artist
                NavigationHelper.navigateToArtist(mFragmentComponent.activity, artist, Pair<View, String>(view.findViewById<View>(R.id.iv_cover), getString(R.string.transition_album)))
            }
        } else {
            mArtistListAdapter?.setNewData(artists)
        }
        artistView.visibility = if (artists.size <= 0) View.GONE else View.VISIBLE
    }

    /**
     * 显示电台列表
     */
    override fun showRadioChannels(channels: MutableList<Playlist>) {
        this.channels = channels
        if (mRadioAdapter == null) {
            //适配器
            mRadioAdapter = BaiduRadioAdapter(this.channels)
            //电台列表
            radioRsv?.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
            radioRsv?.adapter = mRadioAdapter
            radioRsv?.isFocusable = false
            radioRsv?.isNestedScrollingEnabled = false
            mRadioAdapter?.setOnItemClickListener { _, view, position ->
                NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, channels[position], Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
            }
        } else {
            mRadioAdapter?.setNewData(this.channels)
        }
        if (channels.size > 0) {
            containerView.visibility = View.VISIBLE
            radioView.visibility = View.VISIBLE
        } else {
            radioView.visibility = View.GONE
        }
    }

    /**
     * 显示推荐歌单
     */
    override fun showRecommendPlaylist(playlists: MutableList<Playlist>) {
        LogUtil.d(TAG, "获取推荐歌单 songs：" + playlists.size)
        recommendPlaylistView.visibility = if (playlists.size == 0) View.GONE else View.VISIBLE
        if (playlists.size > 6) {
            this.recommendPlaylist = playlists.subList(0, 6)
        } else {
            this.recommendPlaylist = playlists
        }
        if (mPlaylistAdapter == null) {
            mPlaylistAdapter = TopPlaylistAdapter(recommendPlaylist)
            //推荐列表
            recommendPlaylistRsv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)
            recommendPlaylistRsv.adapter = mPlaylistAdapter
            recommendPlaylistRsv.isFocusable = false
            recommendPlaylistRsv.isNestedScrollingEnabled = false
            mPlaylistAdapter?.setOnItemClickListener { adapter, view, position ->
                val playlist = adapter.data[position] as Playlist
                NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, playlist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
            }
        } else {
            mPlaylistAdapter?.setNewData(this.recommendPlaylist)
        }
        recommendPlaylistView.visibility = if (recommendPlaylist.size <= 0) View.GONE else View.VISIBLE
    }

    /**
     * 显示推荐歌曲
     */
    override fun showRecommendSongs(songs: MutableList<Music>) {
        LogUtil.d(TAG, "获取推荐歌曲 songs：" + songs.size)
        recommendView.visibility = if (songs.size == 0) View.GONE else View.VISIBLE
        this.recommend = songs
        if (mMusicAdapter == null) {
            mMusicAdapter = SongAdapter(recommend)
            //推荐列表
            recommendRsv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recommendRsv.adapter = mMusicAdapter
            recommendRsv.isFocusable = false
            recommendRsv.isNestedScrollingEnabled = false

            mMusicAdapter?.setOnItemClickListener { _, view, position ->
                if (view.id != R.id.iv_more) {
                    PlayManager.play(position, recommend, Constants.PLAYLIST_LOVE_ID)
                    mMusicAdapter?.notifyDataSetChanged()
                }
            }
        } else {
            mMusicAdapter?.setNewData(recommend)
        }
    }

    /**
     * 播放私人FM
     */
    override fun showPersonalFm(playlist: Playlist) {
        PlayManager.play(0, playlist.musicList, playlist.pid)
    }


    companion object {
        fun newInstance(): NewDiscoverFragment {
            val args = Bundle()

            val fragment = NewDiscoverFragment()
            fragment.arguments = args
            return fragment
        }
    }

}